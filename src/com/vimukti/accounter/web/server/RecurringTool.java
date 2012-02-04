package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.MessageOrTask;
import com.vimukti.accounter.core.RecurringTransaction;
import com.vimukti.accounter.core.Reminder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class RecurringTool extends Thread {

	Logger log = Logger.getLogger(RecurringTool.class);

	@Override
	public void run() {
		if (ServerLocal.get() == null) {
			ServerLocal.set(Locale.ENGLISH);
		}
		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction hibernateTransaction = null;
		try {
			hibernateTransaction = session.beginTransaction();

			List<Company> companies = session.getNamedQuery(
					"getCompany.has.recurrings").list();

			log.info("Recurring Schedules is on process : "
					+ new Date().toString());

			for (Company company : companies) {
				String timezone = company.getTimezone();
				if (timezone == null) {
					timezone = "UTC+0:00 Etc/Universal";
				}
				String timeZoneID = timezone.substring(0,
						timezone.indexOf(":") + 3);
				timeZoneID = timeZoneID.replaceFirst("UTC", "GMT");

				FinanceDate date = new FinanceDate(
						getDateInTimeZone(timeZoneID));

				checkForCompanyRecurrings(company, date);
			}
			hibernateTransaction.commit();
			log.info("Recurring schedule completed");
		} catch (Exception e) {
			e.printStackTrace();
			if (hibernateTransaction != null) {
				hibernateTransaction.rollback();
			}
			log.error("Error occured");
		} finally {
			session.close();
		}
	}

	public Date getDateInTimeZone(String timeZoneId) {
		Date currentDate = new Date();
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
		Calendar mbCal = new GregorianCalendar(timeZone);
		mbCal.setTimeInMillis(currentDate.getTime());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));

		return cal.getTime();
	}

	public void checkForCompanyRecurrings(Company company, FinanceDate date)
			throws AccounterException, CloneNotSupportedException {
		Session session = HibernateUtil.getCurrentSession();
		List<RecurringTransaction> recurrings = new ArrayList<RecurringTransaction>();
		List<RecurringTransaction> list1 = session
				.getNamedQuery("getRecurrringsBeforeDate")
				.setParameter("date", date).setEntity("company", company)
				.list();

		List<RecurringTransaction> list2 = session
				.getNamedQuery("getRecurrrings.remind.days.before")
				.setEntity("company", company).list();

		recurrings.addAll(list1);
		recurrings.addAll(list2);

		executeRecurringSchedules(recurrings, date, company);
	}

	public void executeRecurringSchedules(
			List<RecurringTransaction> recurrings, FinanceDate date,
			Company company) throws AccounterException,
			CloneNotSupportedException {

		Session session = HibernateUtil.getCurrentSession();

		boolean addAutoTraxTask = false, addRecReminder = false;
		FinanceDate transactionDate = null;
		FinanceDate remind = null;
		for (RecurringTransaction recurringTransaction : recurrings) {
			FinanceDate nextScheduleOn = recurringTransaction
					.getNextScheduleOn();
			AccounterThreadLocal.set(recurringTransaction.getCreatedBy());
			Calendar cal = Calendar.getInstance();
			cal.setTime(nextScheduleOn.getAsDateObject());
			cal.add(Calendar.DAY_OF_MONTH,
					-recurringTransaction.getDaysBeforeToRemind());
			FinanceDate remindDate = new FinanceDate(cal.getTime());
			if (remindDate.compareTo(date) < 1) {
				if (recurringTransaction.getType() == RecurringTransaction.RECURRING_SCHEDULED) {
					Transaction transaction = FinanceTool
							.createDuplicateTransaction(recurringTransaction);
					transaction.setSaveStatus(Transaction.STATUS_APPROVE);
					transaction.setAutomaticTransaction(true);
					session.saveOrUpdate(transaction);
					if (recurringTransaction.isNotifyCreatedTransaction()) {
						addAutoTraxTask = true;
						if (transactionDate == null) {
							transactionDate = remindDate;
						} else if (remindDate.before(transactionDate)) {
							transactionDate = remindDate;
						}
					}
				} else if (recurringTransaction.getType() == RecurringTransaction.RECURRING_REMINDER) {
					Reminder reminder = new Reminder(recurringTransaction);
					session.saveOrUpdate(reminder);
					addRecReminder = true;
					if (remind == null) {
						remind = remindDate;
					} else if (remindDate.before(remind)) {
						remind = remindDate;
					}
				}

				// Schedule again for next recurring
				recurringTransaction.scheduleAgain();

				// Stop the recurring according to end date type.
				if (recurringTransaction.getEndDateType() == RecurringTransaction.END_DATE_DATE) {
					if (recurringTransaction.getNextScheduleOn() == null
							|| !recurringTransaction.getEndDate().after(
									recurringTransaction.getNextScheduleOn())) {
						recurringTransaction.setStopped(true);
					}
				} else if (recurringTransaction.getEndDateType() == RecurringTransaction.END_DATE_OCCURRENCES) {
					if (recurringTransaction.getOccurencesCount() <= recurringTransaction
							.getOccurencesCompleted()) {
						recurringTransaction.setStopped(true);
					}
				}
				if (recurringTransaction.isStopped()
						&& recurringTransaction.isAlertWhenEnded()) {
					MessageOrTask message = new MessageOrTask();
					message.setType(MessageOrTask.TYPE_MESSAGE);
					message.setDate(remindDate == null ? nextScheduleOn
							: remindDate);
					message.setSystemCreated(true);
					message.setContent(Global
							.get()
							.messages()
							.recurringTemplateHasEnded(
									recurringTransaction.getName()));
					int transactionType = recurringTransaction.getTransaction()
							.getType();
					AccounterCoreType type = UIUtils
							.getAccounterCoreType(transactionType);
					StringBuffer token = new StringBuffer(
							getActionToken(transactionType));
					token.append("?");
					token.append(type.toString().toLowerCase());
					token.append(":");
					token.append(recurringTransaction.getTransaction().getID());
					message.setActionToken(token.toString());
					message.setCompany(recurringTransaction.getCompany());
					session.saveOrUpdate(message);
				}

				session.saveOrUpdate(recurringTransaction);
			}
		}

		if (addAutoTraxTask) {
			createAutomaticTransactionMessage(transactionDate, company);
		}
		if (addRecReminder) {
			createRecurringReminderTask(remind, company);
		}
	}

	private void createRecurringReminderTask(FinanceDate remindDate,
			Company company) {
		Session session = HibernateUtil.getCurrentSession();

		MessageOrTask task = (MessageOrTask) session
				.getNamedQuery("getRecurringReminderTask")
				.setParameter("companyId", company.getId()).uniqueResult();

		if (task == null) {
			task = new MessageOrTask();
			task.setType(MessageOrTask.TYPE_TASK);
			task.setSystemCreated(true);
			task.setContent(Global.get().messages()
					.respondToYourRemindersAboutRecurringTemplates());
			task.setContentType(MessageOrTask.TYPE_RECURRING_REMINDER);
		}
		task.setActionToken(HistoryTokens.RECURRINGREMINDERS);
		if (task.getDate() == null || remindDate.before(task.getDate())) {
			task.setDate(remindDate);
		}
		task.setCompany(company);
		session.saveOrUpdate(task);
	}

	private void createAutomaticTransactionMessage(FinanceDate transactionDate,
			Company company) {
		Session session = HibernateUtil.getCurrentSession();

		MessageOrTask message = (MessageOrTask) session
				.getNamedQuery("getAutomaticTransactionsMessage")
				.setParameter("companyId", company.getId()).uniqueResult();

		if (message == null) {
			message = new MessageOrTask();
			message.setType(MessageOrTask.TYPE_MESSAGE);
			message.setSystemCreated(true);
			message.setContent(Global.get().messages()
					.transactionsWereAutomaticallyCreated());
			message.setContentType(MessageOrTask.TYPE_AUTOMATIC_TRANSACTIONS);
		}
		message.setActionToken(HistoryTokens.AUTOMATICTRANSACTIONS);
		if (message.getDate() == null
				|| transactionDate.before(message.getDate())) {
			message.setDate(transactionDate);
		}
		message.setCompany(company);
		session.saveOrUpdate(message);
	}

	private String getActionToken(int type) {
		String token = "";
		switch (type) {

		case ClientTransaction.TYPE_TRANSFER_FUND:
			token = HistoryTokens.DEPOSITETRANSFERFUNDS;
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			token = HistoryTokens.ENTERBILL;
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			token = HistoryTokens.NEWCASHPURCHASE;
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			token = HistoryTokens.NEWCASHSALE;
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			token = HistoryTokens.WRITECHECK;
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			token = HistoryTokens.NRECREDITNOTE;
			break;
		case ClientTransaction.TYPE_INVOICE:
			token = HistoryTokens.NEWINVOICE;
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			token = HistoryTokens.NEWQUOTE;
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			token = HistoryTokens.VENDORCREDIT;
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			token = "cashExpense";
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			token = "creditCardExpense";
			break;

		}
		return token;
	}
}