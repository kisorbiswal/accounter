package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXRateCalculation;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.CompanyPreferencesView;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

public class ReportManager extends Manager {
	public ArrayList<TrialBalance> getCashFlowReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		// Calendar cal = Calendar.getInstance();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// cal.setTime(startDate.getAsDateObject());

		// cal.add(Calendar.DAY_OF_MONTH, -1);
		//
		// String end = cal.get(Calendar.YEAR) + "-";
		// end += ((((cal.get(Calendar.MONTH) + 1) + "").length() == 1) ? "0"
		// + cal.get(Calendar.MONTH) : cal.get(Calendar.MONTH) + 1)
		// + "-";
		// end += (((cal.get(Calendar.DAY_OF_MONTH)) + "").length() == 1) ? "0"
		// + cal.get(Calendar.DAY_OF_MONTH) : cal
		// .get(Calendar.DAY_OF_MONTH);

		long end = date[1] != null ? date[0].getDate() : endDate.getDate();

		List l = ((Query) session.getNamedQuery("getCashFlowStatement")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end)).list();

		double netIncome = 0.0;
		netIncome = getNetIncome(startDate, endDate, "getNetIncome", companyId);

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		TrialBalance netIncomeTB = new TrialBalance();
		netIncomeTB.setAccountName("Net Income");
		netIncomeTB.setAmount(netIncome);
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((BigInteger) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((BigInteger) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			if (t.getAccountType() == Account.TYPE_ACCOUNT_RECEIVABLE
					|| t.getAccountType() == Account.TYPE_OTHER_CURRENT_ASSET
					|| t.getAccountType() == Account.TYPE_FIXED_ASSET
					|| t.getAccountType() == Account.TYPE_OTHER_ASSET) {

				t.setAmount(object[6] == null ? 0 : -1
						* ((Double) object[6]).doubleValue());
			} else {
				t.setAmount(object[6] == null ? 0 : ((Double) object[6])
						.doubleValue());
			}

			t.setCashAtBeginningOfPeriod((object[7] == null ? 0.0
					: ((Double) object[7]).doubleValue()));
			t.setAccountFlow((String) object[8]);
			t.setBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setSubBaseType(object[10] == null ? 0 : (Integer) object[10]);
			t.setGroupType(object[11] == null ? 0 : (Integer) object[11]);

			queryResult.add(t);
			// }

		}
		List<TrialBalance> sortedList = sortTheList(queryResult);
		sortedList.add(0, netIncomeTB);
		return new ArrayList<TrialBalance>(sortedList);

	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransactionDetailByTaxItem")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		if (l.size() > 0) {
			return createTransactionDetailByTaxItemEntries(new ArrayList<TransactionDetailByTaxItem>(
					l));
		} else
			return null;

	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery(
						"getTransactionDetailByTaxItemForParticularTaxItem")
				.setParameter("companyId", companyId)
				.setParameter("taxItemName", taxItemName)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		if (l.size() > 0) {
			return createTransactionDetailByTaxItemEntries(new ArrayList<TransactionDetailByTaxItem>(
					l));
		} else
			return null;

	}

	private ArrayList<TransactionDetailByTaxItem> createTransactionDetailByTaxItemEntries(
			List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TransactionDetailByTaxItem> queryResult = new ArrayList<TransactionDetailByTaxItem>();
		while ((iterator).hasNext()) {

			TransactionDetailByTaxItem TransactionDetailByTaxItem = new TransactionDetailByTaxItem();
			object = (Object[]) iterator.next();
			TransactionDetailByTaxItem.setTransactionId((Long) object[0]);
			// transactionDetailByTaxcode
			// .setType(object[1]== null ? 0 :((Integer) object[1]).intValue());
			TransactionDetailByTaxItem.setPayeeName((String) object[1]);
			TransactionDetailByTaxItem.setTaxAgencyName((String) object[2]);
			TransactionDetailByTaxItem.setTaxItemName((String) object[3]);
			TransactionDetailByTaxItem.setRate(object[4] == null ? 0
					: ((Double) object[4]).doubleValue());
			TransactionDetailByTaxItem.setTransactionType(object[5] == null ? 0
					: ((Integer) object[5]).intValue());
			TransactionDetailByTaxItem.setDate(new ClientFinanceDate(
					(Long) object[6]));
			TransactionDetailByTaxItem.setNumber((String) object[7]);

			/*
			 * if (object[8] == null || object[8].equals("")) {
			 * 
			 * transactionDetailByTaxcode.setMemo("");
			 * 
			 * } else { Clob cl = (Clob) object[8]; StringBuffer strOut = new
			 * StringBuffer(); String aux; try { BufferedReader br = new
			 * BufferedReader(cl .getCharacterStream()); while ((aux =
			 * br.readLine()) != null) strOut.append(aux);
			 * transactionDetailByTaxcode.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */
			TransactionDetailByTaxItem.setMemo((String) object[8]);
			TransactionDetailByTaxItem.setTaxableAmount(((Double) object[9])
					.doubleValue());
			TransactionDetailByTaxItem.setSalesTaxAmount(((Double) object[10])
					.doubleValue());
			TransactionDetailByTaxItem.setIsVoid(((Boolean) object[11])
					.booleanValue());

			queryResult.add(TransactionDetailByTaxItem);
		}
		return new ArrayList<TransactionDetailByTaxItem>(queryResult);
	}

	public ArrayList<ProfitAndLossByLocation> getProfitAndLossByLocation(
			boolean isLocation, FinanceDate startDate, FinanceDate endDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = ((FinanceDate) ((session
				.getNamedQuery("getFiscalYear.by.check.isCurrentFiscalYearistrue")
				.setParameter("company", company)).list().get(0)));

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);
		List l;
		if (isLocation) {
			l = ((Query) session.getNamedQuery("getProfitAndLossByLocation")
					.setParameter("companyId", companyId)

					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())).list();
		} else {
			l = ((Query) session.getNamedQuery("getProfitAndLossByClass")
					.setParameter("companyId", companyId)

					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())).list();
		}

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<ProfitAndLossByLocation> queryResult = new ArrayList<ProfitAndLossByLocation>();
		long previousAccountID = 0;
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			long accountId = ((BigInteger) object[0]).longValue();
			if (previousAccountID == 0 || previousAccountID != accountId) {
				previousAccountID = accountId;
				ProfitAndLossByLocation record = new ProfitAndLossByLocation();
				record.setAccountId(accountId == 0 ? 0 : accountId);
				record.setAccountName(object[1] == null ? null
						: (String) object[1]);
				record.setAccountNumber(object[2] == null ? null
						: (String) object[2]);
				record.setAccountType(object[3] == null ? 0
						: ((Integer) object[3]).intValue());

				long location = object[4] == null ? 0
						: ((BigInteger) object[4]).longValue();
				double amount = object[5] == null ? 0 : (Double) object[5];

				record.getMap().put(location, amount);
				record.setParentAccount(object[6] == null ? 0
						: ((BigInteger) object[6]).longValue());
				queryResult.add(record);
			} else {
				ProfitAndLossByLocation record = queryResult.get(queryResult
						.size() - 1);
				long location = object[4] == null ? 0
						: ((BigInteger) object[4]).longValue();
				double amount = object[5] == null ? 0 : (Double) object[5];
				/* + record.getMap().get(location) */;
				record.getMap().get(location);
				record.getMap().put(location, amount);
			}
		}

		return new ArrayList<ProfitAndLossByLocation>(queryResult);
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();

			List<TransactionDetailByAccount> transactionDetailByAccountList = new ArrayList<TransactionDetailByAccount>();

			Query query = session
					.getNamedQuery("getTransactionDetailByAccount")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());

			List list = query.list();

			if (list != null && list.size() > 0) {
				createTransasctionDetailByAccount(list,
						transactionDetailByAccountList);
			}

			if (transactionDetailByAccountList != null) {
				return new ArrayList<TransactionDetailByAccount>(
						transactionDetailByAccountList);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	private void createTransasctionDetailByAccount(List list,
			List<TransactionDetailByAccount> transactionMakeDepositsList)
			throws DAOException {
		Object object[] = null;

		Iterator i = list.iterator();
		while (i.hasNext()) {

			object = (Object[]) i.next();
			TransactionDetailByAccount transactionDetailByAccount = new TransactionDetailByAccount();

			transactionDetailByAccount.setAccountName((object[0] == null ? null
					: ((String) object[0])));
			transactionDetailByAccount.setTransactionType(object[3] == null ? 0
					: (((Integer) object[3]).intValue()));
			transactionDetailByAccount
					.setTotal(object[6] != null ? (Double) object[6] : 0.0);

			if (transactionDetailByAccount.getAccountName().equals(
					AccounterServerConstants.SALES_TAX_VAT_UNFILED)
					&& transactionDetailByAccount.getTransactionType() == Transaction.TYPE_VAT_RETURN) {
				if (transactionDetailByAccount.getTotal() < 0)
					transactionDetailByAccount.setName("Box3");
				if (transactionDetailByAccount.getTotal() > 0)
					transactionDetailByAccount.setName("Box4");

			} else
				transactionDetailByAccount.setName((object[1] == null ? null
						: ((String) object[1])));

			transactionDetailByAccount.setTransactionId((Long) object[2]);

			transactionDetailByAccount
					.setTransactionDate(object[4] == null ? null
							: new ClientFinanceDate((Long) object[4]));
			transactionDetailByAccount.setTransactionNumber((String) object[5]);

			transactionDetailByAccount
					.setTotal(object[6] != null ? (Double) object[6] : 0.0);
			transactionDetailByAccount.setMemo((String) object[7]);
			transactionMakeDepositsList.add(transactionDetailByAccount);

		}
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();

			Query query = session
					.getNamedQuery(
							"getTransactionDetailByAccount_ForParticularAccount")
					.setParameter("companyId", companyId)
					.setParameter("accountName", accountName).setParameter(

					"startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate());

			List<TransactionDetailByAccount> transactionDetailByAccountList = new ArrayList<TransactionDetailByAccount>();
			List list = query.list();

			if (list != null && list.size() > 0) {
				createTransasctionDetailByAccount(list,
						transactionDetailByAccountList);
			}

			if (transactionDetailByAccountList != null) {
				return new ArrayList<TransactionDetailByAccount>(
						transactionDetailByAccountList);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	private List<TrialBalance> sortTheList(List<TrialBalance> queryResult) {
		List<TrialBalance> sortedList = new ArrayList<TrialBalance>();

		for (TrialBalance t : queryResult) {

			if (t.getParentAccount() == 0) {

				if (!sortedList.contains(t)) {
					String str = t.getAccountFlow();
					// if (t.getAmount() != 0.0) {
					sortedList.add(t);
					// }
					getChilds(str, queryResult, sortedList);
				}
			}

		}

		return removeUnwantedEntries(sortedList);
	}

	private void getChilds(String str, List<TrialBalance> queryResult,
			List<TrialBalance> sortedList) {

		for (TrialBalance t : queryResult) {

			if (t.getAccountFlow().startsWith(str + ".")) {

				if (!t.getAccountFlow().substring(str.length() + 1)
						.contains(".")) {

					if (!sortedList.contains(t)) {
						// if (t.getAmount() != 0.0) {
						sortedList.add(t);
						// }
						str = t.getAccountFlow();

						getChilds(str, queryResult, sortedList);
					}
				}
			}
		}
		if (str.contains(".")) {

			getChilds(str.substring(0, str.lastIndexOf(".")), queryResult,
					sortedList);
		} else {
			return;
		}

	}

	private double getNetIncome(FinanceDate startDate, FinanceDate endDate,
			String query, long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		Query q = session.getNamedQuery(query)
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l1 = q.list();
		double netIncome = 0.0;
		if (l1 != null && l1.size() > 0) {
			if (l1.get(0) != null)
				netIncome = (Double) l1.get(0);
		}
		return netIncome;
	}

	private ArrayList<TrialBalance> removeUnwantedEntries(
			List<TrialBalance> sortTheList) {
		ArrayList<TrialBalance> list = new ArrayList<TrialBalance>();
		for (TrialBalance tb : sortTheList) {
			if (!DecimalUtil.isEquals(tb.getAmount(), 0.0)
					|| !DecimalUtil.isEquals(tb.getTotalAmount(), 0.0)
					|| hasChilds(tb, sortTheList)) {
				list.add(tb);
			}
		}
		return list;
	}

	private boolean hasChilds(TrialBalance tb, List<TrialBalance> sortTheList) {

		for (TrialBalance balance : sortTheList) {
			if (balance.getAccountFlow().startsWith(tb.getAccountFlow() + "."))
				return true;
		}

		return false;
	}

	public ArrayList<TrialBalance> getTrialBalance(final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTrialBalance")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {
			t.setAccountId(((Long) object[0]));
			t.setAccountName(object[1] == null ? null : (String) object[1]);
			t.setAccountNumber(object[2] == null ? null : (String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());

			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((Long) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}

			t.setAmount(object[6] == null ? 0d : ((Double) object[6])
					.doubleValue());
			if (DecimalUtil.isLessThan(t.getAmount(), 0.0)) {
				t.setCreditAmount(-1 * t.getAmount());

			} else {
				t.setDebitAmount(t.getAmount());
			}

			t.setAccountFlow((String) object[7]);
			queryResult.add(t);

			// }

		}
		return removeUnwantedEntries(new ArrayList<TrialBalance>(queryResult));

		// Object obj = template.execute(new HibernateCallback(){
		// Ac
		// public Object doInHibernate(Session session)
		// throws HibernateException, SQLException {
		//
		// return new TrialBalanceReport(session,startDate,endDate);
		// }
		// });
		//
		// return obj !=null?(TrialBalanceReport)obj:null;

	}

	public ArrayList<TrialBalance> getBalanceSheetReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session.getNamedQuery("getBalanceSheet")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		double netIncome = 0.0;
		netIncome = getNetIncome(startDate, endDate,
				"getNetIncome_Closing_postings_Included", companyId);

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();

		TrialBalance netIncomeTB = new TrialBalance();
		netIncomeTB.setAccountName("Net Profit");
		netIncomeTB.setBaseType(Account.BASETYPE_EQUITY);
		netIncomeTB.setSubBaseType(Account.SUBBASETYPE_EQUITY);
		// netIncomeTB.setAccountNumber("3100");
		netIncomeTB.setAmount(netIncome);

		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((BigInteger) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((BigInteger) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			t.setAmount(object[6] == null ? 0 : ((Double) object[6])
					.doubleValue());
			t.setAccountFlow((String) object[7]);
			t.setBaseType(object[8] == null ? 0 : (Integer) object[8]);
			t.setSubBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setGroupType(object[10] == null ? 0 : (Integer) object[10]);

			if (t.getAmount() != 0) {
				queryResult.add(t);
			}

			// }

		}
		// List<TrialBalance> sortedList = sortTheList(queryResult);
		// sortedList.add(netIncomeTB);

		// List<TrialBalance> sortedList = getBalanceSheetSorted(queryResult);
		// sortedList.add(netIncomeTB);
		queryResult.add(netIncomeTB);

		return new ArrayList<TrialBalance>(queryResult);

	}

	public ArrayList<TrialBalance> getProfitAndLossReport(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = ((FinanceDate) ((session
				.getNamedQuery("getFiscalYear.by.check.isCurrentFiscalYearistrue")
				.setParameter("company", company)).list().get(0)));

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);
		// + ((month + "").length() == 1 ? "0" + month : month) + "01");

		List l = ((Query) session.getNamedQuery("getProfitAndLoss")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("startDate1", startDate1.getDate())
				.setParameter("endDate1", endDate1.getDate())).list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TrialBalance> queryResult = new ArrayList<TrialBalance>();
		while ((iterator).hasNext()) {

			TrialBalance t = new TrialBalance();
			object = (Object[]) iterator.next();

			// this condition is to filter unnecessary rows
			// if ((object[6] == null ? 0 : ((Double) object[6]).doubleValue())
			// != 0.0) {

			t.setAccountId(((BigInteger) object[0]).longValue());
			t.setAccountName((String) object[1]);
			t.setAccountNumber((String) object[2]);
			t.setAccountType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			t.setCashFlowCategory(object[4] == null ? 0 : ((Integer) object[4])
					.intValue());
			Account parentAccount = (object[5] == null) ? null
					: (Account) session.get(Account.class,
							((BigInteger) object[5]).longValue());
			if (parentAccount != null) {
				t.setParentAccount(parentAccount.getID());
			}
			t.setAmount(object[6] == null ? 0 : ((Double) object[6])
					.doubleValue());
			t.setTotalAmount(object[7] == null ? 0 : ((Double) object[7])
					.doubleValue());
			t.setAccountFlow((String) object[8]);
			t.setBaseType(object[9] == null ? 0 : (Integer) object[9]);
			t.setSubBaseType(object[10] == null ? 0 : (Integer) object[10]);
			t.setGroupType(object[11] == null ? 0 : (Integer) object[11]);

			queryResult.add(t);
			// }

		}
		List<TrialBalance> sortedResult = new ArrayList<TrialBalance>();
		List<TrialBalance> otherExpenseList = new ArrayList<TrialBalance>();

		sortedResult = sortTheList(queryResult);
		int index = 0;
		Iterator iter = sortedResult.listIterator();
		while (iter.hasNext()) {
			TrialBalance tb = (TrialBalance) iter.next();
			if (tb.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				index = sortedResult.indexOf(tb);
			if (tb.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
				otherExpenseList.add(tb);
				iter.remove();
			}
		}

		if (otherExpenseList.size() != 0)
			sortedResult.addAll((index + 1), otherExpenseList);
		return new ArrayList<TrialBalance>(sortedResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session
				.getNamedQuery("getSalesByCustomerDetailForParticularCustomer")
				.setParameter("companyId", companyId)
				.setParameter("customerName", customerName)
				.setParameter("startDate",

				startDate.getDate()).setParameter("endDate", endDate.getDate()))
				.list();

		return createSalesByCustomerDetailReport(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	private ArrayList<SalesByCustomerDetail> createSalesByCustomerDetailReport(
			List l) {

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();

			salesByCustomerDetail.setName((String) object[0]);
			salesByCustomerDetail.setType(object[1] == null ? 0
					: ((Integer) object[1]).intValue());
			salesByCustomerDetail.setDate(new ClientFinanceDate(
					((BigInteger) object[2]).longValue()));

			salesByCustomerDetail.setNumber((String) object[3]);
			/*
			 * Clob cl = (Clob) object[4]; if (cl == null) {
			 * 
			 * salesByCustomerDetail.setMemo("");
			 * 
			 * } else {
			 * 
			 * StringBuffer strOut = new StringBuffer(); String aux; try {
			 * BufferedReader br = new BufferedReader(cl .getCharacterStream());
			 * while ((aux = br.readLine()) != null) strOut.append(aux);
			 * salesByCustomerDetail.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */
			salesByCustomerDetail.setMemo((String) object[4]);
			salesByCustomerDetail.setDueDate(object[5] == null ? null
					: new ClientFinanceDate(((BigInteger) object[5])
							.longValue()));
			salesByCustomerDetail.setPaymentTermName((String) object[6]);
			salesByCustomerDetail.setItemName((String) object[7]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[8] == null ? 0 : ((Double) object[8])
					.intValue());
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setUnitPrice(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			salesByCustomerDetail.setAmount(object[10] == null ? 0
					: ((Double) object[10]).doubleValue());
			salesByCustomerDetail.setDeliveryDate(object[11] == null ? null
					: new ClientFinanceDate(((BigInteger) object[11])
							.longValue()));
			salesByCustomerDetail.setIsVoid(object[12] == null ? true
					: ((Boolean) object[12]).booleanValue());
			salesByCustomerDetail.setReference((String) object[13]);
			salesByCustomerDetail.setTransactionId((object[14] == null ? 0
					: (((BigInteger) object[14]).longValue())));
			queryResult.add(salesByCustomerDetail);
		}
		// return prepareSalesPurchaseEntriesForVoid(queryResult);
		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesByCustomerDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();

		return createSalesByCustomerDetailReport(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(startDate.getAsDateObject());
		cal.add(Calendar.DAY_OF_MONTH, -1);

		long end = cal.get(Calendar.YEAR);
		end = (end * 100) + (cal.get(Calendar.MONTH) + 1);
		end = (end * 100) + cal.get(Calendar.DAY_OF_MONTH);

		Query query = session.getNamedQuery("getSalesTaxLiabilityReport")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end);

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesTaxLiability> queryResult = new ArrayList<SalesTaxLiability>();
		while ((iterator).hasNext()) {

			SalesTaxLiability salesTaxLiability = new SalesTaxLiability();
			object = (Object[]) iterator.next();
			salesTaxLiability.setTaxAgencyName(object[0] == null ? null
					: (String) object[0]);
			salesTaxLiability.setTaxItemName(object[1] == null ? null
					: (String) object[1]);
			salesTaxLiability.setTaxRate(object[2] == null ? 0.0
					: ((Double) object[2]).doubleValue());
			salesTaxLiability.setTaxCollected(object[3] == null ? 0.0
					: ((Double) object[3]).doubleValue());
			salesTaxLiability.setTotalSales(object[4] == null ? 0.0
					: ((Double) object[4]).doubleValue());
			salesTaxLiability.setTaxable(object[5] == null ? 0.0
					: ((Double) object[5]).doubleValue());
			salesTaxLiability.setNonTaxable(object[6] == null ? 0.0
					: ((Double) object[6]).doubleValue());
			// salesTaxLiability.setNonTaxableOther(object[7] == null ?
			// null:(Double)object[7]);
			salesTaxLiability.setBeginningBalance(object[7] == null ? 0.0
					: ((Double) object[7]).doubleValue());
			queryResult.add(salesTaxLiability);
		}
		return new ArrayList<SalesTaxLiability>(queryResult);
	}

	private void prepareVATDetailReport(VATDetailReport vatDetailReport,
			TAXAgency taxAgency, FinanceDate startDate, FinanceDate endDate,
			Company company) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		// /getting entries from VATRAteCalculation
		if (taxAgency != null) {

			query = session
					.getNamedQuery(
							"getTaxCalc.by.TaxAgencyId.and.withOtherDetails")
					.setParameter("taxAgency", taxAgency.getID())
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setEntity("company", company);
		} else {

			query = session
					.getNamedQuery(
							"getTaxrateCalc.by.TaxAgencyandItem.and.Dates")
					.setParameter("fromDate", startDate)
					.setParameter("toDate", endDate)
					.setEntity("company", company);
		}

		List<TAXRateCalculation> vats = query.list();

		List<VATDetail> vatDetails = new ArrayList<VATDetail>();

		for (TAXRateCalculation v : vats) {

			VATDetail vd = new VATDetail();

			vd.setBoxName(setVATBoxName(v));

			if (v.getTransactionItem().getTransaction().isAmountsIncludeVAT())
				vd.setNetAmount(v.getLineTotal() - v.getVatAmount());
			else
				vd.setNetAmount(v.getLineTotal());

			if (v.getTransactionItem().getTransaction().getInvolvedPayee() != null)
				vd.setPayeeName(v.getTransactionItem().getTransaction()
						.getInvolvedPayee().getName());

			/*
			 * Here all box values other than Vat code EGS (for purchase) of
			 * box2 and Vat code RC (for purchase) of box1 are getting it's
			 * native values
			 */

			if (vd.getBoxName().equals(
					VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
					|| (vd.getBoxName().equals(
							VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !v
							.isVATGroupEntry())
					|| (vd.getBoxName().equals(
							VATSummary.UK_BOX1_VAT_DUE_ON_SALES) && v
							.getTaxItem().getVatReturnBox().getTotalBox()
							.equals(AccounterServerConstants.BOX_NONE))) {
				double amount = -1 * (v.getVatAmount());
				// double amount = (!v.getTransactionItem().isVoid()) ? (-1 * (v
				// .getVatAmount())) : 0;
				vd.setTotal(amount);
			} else {
				double amount = v.getVatAmount();
				// double amount = (!v.getTransactionItem().isVoid()) ? v
				// .getVatAmount() : 0;
				vd.setTotal(amount);
			}
			vd.setTransactionDate(new ClientFinanceDate(v.getTransactionDate()
					.getDate()));
			vd.setTransactionName(v.getTransactionItem().getTransaction()
					.toString());
			vd.setTransactionNumber(v.getTransactionItem().getTransaction()
					.getNumber());
			vd.setTransactionType(v.getTransactionItem().getTransaction()
					.getType());
			vd.setVatRate(v.getTaxItem().getTaxRate());
			vd.setTransactionId(v.getTransactionItem().getTransaction().getID());
			vd.setPercentage(v.getTaxItem().isPercentage());
			vatDetailReport.getEntries().get(vd.getBoxName()).add(vd);

			// Adding Box2 entries to Box4
			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			//
			// VATDetail vd2 = new VATDetail();
			// vd2
			// .setBoxName(setVATBoxName(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
			//
			// vd2.setNetAmount(vd.getNetAmount());
			// vd2.setPayeeName(vd.getPayeeName());
			// vd2.setTotal(vd.getTotal());
			// vd2.setTransactionDate(vd.getTransactionDate());
			// vd2.setTransactionName(vd.getTransactionName());
			// vd2.setTransactionNumber(vd.getTransactionNumber());
			// vd2.setTransactionType(vd.getTransactionType());
			// vd2.setVatRate(vd.getVatRate());
			// vd2.setTransactionId(vd.getTransactionId());
			// vd2.setPercentage(v.getVatItem().isPercentage());
			//
			// vatDetailReport.getEntries().get(vd2.getBoxName()).add(vd2);
			// }

			// /////////////////////////////

			// Adding RC vat code transaction entry to Box4
			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES)
			// && (v.getVatItem().getVatReturnBox().getTotalBox()
			// .equals(AccounterConstants.BOX_NONE))) {
			//
			// VATDetail vd3 = new VATDetail();
			// vd3
			// .setBoxName(setVATBoxName(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
			//
			// vd3.setNetAmount(vd.getNetAmount());
			// vd3.setPayeeName(vd.getPayeeName());
			// vd3.setTotal(vd.getTotal());
			// vd3.setTransactionDate(vd.getTransactionDate());
			// vd3.setTransactionName(vd.getTransactionName());
			// vd3.setTransactionNumber(vd.getTransactionNumber());
			// vd3.setTransactionType(vd.getTransactionType());
			// vd3.setVatRate(vd.getVatRate());
			// vd3.setTransactionId(vd.getTransactionId());
			// vd3.setPercentage(v.getVatItem().isPercentage());
			//
			// vatDetailReport.getEntries().get(vd3.getBoxName()).add(vd3);
			// }
			// /////////////////////////////////////

			VATDetail vd1 = new VATDetail();

			vd1.setBoxName(setTotalBoxName(v));

			if ((vd1.getBoxName().equals(
					AccounterServerConstants.UK_BOX10_UNCATEGORISED) && v
					.getTransactionItem().getTransaction()
					.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER)
					|| !vd1.getBoxName().equals(
							AccounterServerConstants.UK_BOX10_UNCATEGORISED)) {

				if (v.getTransactionItem().getTransaction()
						.isAmountsIncludeVAT()) {
					double totalAmount = v.getLineTotal() - v.getVatAmount();
					// double totalAmount = (!v.getTransactionItem().isVoid()) ?
					// (v
					// .getLineTotal() - v.getVatAmount())
					// : 0;

					vd1.setTotal(totalAmount);
				}
				// vd1.setNetAmount();
				// vd1.setTotal(v.getVatAmount());
				else {
					double totalAmount = v.getLineTotal();
					// double totalAmount = (!v.getTransactionItem().isVoid()) ?
					// v
					// .getLineTotal() : 0;

					vd1.setTotal(totalAmount);
				}
				if (v.getTransactionItem().getTransaction().getInvolvedPayee() != null)
					vd1.setPayeeName(v.getTransactionItem().getTransaction()
							.getInvolvedPayee().getName());
				vd1.setTransactionDate(new ClientFinanceDate(v
						.getTransactionDate().getDate()));
				vd1.setTransactionName(v.getTransactionItem().getTransaction()
						.toString());
				vd1.setTransactionNumber(v.getTransactionItem()
						.getTransaction().getNumber());
				vd1.setTransactionType(v.getTransactionItem().getTransaction()
						.getType());
				// vd1.setVatRate(v.getTransactionItem().getLineTotal());
				vd1.setTransactionId(v.getTransactionItem().getTransaction()
						.getID());

				vatDetailReport.getEntries().get(vd1.getBoxName()).add(vd1);

			}

		}

		{

			// getting entries from VATAdjustment
			if (taxAgency != null) {
				query = session.getNamedQuery(

				"getTaxadjustment.by.allDetails.withOrder")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("taxAgency", taxAgency.getID());
			} else {
				query = session.getNamedQuery(

				"getTaxadjustment.by.betweenDates")
						.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate);
			}

			// Adding adjustment entries to it's Respective boxes, except
			// Uncategorised Tax Amounts box
			List<TAXAdjustment> vas = query.list();

			// double box2Amount = 0.0;

			for (TAXAdjustment v : vas) {

				VATDetail vd = new VATDetail();

				vd.setBoxName(setVATBoxName(v.getTaxItem().getVatReturnBox()
						.getVatBox()));

				if (!vd.getBoxName().equals(
						AccounterServerConstants.UK_BOX10_UNCATEGORISED)) {

					Entry e = v.getJournalEntry().getEntry().get(0);
					if (!DecimalUtil.isEquals(e.getDebit(), 0)) {
						vd.setTotal(e.getDebit());
					} else {
						vd.setTotal(-1 * e.getCredit());
					}

					if (vd.getBoxName().equals("VAT Due on sales (Box 1)"))
						vd.setTotal(-1 * vd.getTotal());

					vd.setPayeeName(e.getAccount().getName());

					vd.setTransactionDate(new ClientFinanceDate(e
							.getEntryDate().getDate()));
					vd.setTransactionName(e.getJournalEntry().toString());
					vd.setTransactionNumber(e.getJournalEntry().getNumber());
					vd.setTransactionType(e.getJournalEntryType());
					vd.setTransactionId(e.getJournalEntry().getID());

					vatDetailReport.getEntries().get(vd.getBoxName()).add(vd);
				}

				// Adding Box2 adjustment entries to Box4
				// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
				// AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
				// VATDetail vd2 = new VATDetail();
				// vd2
				// .setBoxName(setVATBoxName(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));
				//
				// vd2.setTotal(vd.getTotal());
				// vd2.setPayeeName(vd.getPayeeName());
				// vd2.setTransactionDate(vd.getTransactionDate());
				// vd2.setTransactionName(vd.getTransactionName());
				// vd2.setTransactionNumber(vd.getTransactionNumber());
				// vd2.setTransactionType(vd.getTransactionType());
				// vd2.setTransactionId(vd.getTransactionId());
				//
				// vatDetailReport.getEntries().get(vd2.getBoxName()).add(vd2);
				// }

				// Adding all adjustment entries to Uncategorised Tax Amounts
				// Box
				VATDetail vd1 = new VATDetail();

				vd1.setBoxName(AccounterServerConstants.UK_BOX10_UNCATEGORISED);

				Entry e = v.getJournalEntry().getEntry().get(0);
				if (!DecimalUtil.isEquals(e.getDebit(), 0)) {
					vd1.setTotal(1 * e.getDebit());
				} else {
					vd1.setTotal(-1 * e.getCredit());
				}

				if (v.getTaxItem().getName().equals("EC Sales Goods Standard")
						|| v.getTaxItem().getName()
								.equals("EC Sales Services Standard"))
					vd1.setTotal(-1 * vd1.getTotal());

				vd1.setPayeeName(e.getAccount().getName());

				vd1.setTransactionDate(new ClientFinanceDate(e.getEntryDate()
						.getDate()));
				vd1.setTransactionName(e.getJournalEntry().toString());
				vd1.setTransactionNumber(e.getVoucherNumber());
				vd1.setTransactionType(e.getJournalEntryType());
				vd1.setTransactionId(e.getJournalEntry().getID());

				if (vatDetailReport.getEntries().get(vd1.getBoxName()) != null)
					vatDetailReport.getEntries().get(vd1.getBoxName()).add(vd1);
			}
		}

		// Getting journal entries from VATReturn
		{
			if (taxAgency != null) {
				query = session.getNamedQuery(

				"getVat.by.taxAgency.and.VatPeriod")

				.setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate)
						.setParameter("taxAgency", taxAgency.getID());
			} else {
				query = session.getNamedQuery(

				"getVat.by.BetweenendDates").setEntity("company", company)
						.setParameter("fromDate", startDate)
						.setParameter("toDate", endDate);
			}

			List<VATReturn> vatReturns = query.list();
			for (VATReturn v : vatReturns) {

				List<Entry> entries = v.getJournalEntry().getEntry();
				for (Entry e : entries) {
					if ((!e.getAccount()
							.getName()
							.equals(company.getAccountsPayableAccount()
									.getName()))) {
						// && ((e.getDebit() == 0 && e.getCredit() == 0))) {

						if (e.getTaxItem() != null) {
							VATDetail vd = new VATDetail();

							vd.setBoxName(setVATBoxName(e.getTaxItem()
									.getVatReturnBox().getVatBox()));
							// vd.setBoxName("Uncategorised Tax Amounts");

							if (!DecimalUtil.isEquals(e.getDebit(), 0)) {
								vd.setTotal(-1 * e.getDebit());
							} else {
								vd.setTotal(-1 * e.getCredit());
							}

							vd.setTotal(-1 * e.getTotal());
							vd.setPayeeName(e.getAccount().getName());
							vd.setTransactionDate(new ClientFinanceDate(e
									.getEntryDate().getDate()));
							vd.setTransactionName(e.getJournalEntry()
									.toString());
							vd.setTransactionNumber(e.getVoucherNumber());
							vd.setTransactionType(e.getJournalEntryType());
							vd.setTransactionId(e.getJournalEntry().getID());

							// if (vatDetailReport.getEntries().get(
							// vd.getBoxName()) != null)
							// vatDetailReport.getEntries().get(
							// vd.getBoxName()).add(vd);

						} else {
							VATDetail vd = new VATDetail();

							vd.setBoxName(VATDetailReport.IRELAND_BOX10_UNCATEGORISED);

							if (!DecimalUtil.isEquals(e.getDebit(), 0)) {
								vd.setTotal(-1 * e.getDebit());
							} else {
								vd.setTotal(-1 * e.getCredit());
							}

							vd.setTransactionDate(new ClientFinanceDate(e
									.getEntryDate().getDate()));
							vd.setTransactionName(e.getJournalEntry()
									.toString());
							vd.setTransactionNumber(e.getVoucherNumber());
							vd.setTransactionType(e.getJournalEntryType());
							// vatDetailReport.getEntries().get(vd.getBoxName())
							// .add(vd);
						}
					}

				}

				// Adding Filed vat entries to it's Respective boxes, except
				// Box3 and Box5
				Query query1 = session.getNamedQuery("getFiledBoxValues")
						.setParameter("id", v.getID())
						.setParameter("companyId", company.getID());

				List list = query1.list();
				Object[] object = null;
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {

					object = (Object[]) iterator.next();

					VATDetail vd = new VATDetail();

					vd.setBoxName(setVATBoxName((String) object[0]));

					// if (((String) object[0])
					// .equals("Box 2 VAT due in this period on acquisitions from other EC member states")
					// && vd.getBoxName().equals(
					// VATSummary.UK_BOX10_UNCATEGORISED))
					// vd
					// .setBoxName(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);

					if ((Double) object[1] != 0.0
							&& (!vd.getBoxName().equals(
									VATSummary.UK_BOX3_TOTAL_OUTPUT) && !vd
									.getBoxName().equals(
											VATSummary.UK_BOX5_NET_VAT))) {

						vd.setTotal(-1 * (Double) object[1]);
						vd.setTransactionDate(new ClientFinanceDate(
								(Long) object[2]));
						vd.setTransactionName(v.getJournalEntry().toString());
						vd.setTransactionNumber((String) object[3]);
						vd.setTransactionType(v.getJournalEntry().getEntry()
								.get(0).getJournalEntryType());

						if (vatDetailReport.getEntries().get(vd.getBoxName()) != null)
							vatDetailReport.getEntries().get(vd.getBoxName())
									.add(vd);
					}
				}
			}
		}
	}

	private String setVATBoxName(String getBoxName) {
		String boxName = null;
		if (getBoxName
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (getBoxName.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (getBoxName
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	private String setTotalBoxName(TAXRateCalculation v) {
		String boxName = null;
		if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getTotalBox()
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getTotalBox()
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, FinanceDate endDate, long companyId)
			throws DAOException, ParseException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate;
		{
			Query q1 = session.getNamedQuery("getVATReturn.by.enddate")
					.setParameter("endDate", endDate)
					.setEntity("company", company);

			VATReturn vatReturn = (VATReturn) q1.uniqueResult();
			if (vatReturn == null) {
				throw new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						new NullPointerException(
								"No VAT Return found in database with VATAgency '"
										+ vatAgency.getName()
										+ "' and End date :"
										+ endDate.toString()));
			}

			startDate = vatReturn.getVATperiodStartDate();

		}

		VATDetailReport vatDetailReport = new VATDetailReport(
				vatAgency.getVATReturn());
		prepareVATDetailReport(vatDetailReport, vatAgency, startDate, endDate,
				company);

		return getListOfVATDetails(vatDetailReport);
	}

	public ArrayList<VATDetail> getVATDetailReport(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException,
			ParseException {

		VATDetailReport vatDetailReport = new VATDetailReport();
		Company company = getCompany(companyId);
		prepareVATDetailReport(vatDetailReport, null, startDate, endDate,
				company);

		return getListOfVATDetails(vatDetailReport);
	}

	private ArrayList<VATDetail> getListOfVATDetails(
			VATDetailReport vatDetailReport) {

		List<VATDetail> vatDetails = new ArrayList<VATDetail>();

		LinkedHashMap<String, List<VATDetail>> map = vatDetailReport
				.getEntries();

		if (map.containsKey(VATSummary.UK_BOX10_UNCATEGORISED)
				&& map.get(VATSummary.UK_BOX10_UNCATEGORISED).size() > 0)
			vatDetails.addAll((map.get(VATSummary.UK_BOX10_UNCATEGORISED)));

		// if (map.containsKey(VATDetailReport.IRELAND_BOX10_UNCATEGORISED)
		// && map.get(VATDetailReport.IRELAND_BOX10_UNCATEGORISED).size() > 0)
		// vatDetails.addAll(map
		// .get(VATDetailReport.IRELAND_BOX10_UNCATEGORISED));

		if (map.containsKey(VATSummary.UK_BOX1_VAT_DUE_ON_SALES)
				&& map.get(VATSummary.UK_BOX1_VAT_DUE_ON_SALES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX1_VAT_DUE_ON_SALES));

		if (map.containsKey(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
				&& map.get(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS));

		if (map.containsKey(VATSummary.UK_BOX3_TOTAL_OUTPUT)
				&& map.get(VATSummary.UK_BOX3_TOTAL_OUTPUT).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX3_TOTAL_OUTPUT));

		if (map.containsKey(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)
				&& map.get(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES));

		if (map.containsKey(VATSummary.UK_BOX5_NET_VAT)
				&& map.get(VATSummary.UK_BOX5_NET_VAT).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX5_NET_VAT));

		if (map.containsKey(VATSummary.UK_BOX6_TOTAL_NET_SALES)
				&& map.get(VATSummary.UK_BOX6_TOTAL_NET_SALES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX6_TOTAL_NET_SALES));

		if (map.containsKey(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES)
				&& map.get(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX7_TOTAL_NET_PURCHASES));

		if (map.containsKey(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES)
				&& map.get(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES).size() > 0)
			vatDetails.addAll(map.get(VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES));

		if (map.containsKey(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS)
				&& map.get(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS).size() > 0)
			vatDetails.addAll(map
					.get(VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS));

		if (map.containsKey(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES)
				&& map.get(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS)
				&& map.get(
						VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS)
						.size() > 0)
			vatDetails
					.addAll(map
							.get(VATDetailReport.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS));

		if (map.containsKey(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES)
				&& map.get(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX3_VAT_ON_SALES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES)
				&& map.get(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX4_VAT_ON_PURCHASES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE)
				&& map.get(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX5_T3_T4_PAYMENT_DUE));

		if (map.containsKey(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU)
				&& map.get(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX6_E1_GOODS_TO_EU));

		if (map.containsKey(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU)
				&& map.get(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX7_E2_GOODS_FROM_EU));

		if (map.containsKey(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES)
				&& map.get(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES).size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX8_TOTAL_NET_SALES));

		if (map.containsKey(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES)
				&& map.get(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES)
						.size() > 0)
			vatDetails.addAll(map
					.get(VATDetailReport.IRELAND_BOX9_TOTAL_NET_PURCHASES));

		return new ArrayList<VATDetail>(vatDetails);

	}

	private String setVATBoxName(TAXRateCalculation v) {
		String boxName = null;
		if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES)) {
			boxName = VATSummary.UK_BOX1_VAT_DUE_ON_SALES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT)) {
			boxName = VATSummary.UK_BOX3_TOTAL_OUTPUT;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
			boxName = VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX5_NET_VAT)) {
			boxName = VATSummary.UK_BOX5_NET_VAT;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES)) {
			boxName = VATSummary.UK_BOX6_TOTAL_NET_SALES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES)) {
			boxName = VATSummary.UK_BOX7_TOTAL_NET_PURCHASES;
		} else if (v.getTaxItem().getVatReturnBox().getVatBox()
				.equals(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES)) {
			boxName = VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES;
		} else if (v
				.getTaxItem()
				.getVatReturnBox()
				.getVatBox()
				.equals(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS)) {
			boxName = VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS;
		} else {
			boxName = VATSummary.UK_BOX10_UNCATEGORISED;
		}

		return boxName;
	}

	public ArrayList<CheckDetailReport> getCheckDetailReport(
			long paymentmethod, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getCheckDetailReport")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("paymentmethod", paymentmethod);
		List list = query.list();
		Object[] object = null;
		Iterator iterator = list.iterator();
		List<CheckDetailReport> queryResult = new ArrayList<CheckDetailReport>();
		while ((iterator).hasNext()) {

			CheckDetailReport checkDetail = new CheckDetailReport();
			object = (Object[]) iterator.next();

			checkDetail.setTransactionId((Long) object[0]);
			checkDetail.setTransactionType(((Integer) object[1]));
			checkDetail.setNumber((String) object[2]);
			checkDetail.setTransactionDate(new ClientFinanceDate(
					((Long) object[3])));
			checkDetail.setPaymentMethod((String) object[4]);
			checkDetail.setPayeeName((String) object[5]);
			checkDetail.setAccountName((String) object[6]);
			checkDetail.setAmount((Double) object[7]);
			checkDetail.setMemo((String) object[8]);

			queryResult.add(checkDetail);

		}

		return new ArrayList<CheckDetailReport>(queryResult);
	}

	public ArrayList<ExpenseList> getExpenseReportByType(int type,
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		List list = null;
		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getExpenseReportByType")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("type", type);
		list = query.list();

		Object[] object = null;
		Iterator iterator = list.iterator();
		List<ExpenseList> queryResult = new ArrayList<ExpenseList>();
		while ((iterator).hasNext()) {

			ExpenseList expense = new ExpenseList();
			object = (Object[]) iterator.next();

			expense.setTransactionId(object[0] != null ? (Long) object[0]
					: null);
			expense.setTransactionType(object[1] != null ? (Integer) object[1]
					: null);
			expense.setTransactionDate(object[2] != null ? new ClientFinanceDate(
					((Long) object[2])) : null);
			expense.setTransactionNumber(object[3] != null ? (String) object[3]
					: null);
			expense.setMemo(object[4] != null ? (String) object[4] : null);
			expense.setName(object[5] != null ? (String) object[5] : null);
			expense.setTotal(object[6] != null ? (Double) object[6] : null);
			queryResult.add(expense);

		}

		return new ArrayList<ExpenseList>(queryResult);
	}

	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {
		// /////
		// ////////
		// /////////
		// //////
		// //////

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.datesand.orderby.transactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		Map<String, Double> maps = new LinkedHashMap<String, Double>();

		long transactionItemId = 0;

		for (TAXRateCalculation v : taxRateCalculations) {

			if (v.getTransactionItem().getTransaction()
					.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				String s = v.getTransactionItem().getTransaction()
						.getInvolvedPayee() != null ? v.getTransactionItem()
						.getTransaction().getInvolvedPayee().getName() : null;

				if ((transactionItemId == 0)
						|| (transactionItemId != v.getTransactionItem().getID())) {

					if (maps.containsKey(s)) {
						maps.put(
								s,
								maps.get(s) + v.getVatAmount()
										+ v.getLineTotal());
						if (v.getTransactionItem().isVoid()) {
							maps.put(
									s,
									maps.get(s)
											- (v.getVatAmount() + v
													.getLineTotal()));
						}
					} else {
						maps.put(s, v.getVatAmount() + v.getLineTotal());
						if (v.getTransactionItem().isVoid()) {
							maps.put(
									s,
									maps.get(s)
											- (v.getVatAmount() + v
													.getLineTotal()));
						}
					}

					transactionItemId = v.getTransactionItem().getID();
				}
			}
		}

		String[] names = new String[maps.keySet().size()];

		maps.keySet().toArray(names);

		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

		List<ReverseChargeList> reverseCharges = new ArrayList<ReverseChargeList>();

		for (String s : names) {

			ReverseChargeList r = new ReverseChargeList();
			r.setAmount(maps.get(s));
			r.setName(s);

			reverseCharges.add(r);
		}

		return new ArrayList<ReverseChargeList>(reverseCharges);
	}

	public ArrayList<VATSummary> getVAT100Report(TAXAgency taxAgency,
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {
		Session session = HibernateUtil.getCurrentSession();

		List<VATSummary> vatSummaries = createRows(taxAgency);
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("getVATReturn.checkingby.taxagencyidand.dates")
				.setLong("id", taxAgency.getID()).setEntity("company", company);

		Object object[] = null;
		List list = query.list();
		Iterator it = list.iterator();
		FinanceDate leastStartDate = null;
		FinanceDate highestEndDate = null;

		while (it.hasNext()) {
			object = (Object[]) it.next();
			leastStartDate = (((FinanceDate) object[0]) == null ? null
					: ((FinanceDate) object[0]));
			highestEndDate = (((FinanceDate) object[1]) == null ? null
					: ((FinanceDate) object[1]));

		}

		if (leastStartDate != null && highestEndDate != null) {

			query = session
					.getNamedQuery(
							"getTAXRateCalculation.by.datesand.vatReturn")
					.setParameter("startDate", fromDate)
					.setParameter("endDate", toDate)
					.setEntity("company", company);
			// .setParameter("startDate1",
			// leastStartDate).setParameter("endDate1",
			// highestEndDate);
			// v.transactionDate not between :startDate1 and :endDate1")
		} else {
			query = session.getNamedQuery("getTAXRateCalculation.by.dates")
					.setParameter("startDate", fromDate)
					.setParameter("endDate", toDate)
					.setEntity("company", company);
		}

		List<TAXRateCalculation> vats = query.list();

		// double rcAmount = 0;

		for (TAXRateCalculation v : vats) {

			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES)
			// && (v.getVatItem().getVatReturnBox().getTotalBox()
			// .equals(AccounterConstants.BOX_NONE))) {
			// rcAmount += -1 * (v.getVatAmount());
			// }

			// if (v.getVatItem().getVatReturnBox().getVatBox().equals(
			// AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
			// rcAmount += -1 * (v.getVatAmount());

			/*
			 * Here all box values other than Vat code EGS (for purchase) of
			 * box2 and Vat code RC (for purchase) of box1 are getting it's
			 * native values
			 */

			for (VATSummary vs : vatSummaries) {

				if (v.getTaxItem().getVatReturnBox().getVatBox()
						.equals(vs.getVatReturnEntryName())) {

					if (v.getTaxItem()
							.getVatReturnBox()
							.getVatBox()
							.equals(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS)
							|| (v.getTaxItem()
									.getVatReturnBox()
									.getVatBox()
									.equals(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES) && !v
									.isVATGroupEntry())
							|| (v.getTaxItem()
									.getVatReturnBox()
									.getVatBox()
									.equals(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES) && v
									.getTaxItem().getVatReturnBox()
									.getTotalBox()
									.equals(AccounterServerConstants.BOX_NONE)))
						vs.setValue(vs.getValue() + (-1 * (v.getVatAmount())));
					else
						vs.setValue(vs.getValue() + v.getVatAmount());
					// if (v
					// .getVatItem()
					// .getVatReturnBox()
					// .getVatBox()
					// .equals(
					// AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
					// vs.setValue(vs.getValue() + rcAmount);
					// rcAmount = 0.0;
					// }
				}

				if (v.getTaxItem().getVatReturnBox().getTotalBox()
						.equals(vs.getVatReturnEntryName())) {

					vs.setValue(vs.getValue() + v.getLineTotal());

				}
			}
		}

		query = session
				.getNamedQuery("getTAXAdjustment.by.taxAgencyidanddates")
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("taxAgency", taxAgency.getID())
				.setEntity("company", company);

		List<TAXAdjustment> vas = query.list();
		// double box2Amount = 0.0;

		if (vas != null) {
			for (TAXAdjustment v : vas) {
				for (VATSummary vs : vatSummaries) {

					// if (vs
					// .getVatReturnEntryName()
					// .equals(
					// AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES)) {
					// vs.setValue(vs.getValue() + box2Amount);
					// box2Amount = 0.0;
					// }

					if (v.getTaxItem().getVatReturnBox().getVatBox()
							.equals(vs.getVatReturnEntryName())) {
						if (v.getIncreaseVATLine())
							vs.setValue(vs.getValue() + v.getTotal());
						else
							vs.setValue(vs.getValue() - v.getTotal());

						// if (v
						// .getVatItem()
						// .getVatReturnBox()
						// .getVatBox()
						// .equals(
						// AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
						// box2Amount += (v.getIncreaseVATLine() == true ? v
						// .getTotal() : -1 * v.getTotal());
					}

					else if (vs.getVatReturnEntryName().equals(
							"Uncategorised Tax Amounts")) {

						if (v.getTaxItem().isSalesType()) {
							if ((v.getTaxItem().getID() != 3)
									&& (v.getTaxItem().getID() != 4)) {
								if (v.getIncreaseVATLine())
									vs.setValue(vs.getValue() - v.getTotal());
								else
									vs.setValue(vs.getValue() + v.getTotal());

							} else {

								if (v.getIncreaseVATLine())
									vs.setValue(vs.getValue() + v.getTotal());
								else
									vs.setValue(vs.getValue() - v.getTotal());
							}
						}

						else {
							if (v.getIncreaseVATLine())
								vs.setValue(vs.getValue() + v.getTotal());
							else
								vs.setValue(vs.getValue() - v.getTotal());
						}
					}
				}
			}

		}

		// for (VATSummary v : vatSummaries) {
		// if (v.getVatReturnEntryName().equals(
		// AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS))
		// v.setVatReturnEntryName("Box 2 "
		// + AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
		// }

		return new ArrayList<VATSummary>(vatSummaries);
	}

	private List<VATSummary> createRows(TAXAgency taxAgency) {

		List<VATSummary> vatSummaries = new ArrayList<VATSummary>();

		if (taxAgency.getVATReturn() == TAXAgency.RETURN_TYPE_UK_VAT) {
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES,
					VATSummary.UK_BOX1_VAT_DUE_ON_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS,
					VATSummary.UK_BOX2_VAT_DUE_ON_ACQUISITIONS, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX3_TOTAL_OUTPUT,
					VATSummary.UK_BOX3_TOTAL_OUTPUT, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES,
					VATSummary.UK_BOX4_VAT_RECLAMED_ON_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX5_NET_VAT,
					VATSummary.UK_BOX5_NET_VAT, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES,
					VATSummary.UK_BOX6_TOTAL_NET_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES,
					VATSummary.UK_BOX7_TOTAL_NET_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES,
					VATSummary.UK_BOX8_TOTAL_NET_SUPPLIES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS,
					VATSummary.UK_BOX9_TOTAL_NET_ACQUISITIONS, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.UK_BOX10_UNCATEGORISED,
					VATSummary.UK_BOX10_UNCATEGORISED, 0d));
		} else if (taxAgency.getVATReturn() == TAXAgency.RETURN_TYPE_IRELAND_VAT) {
			vatSummaries
					.add(new VATSummary(
							AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES,
							VATSummary.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES, 0d));
			vatSummaries
					.add(new VATSummary(
							AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
							VATSummary.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS,
							0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX3_VAT_ON_SALES,
					VATSummary.IRELAND_BOX3_VAT_ON_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES,
					VATSummary.IRELAND_BOX4_VAT_ON_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX5_T3_T4_PAYMENT_DUE,
					VATSummary.IRELAND_BOX5_T3_T4_PAYMENT_DUE, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU,
					VATSummary.IRELAND_BOX6_E1_GOODS_TO_EU, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU,
					VATSummary.IRELAND_BOX7_E2_GOODS_FROM_EU, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES,
					VATSummary.IRELAND_BOX8_TOTAL_NET_SALES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES,
					VATSummary.IRELAND_BOX9_TOTAL_NET_PURCHASES, 0d));
			vatSummaries.add(new VATSummary(
					AccounterServerConstants.IRELAND_BOX10_UNCATEGORISED,
					VATSummary.IRELAND_BOX10_UNCATEGORISED, 0d));
		}
		return vatSummaries;
	}

	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws ParseException {
		List<UncategorisedAmountsReport> uncategorisedAmounts = new ArrayList<UncategorisedAmountsReport>();

		Session session = HibernateUtil.getCurrentSession();

		// Entries from Sales where Vat Codes EGS and RC are used
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getEGSandRCentriesFromSales")
				.setParameter("startDate", fromDate.getDate())
				.setParameter("endDate", toDate.getDate())
				.setLong("companyId", companyId);

		List list2 = query.list();
		Object[] object = null;
		Iterator iterator = list2.iterator();
		while (iterator.hasNext()) {

			object = (Object[]) iterator.next();
			UncategorisedAmountsReport u = new UncategorisedAmountsReport();
			u.setTransactionType((Integer) object[0]);
			u.setDate(new ClientFinanceDate((Long) object[1]));
			u.setTransactionNumber((String) object[2]);
			u.setSourceName((String) object[4]);
			u.setMemo((String) object[5]);
			u.setAmount(object[6] == null ? 0 : (Double) object[6]);

			uncategorisedAmounts.add(u);
		}

		query = session
				.getNamedQuery("getTAXAdjustment.checkingby.transactionDate")
				.setParameter("endDate", toDate)
				.setEntity("company", getCompany(companyId));

		List<TAXAdjustment> vatAdjustments = query.list();

		for (TAXAdjustment v : vatAdjustments) {

			if (v.getJournalEntry().getEntry() != null
					&& v.getJournalEntry().getEntry().size() > 0
					&& !DecimalUtil.isEquals(v.getJournalEntry().getEntry()
							.get(0).getTotal(), 0)) {

				UncategorisedAmountsReport u = new UncategorisedAmountsReport();

				u.setTransactionType(v.getJournalEntry().getType());
				u.setTransactionNumber(v.getJournalEntry().getNumber());
				u.setID(v.getJournalEntry().getID());
				u.setSourceName(v.getJournalEntry().getEntry().get(1)
						.getAccount().getName());
				u.setMemo("VAT Adjustment");
				u.setDate(new ClientFinanceDate(v.getJournalEntry().getDate()
						.getDate()));
				if (v.getTaxItem().isSalesType()) {
					if (v.getIncreaseVATLine()) {

						u.setAmount(-1
								* (v.getJournalEntry().getEntry().get(0)
										.getTotal()));
					} else {
						u.setAmount((v.getJournalEntry().getEntry().get(0)
								.getTotal()));
					}

					if ((v.getTaxItem().getName()
							.equals("EC Sales Services Standard"))
							|| (v.getTaxItem().getName()
									.equals("EC Sales Goods Standard"))) {

						u.setAmount(-1 * u.getAmount());
					}

				} else {
					if (v.getIncreaseVATLine()) {

						u.setAmount((v.getJournalEntry().getEntry().get(0)
								.getTotal()));
					} else {
						u.setAmount(-1
								* (v.getJournalEntry().getEntry().get(0)
										.getTotal()));
					}
				}

				uncategorisedAmounts.add(u);
			}
		}

		// Entries from VATReturn;

		query = session
				.getNamedQuery("getTAXAdjustment.checkingby.VATperiodEndDate")
				.setParameter("endDate", toDate).setEntity("company", company);

		List<VATReturn> vatReturns = query.list();
		for (VATReturn v : vatReturns) {

			double amount = v.getBoxes().get(v.getBoxes().size() - 1)
					.getAmount();

			if (!DecimalUtil.isEquals(amount, 0)) {

				UncategorisedAmountsReport u = new UncategorisedAmountsReport();

				u.setTransactionType(v.getJournalEntry().getType());
				u.setTransactionNumber(v.getJournalEntry().getNumber());
				u.setID(v.getJournalEntry().getID());
				u.setSourceName(v.getJournalEntry().getEntry().get(0)
						.getAccount().getName());
				u.setMemo("Filed Uncategorised amounts");
				u.setDate(new ClientFinanceDate(v.getJournalEntry().getDate()
						.getDate()));

				// if (amount > 0)
				u.setAmount(-1 * (amount));
				// else
				// u.setAmount(amount);

				uncategorisedAmounts.add(u);
			}
		}

		return new ArrayList<UncategorisedAmountsReport>(uncategorisedAmounts);
	}

	public ArrayList<VATItemDetail> getVATItemDetailReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery("getTAXAdjustment.by.taxAgencyidanddates")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();
		for (TAXRateCalculation v : taxRateCalculations) {

			VATItemDetail vi = new VATItemDetail();
			vi.setAmount(v.getTransactionItem().getLineTotal());
			vi.setDate(new ClientFinanceDate(v.getTransactionDate().getDate()));
			vi.setName(v.getTransactionItem().getTransaction()
					.getInvolvedPayee().getName());
			vi.setTransactionId(v.getTransactionItem().getTransaction().getID());
			vi.setMemo(v.getTransactionItem().getTransaction().getMemo());
			vi.setTransactionNumber(v.getTransactionItem().getTransaction()
					.getNumber());
			vi.setTransactionType(v.getTransactionItem().getTransaction()
					.getType());
			vi.setSalesPrice(vi.getAmount());

			vatItemDetails.add(vi);

		}

		// Entries from the VATAdjustment
		query = session
				.getNamedQuery(
						"getTAXAdjustment.by.dates.orderby.taxItemNameand.TransactionDate")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXAdjustment> vatAdjustments = query.list();
		for (TAXAdjustment v : vatAdjustments) {

			VATItemDetail vi = new VATItemDetail();

			if (v.getIncreaseVATLine()) {
				vi.setAmount(v.getJournalEntry().getTotal());
			} else {
				vi.setAmount(-1 * v.getJournalEntry().getTotal());
			}
			vi.setDate(new ClientFinanceDate(v.getJournalEntry().getDate()
					.getDate()));
			vi.setMemo("VAT Adjustment");
			vi.setName(v.getTaxItem().getTaxAgency().getName());
			vi.setTransactionId(v.getJournalEntry().getID());
			vi.setTransactionNumber(v.getJournalEntry().getNumber());
			vi.setTransactionType(v.getJournalEntry().getType());
			vatItemDetails.add(vi);
		}

		return new ArrayList<VATItemDetail>(vatItemDetails);
	}

	public ArrayList<VATItemDetail> getVATItemDetailReport(String taxItemName,
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		Company company = getCompany(companyId);
		long transactionItemId = 0, vatItemId = 0;

		// Entries from the VATRate calculation

		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.dates.groupedByIdtransactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate)
				.setParameter("taxItemName", taxItemName)
				.setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();
		for (TAXRateCalculation v : taxRateCalculations) {

			if ((v.getTaxItem().getID() != 4 && v.getTaxItem().getID() != 12 && v
					.getTaxItem().getID() != 14)
					|| (v.getTaxItem().getID() == 4 && v.isVATGroupEntry() == false)
					|| (v.getTaxItem().getID() == 12 && v.isVATGroupEntry() == false)
					|| (v.getTaxItem().getID() == 14 && v.isVATGroupEntry() == false)) {

				if (transactionItemId == 0
						|| transactionItemId != v.getTransactionItem().getID()) {

					VATItemDetail vi = new VATItemDetail();
					double amount = (!v.getTransactionItem().isVoid()) ? v
							.getLineTotal() : 0;
					vi.setAmount(amount);
					vi.setDate(new ClientFinanceDate(v.getTransactionDate()
							.getDate()));

					if (v.getTransactionItem().getTransaction()
							.getInvolvedPayee() != null)
						vi.setName(v.getTransactionItem().getTransaction()
								.getInvolvedPayee().getName());

					else {

						CashPurchase cashPurchase = (CashPurchase) (v
								.getTransactionItem().getTransaction());
						if (cashPurchase.getCashExpenseAccount() != null)
							vi.setName(cashPurchase.getCashExpenseAccount()
									.getName());
						else if (cashPurchase.getEmployee() != null)
							vi.setName(cashPurchase.getEmployee().getName());
					}

					vi.setTransactionId(v.getTransactionItem().getTransaction()
							.getID());
					vi.setMemo(v.getTransactionItem().getTransaction()
							.getMemo());
					vi.setTransactionNumber(v.getTransactionItem()
							.getTransaction().getNumber());
					vi.setTransactionType(v.getTransactionItem()
							.getTransaction().getType());
					vi.setSalesPrice(v.getTransactionItem().getLineTotal());

					transactionItemId = v.getTransactionItem().getID();
					vatItemId = v.getTaxItem().getID();

					vatItemDetails.add(vi);
				}

				// if (transactionItemId == v.getTransactionItem().getID() &&
				// v.getVatItem().getID() == 4) {
				// vi.
				// }

			}

		}

		// Entries from the VATAdjustment
		query = session
				.getNamedQuery("getTAXAdjustment.by.dates.and.taxItemName")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate)
				.setParameter("taxItemName", taxItemName)
				.setEntity("company", company);

		List<TAXAdjustment> vatAdjustments = query.list();
		for (TAXAdjustment v : vatAdjustments) {

			VATItemDetail vi = new VATItemDetail();

			if (v.getIncreaseVATLine()) {
				vi.setAmount(v.getJournalEntry().getTotal());
			} else {
				vi.setAmount(-1 * v.getJournalEntry().getTotal());
			}
			vi.setDate(new ClientFinanceDate(v.getJournalEntry().getDate()
					.getDate()));
			vi.setMemo("VAT Adjustment");
			vi.setName(v.getTaxItem().getTaxAgency().getName());
			vi.setTransactionId(v.getJournalEntry().getID());
			vi.setTransactionNumber(v.getJournalEntry().getNumber());
			vi.setTransactionType(v.getJournalEntry().getType());
			// vatItemDetails.add(vi);
		}

		return new ArrayList<VATItemDetail>(vatItemDetails);
	}

	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException, ParseException {

		List<VATItemSummary> vatItemSummaries = new ArrayList<VATItemSummary>();

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.dates.orderbytaxItem.name")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("comapny", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		String tempVATItemName = null;
		long transactionItemId = 0;
		VATItemSummary vi = null;

		for (TAXRateCalculation v : taxRateCalculations) {

			if (tempVATItemName == null
					|| (!tempVATItemName.equals(v.getTaxItem().getName()))) {
				// if (transactionItemId == 0
				// || (transactionItemId != v.getTransactionItem().getID())) {

				if (vi != null) {
					vatItemSummaries.add(vi);
				}

				tempVATItemName = v.getTaxItem().getName();
				// transactionItemId = v.getTransactionItem().getID();

				vi = new VATItemSummary();
				vi.setName(tempVATItemName);
				// vi.setAmount(v.getVatAmount());
				vi.setAmount(v.getLineTotal());
				if (v.getTransactionItem() != null
						&& v.getTransactionItem().isVoid()) {
					vi.setAmount(vi.getAmount() - v.getLineTotal());
				}
			} else {

				vi.setAmount(vi.getAmount() + v.getLineTotal());
				if (v.getTransactionItem() != null
						&& v.getTransactionItem().isVoid()) {
					vi.setAmount(vi.getAmount() - v.getLineTotal());
				}
			}

		}
		if (vi != null)
			vatItemSummaries.add(vi);

		return new ArrayList<VATItemSummary>(vatItemSummaries);
	}

	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate,
			Company company) throws DAOException, ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery(
						"getTAXRateCalculation.by.check.taxItemandDates.orderBy.transactionItem")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		List<ECSalesListDetail> details = new ArrayList<ECSalesListDetail>();

		Map<String, List<ECSalesListDetail>> customerWiseDetail = new LinkedHashMap<String, List<ECSalesListDetail>>();

		boolean isAlreadyPut = false;
		long transactionItemId = 0;
		for (TAXRateCalculation v : taxRateCalculations) {
			if (v.getTransactionItem().getTransaction()
					.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				if (v.getTransactionItem().getTransaction().getInvolvedPayee() != null
						&& (v.getTransactionItem().getTransaction()
								.getInvolvedPayee().getName())
								.equals(payeeName)) {

					if ((transactionItemId == 0)
							|| (transactionItemId != v.getTransactionItem()
									.getID())) {

						ECSalesListDetail e = new ECSalesListDetail();
						double amount = (!v.getTransactionItem().isVoid()) ? v
								.getLineTotal() : 0;
						e.setAmount(amount);
						e.setDate(new ClientFinanceDate(v.getTransactionDate()
								.getDate()));
						e.setMemo(v.getTransactionItem().getTransaction()
								.getMemo());
						e.setName(v.getTransactionItem().getTransaction()
								.getInvolvedPayee() != null ? v
								.getTransactionItem().getTransaction()
								.getInvolvedPayee().getName() : null);
						e.setSalesPrice(v.getTransactionItem().getLineTotal());
						e.setTransactionId(v.getTransactionItem()
								.getTransaction().getID());
						e.setTransactionNumber(v.getTransactionItem()
								.getTransaction().getNumber());
						e.setTransactionid(v.getTransactionItem()
								.getTransaction().getID());
						e.setTransactionType(v.getTransactionItem()
								.getTransaction().getType());

						if (customerWiseDetail.containsKey(e.getName())) {
							customerWiseDetail.get(e.getName()).add(e);
						} else {
							List<ECSalesListDetail> list = new ArrayList<ECSalesListDetail>();
							list.add(e);
							customerWiseDetail.put(e.getName(), list);
						}

						transactionItemId = v.getTransactionItem().getID();

						// if (v.getTransactionItem().isVoid()) {
						//
						// ECSalesListDetail e2 = new ECSalesListDetail();
						//
						// e2.setAmount(-1 * (v.getLineTotal()));
						// e2.setDate(new
						// ClientFinanceDate(v.getTransactionDate()
						// .getTime()));
						// e2.setMemo(v.getTransactionItem().getTransaction()
						// .getMemo());
						// e2.setName(v.getTransactionItem().getTransaction()
						// .getInvolvedPayee() != null ? v
						// .getTransactionItem().getTransaction()
						// .getInvolvedPayee().getName() : null);
						// e2.setSalesPrice(v.getTransactionItem().getLineTotal());
						// e2.setTransactionId(v.getTransactionItem()
						// .getTransaction().getID());
						// e2.setTransactionNumber(v.getTransactionItem()
						// .getTransaction().getNumber());
						// e2.setTransactionid(v.getTransactionItem()
						// .getTransaction().getID());
						// e2.setTransactionType(v.getTransactionItem()
						// .getTransaction().getType());
						//
						// customerWiseDetail.get(e2.getName()).add(e2);
						// }
					}
				}
			}

		}

		String arr[] = new String[customerWiseDetail.keySet().size()];
		customerWiseDetail.keySet().toArray(arr);
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		for (String a : arr) {
			details.addAll(customerWiseDetail.get(a));
		}

		return new ArrayList<ECSalesListDetail>(details);

	}

	public ArrayList<ECSalesList> getECSalesListReport(FinanceDate fromDate,
			FinanceDate toDate, Company company) throws DAOException,
			ParseException {

		List<VATItemDetail> vatItemDetails = new ArrayList<VATItemDetail>();

		Session session = HibernateUtil.getCurrentSession();

		// Getting entries from VATRateCalculation
		Query query = session
				.getNamedQuery("getTAXRateCalculation.by.check.taxItemandDates")
				.setParameter("startDate", fromDate)
				.setParameter("endDate", toDate).setEntity("company", company);

		List<TAXRateCalculation> taxRateCalculations = query.list();

		List<ECSalesList> details = new ArrayList<ECSalesList>();

		Map<String, Double> customerWiseDetail = new LinkedHashMap<String, Double>();

		long transactionItemId = 0;

		for (TAXRateCalculation v : taxRateCalculations) {
			if (v.getTransactionItem().getTransaction()
					.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				if ((transactionItemId == 0)
						|| (transactionItemId != v.getTransactionItem().getID())) {

					String customerName;

					customerName = v.getTransactionItem().getTransaction()
							.getInvolvedPayee() != null ? v
							.getTransactionItem().getTransaction()
							.getInvolvedPayee().getName() : null;

					if (customerWiseDetail.containsKey(customerName)) {
						customerWiseDetail.put(customerName,
								(customerWiseDetail.get(customerName) + v
										.getLineTotal()));

					} else {
						customerWiseDetail.put(customerName, v.getLineTotal());

					}
					if (v.getTransactionItem().isVoid()) {
						customerWiseDetail.put(customerName,
								(customerWiseDetail.get(customerName) - v
										.getLineTotal()));
					}

					transactionItemId = v.getTransactionItem().getID();
				}
			}
		}

		String arr[] = new String[customerWiseDetail.keySet().size()];
		customerWiseDetail.keySet().toArray(arr);
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		for (String a : arr) {
			ECSalesList e = new ECSalesList();
			e.setName(a);
			e.setAmount(customerWiseDetail.get(a));
			details.add(e);
		}

		return new ArrayList<ECSalesList>(details);

	}

	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, FinanceDate fromDate, FinanceDate toDate,
			long companyId) throws DAOException, ParseException {
		// /////
		// ////////
		// /////////
		// //////
		// //////

		Session session = HibernateUtil.getCurrentSession();

		Query query = session
				.getNamedQuery("getReverseChargeListDetailReportEntries")
				.setParameter("startDate", fromDate.getDate())
				.setParameter("endDate", toDate.getDate())
				.setParameter("companyId", companyId);

		Map<String, List<ReverseChargeListDetail>> maps = new LinkedHashMap<String, List<ReverseChargeListDetail>>();

		List list = query.list();
		Iterator it = list.iterator();
		long tempTransactionItemID = 0;
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();

			if (((String) object[1]) != null
					&& ((String) object[1]).equals(payeeName)) {

				ReverseChargeListDetail r = new ReverseChargeListDetail();

				r.setAmount((Double) object[0]);
				r.setCustomerName((String) object[1]);
				r.setMemo((String) object[2]);
				r.setName(payeeName);
				r.setNumber((String) object[3]);
				r.setPercentage((Boolean) object[4]);
				r.setSalesPrice((Double) object[5]);
				r.setTransactionId((Long) object[6]);
				r.setTransactionType((Integer) object[7]);
				r.setDate(new ClientFinanceDate((Long) object[9]));

				if (maps.containsKey(r.getCustomerName())) {
					maps.get(r.getCustomerName()).add(r);
				} else {
					List<ReverseChargeListDetail> reverseChargesList = new ArrayList<ReverseChargeListDetail>();
					reverseChargesList.add(r);
					maps.put(r.getCustomerName(), reverseChargesList);
				}

				if (tempTransactionItemID == 0
						|| ((Long) object[12]) != tempTransactionItemID) {

					ReverseChargeListDetail r2 = new ReverseChargeListDetail();

					tempTransactionItemID = ((Long) object[12]);

					r2.setAmount((Double) object[8]);
					r2.setCustomerName((String) object[1]);
					r2.setDate(new ClientFinanceDate((Long) object[9]));
					r2.setMemo((String) object[10]);
					r2.setName((String) object[1]);
					r2.setNumber((String) object[3]);
					r2.setPercentage(false);
					r2.setSalesPrice((Double) object[11]);
					r2.setTransactionId((Long) object[6]);
					r2.setTransactionType((Integer) object[7]);

					if (maps.containsKey(r2.getCustomerName())) {
						maps.get(r2.getCustomerName()).add(r2);
					} else {
						List<ReverseChargeListDetail> reverseChargesList = new ArrayList<ReverseChargeListDetail>();
						reverseChargesList.add(r2);
						maps.put(r2.getCustomerName(), reverseChargesList);
					}
				}
			}
		}
		String[] names = new String[maps.keySet().size()];

		maps.keySet().toArray(names);

		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);

		List<ReverseChargeListDetail> reverseCharges = new ArrayList<ReverseChargeListDetail>();

		for (String s : names) {

			reverseCharges.addAll(maps.get(s));
		}

		return new ArrayList<ReverseChargeListDetail>(reverseCharges);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(final FinanceDate startDate,
			final FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAgedDebtors")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return prepareAgedDebotOrsorCreditors(new ArrayList<AgedDebtors>(l),
				startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(FinanceDate startDate,
			FinanceDate endDate, int intervalDays, int throughDaysPassOut,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getTransactionDate.by.dates")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setEntity("company", company);
		return null;
	}

	public ArrayList<AgedDebtors> getAgedCreditors(final FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAgedCreditors")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return prepareAgedDebotOrsorCreditors(new ArrayList<AgedDebtors>(l),
				startDate, endDate, companyId);

	}

	private ArrayList<AgedDebtors> prepareAgedDebotOrsorCreditors(List list,
			final FinanceDate startDate, FinanceDate endDate, long companyId) {
		Object[] object = null;
		Iterator iterator = list.iterator();
		List<AgedDebtors> queryResult = new ArrayList<AgedDebtors>();
		while ((iterator).hasNext()) {

			AgedDebtors agedDebtors = new AgedDebtors();
			object = (Object[]) iterator.next();

			agedDebtors.setName((String) object[0]);
			agedDebtors.setContact((String) object[1]);
			agedDebtors.setPhone((String) object[2]);
			agedDebtors.setType(object[3] == null ? 0 : ((Integer) object[3])
					.intValue());
			agedDebtors.setDate(new ClientFinanceDate((Long) object[4]));
			agedDebtors.setNumber((String) object[5]);
			agedDebtors.setReference((String) object[6]);
			agedDebtors.setDueDate(object[7] == null ? null
					: new ClientFinanceDate((Long) object[7]));
			agedDebtors.setPaymentTermName((String) object[8]);
			agedDebtors.setAmount(object[9] == null ? 0 : ((Double) object[9])
					.doubleValue());
			agedDebtors.setTotal(object[9] == null ? 0 : ((Double) object[9])
					.doubleValue());
			agedDebtors.setIsVoid(object[11] == null ? true
					: ((Boolean) object[11]).booleanValue());
			agedDebtors.setTransactionId((Long) object[12]);
			agedDebtors
					.setMemo(object[13] != null ? (String) object[13] : null);
			long ageing = getAgeing(agedDebtors.getDate(),
					agedDebtors.getDueDate(), endDate, companyId);
			int category = getCategory(ageing);
			agedDebtors.setAgeing(ageing);
			agedDebtors.setCategory(category);

			if (agedDebtors.getAmount() != 0)
				queryResult.add(agedDebtors);
		}

		Collections.sort(queryResult, new Comparator<AgedDebtors>() {

			public int compare(AgedDebtors arg0, AgedDebtors arg1) {
				return arg0.getCategory() > arg1.getCategory() ? 2 : arg0
						.getCategory() < arg1.getCategory() ? -1 : 0;
			}
		});
		return new ArrayList<AgedDebtors>(queryResult);
	}

	public long getAgeing(ClientFinanceDate transactionDate,
			ClientFinanceDate dueDate, final FinanceDate endDate, long companyId) {

		long ageing = 0;

		try {

			ClientFinanceDate ageingForDueorTranactionDate;
			Company company = getCompany(companyId);
			if (company.getPreferences()
					.getAgeingFromTransactionDateORDueDate() == CompanyPreferencesView.TYPE_AGEING_FROM_DUEDATE)
				ageingForDueorTranactionDate = dueDate;
			else
				ageingForDueorTranactionDate = transactionDate;

			if (!ageingForDueorTranactionDate.after(endDate
					.toClientFinanceDate()))
				ageing = UIUtils.getDays_between(ageingForDueorTranactionDate
						.getDateAsObject(), endDate.toClientFinanceDate()
						.getDateAsObject());

			// if (agedDebtors.getDueDate() != null
			// && !agedDebtors.getDueDate().equals("")
			// && !agedDebtors.getDueDate().after(
			// format.parse(endDate))) {
			//
			// diff = UIUtils.getDays_between(format.parse(endDate),
			// agedDebtors.getDueDate());
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ageing;
	}

	public ArrayList<PayeeStatementsList> getPayeeStatementsList(long id,
			FinanceDate fromDate, FinanceDate toDate, long companyId)
			throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session
					.getNamedQuery("getCreatableStatementForCustomer")
					.setParameter("startDate", fromDate.getDate())
					.setParameter("endDate", toDate.getDate())
					.setParameter("customerId", id)
					.setParameter("companyId", companyId);

			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<PayeeStatementsList> queryResult = new ArrayList<PayeeStatementsList>();
				while ((iterator).hasNext()) {

					PayeeStatementsList statementsList = new PayeeStatementsList();
					object = (Object[]) iterator.next();

					statementsList.setTransactiontype(object[0] == null ? null
							: (Integer) object[0]);
					statementsList.setTransactionNumber((String) object[1]);
					statementsList
							.setTransactionDate(((Long) object[2]) == null ? null
									: new ClientFinanceDate((Long) object[2]));
					statementsList.setDueDate(((Long) object[3]) == null ? null
							: new ClientFinanceDate((Long) object[3]));
					statementsList.setTotal((Double) object[4]);
					statementsList.setBalance((Double) object[5]);
					statementsList
							.setPayeeName(((String) object[6]) == null ? null
									: (String) object[6]);

					ClientAddress clientAddress = new ClientAddress();
					clientAddress
							.setAddress1(((String) object[7]) == null ? null
									: ((String) object[7]));
					clientAddress.setStreet(((String) object[8]) == null ? null
							: ((String) object[8]));
					clientAddress.setCity(((String) object[9]) == null ? null
							: ((String) object[9]));
					clientAddress
							.setStateOrProvinence(((String) object[10]) == null ? null
									: ((String) object[10]));
					clientAddress
							.setCountryOrRegion(((String) object[11]) == null ? null
									: ((String) object[11]));
					clientAddress
							.setZipOrPostalCode(((String) object[12]) == null ? null
									: ((String) object[12]));
					statementsList.setBillingAddress(clientAddress);

					statementsList.setSalesPerson(object[13] == null ? null
							: (String) object[13]);
					statementsList.setShippingMethod(object[14] == null ? null
							: (String) object[14]);
					statementsList.setPaymentTerm(object[15] == null ? null
							: (String) object[15]);
					statementsList.setTransactionId(object[16] == null ? null
							: ((Long) object[16]).longValue());
					long ageing = getAgeing(
							statementsList.getTransactionDate(),
							statementsList.getDueDate(), toDate, companyId);
					statementsList.setAgeing(ageing);
					statementsList.setCategory(getCategory(ageing));

					queryResult.add(statementsList);
				}
				return new ArrayList<PayeeStatementsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public int getCategory(long days) {
		// if (diff > 0)
		// agedDebtors.setCategory(1);
		if (days >= 0 && days <= 30)
			return 1;
		else if (days > 30 && days <= 60)
			return 2;
		else if (days > 60 && days <= 90)
			return 3;
		else if (days > 90)
			// else if (diff > 90 && diff < 120)
			return 4;
		// else if (diff > 120 && diff < 150)
		// agedDebtors.setCategory(5);
		// else if (diff > 150 && diff < 180)
		// agedDebtors.setCategory(6);
		return 0;
	}
}
