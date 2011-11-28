package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public class InvoiceListCommand extends NewAbstractCommand {

	private static final String THIS_WEEK = "This week";
	private static final String THIS_MONTH = "This month";
	private static final String LAST_WEEK = "Last week";
	private static final String LAST_MONTH = "Last month";
	private static final String THIS_FINANCIAL_YEAR = "This financial year";
	private static final String LAST_FINANCIAL_YEAR = "Last financial year";
	private static final String THIS_FINANCIAL_QUARTER = "This financial quarter";
	private static final String lAST_FINANCIAL_QUARTER = "Last financial quarter";
	private static final String FINANCIAL_YEAR_TO_DATE = "Financial year to date";
	private ClientFinanceDate startDate;
	private ClientFinanceDate endDate;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {

		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

		get(VIEW_BY).setDefaultValue(getMessages().open());
		get(FROM_DATE).setDefaultValue(new ClientFinanceDate());
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
		get(DATE_RANGE).setDefaultValue(getMessages().all());

	}

	@Override
	public String getSuccessMessage() {

		return "Success";
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().open());
				list.add(getMessages().overDue());
				list.add(getMessages().voided());
				list.add(getMessages().all());
				return list;
			}
		});
		list.add(new CommandsRequirement(DATE_RANGE, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().thisWeek());
				list.add(getMessages().thisMonth());
				list.add(getMessages().lastWeek());
				list.add(getMessages().lastMonth());
				list.add(getMessages().thisFinancialYear());
				list.add(getMessages().lastFinancialYear());
				list.add(getMessages().thisFinancialQuarter());
				return list;
			}
		});

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), true, true));

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), true, true));

		list.add(new ShowListRequirement<InvoicesList>("Invoices",
				"Please select", 20) {

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// InvoicesList value) {
			// commandList.add(new UserCommand("update transaction", value
			// .getNumber()));
			// commandList.add(new UserCommand("Void transaction", value
			// .getType() + " " + value.getTransactionId()));
			//
			// }

			@Override
			protected String onSelection(InvoicesList value) {
				return "Edit Transaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().invoices();
			}

			@Override
			protected String getEmptyString() {

				return getMessages()
						.youDontHaveAny(getMessages().invoiceList());

			}

			@Override
			protected Record createRecord(InvoicesList value) {
				Record record = new Record(value);
				record.add("", value.getCustomerName());
				record.add("", value.getDate());
				record.add("", value.getBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create Invoice");

			}

			@Override
			protected boolean filter(InvoicesList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<InvoicesList> getLists(Context context) {
				return getInvoices(context);
			}
		});
	}

	private List<InvoicesList> getInvoices(Context context) {

		String dateRange = get(DATE_RANGE).getValue();
		String viewType = get(VIEW_BY).getValue();
		startDate = get(FROM_DATE).getValue();
		endDate = get(TO_DATE).getValue();
		List<InvoicesList> invoices;
		try {

			if (dateRange != null) {
				invoices = dateRangeChanged(context, dateRange, startDate,
						endDate);
			} else {
				invoices = new FinanceTool().getInventoryManager()
						.getInvoiceList(context.getCompany().getId(),
								startDate, endDate);
			}
			List<InvoicesList> list = new ArrayList<InvoicesList>(
					invoices.size());
			for (InvoicesList invoice : invoices) {
				if (viewType.equals(getMessages().open())) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getStatus() != ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}

				} else if (viewType.equals(getMessages().overDue())) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getDueDate().compareTo(
									new ClientFinanceDate()) < 0)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(getMessages().voided())) {
					if (invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(getMessages().all())) {
					list.add(invoice);
				}
			}

			return list;
		} catch (DAOException e) {
		}
		return null;
	}

	public List<InvoicesList> dateRangeChanged(Context context,
			String dateRange, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = fromDate;
		endDate = toDate;
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(THIS_WEEK)) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(THIS_MONTH)) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals(LAST_WEEK)) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(LAST_MONTH)) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(THIS_FINANCIAL_YEAR)) {
			startDate = CommandUtils.getCurrentFiscalYearStartDate(context
					.getPreferences());
			endDate = CommandUtils.getCurrentFiscalYearEndDate(context
					.getPreferences());
		}
		if (dateRange.equals(LAST_FINANCIAL_YEAR)) {

			startDate = CommandUtils.getCurrentFiscalYearStartDate(context
					.getPreferences());
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(CommandUtils.getCurrentFiscalYearEndDate(
					context.getPreferences()).getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals(THIS_FINANCIAL_QUARTER)) {
			startDate = new ClientFinanceDate();
			endDate = CommandUtils.getCurrentFiscalYearEndDate((context
					.getPreferences()));
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(lAST_FINANCIAL_QUARTER)) {
			startDate = new ClientFinanceDate();
			endDate = CommandUtils.getCurrentFiscalYearEndDate(context
					.getPreferences());
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(FINANCIAL_YEAR_TO_DATE)) {
			startDate = CommandUtils.getCurrentFiscalYearStartDate(context
					.getPreferences());
			endDate = new ClientFinanceDate();
		}
		try {
			return new FinanceTool().getInventoryManager().getInvoiceList(
					context.getCompany().getId(), startDate, endDate);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}
}