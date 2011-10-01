package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.ui.GraphChart;

public class DashboardManager extends Manager {

	public ArrayList<Double> getBankingChartValues(long accountNo,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		FinanceDate currentDate = new FinanceDate();

		Calendar dateCal[] = new Calendar[4];
		if (dateCal[3] == null)
			dateCal[3] = new GregorianCalendar();

		dateCal[3].setTime(currentDate.getAsDateObject());

		for (int i = 2; i >= 0; i--) {
			if (dateCal[i] == null)
				dateCal[i] = new GregorianCalendar();

			dateCal[i].setTime(dateCal[i + 1].getTime());
			dateCal[i].set(Calendar.DATE, dateCal[i].get(Calendar.DATE) - 1);

			if (dateCal[i].get(Calendar.DATE) <= 0) {
				dateCal[i].set(Calendar.MONTH,
						dateCal[i].get(Calendar.MONTH) - 1);
				dateCal[i].set(
						Calendar.DATE,
						dateCal[i].getActualMaximum(Calendar.DATE)
								- dateCal[i].get(Calendar.DATE));
			}

		}

		Query query = session
				.getNamedQuery("getPointsForBankAccount")
				.setLong("companyId", companyId)
				.setParameter("accountNo", accountNo)
				.setParameter("previousThreeDaysBackDateCal",
						new FinanceDate(dateCal[0].getTime()).getDate())
				.setParameter("previousTwoDaysBackDateCal",
						new FinanceDate(dateCal[1].getTime()).getDate())
				.setParameter("previousOneDayBackDateCal",
						new FinanceDate(dateCal[2].getTime()).getDate())
				.setParameter("previousOneDayBackDateCal",
						currentDate.getDate());

		List<Double> gPoints = new ArrayList<Double>();
		List list = query.list();
		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();

				gPoints.add(object[2] == null ? 0 : (Double) object[2]);
				gPoints.add(object[3] == null ? 0 : (Double) object[3]);
				gPoints.add(object[4] == null ? 0 : (Double) object[4]);
				gPoints.add(object[5] == null ? 0 : (Double) object[5]);

			}
		}
		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<CreditCardCharge> getCreditCardChargesThisMonth(
			final long date, long companyId) throws DAOException {
		// SELECT * from com.vimukti.accounter.core.CREDIT_CARD_CHARGES CCC JOIN
		// TRANSACTION T ON T.ID =
		// CCC.ID AND T.T_DATE = CURRENT_DATE
		// List<CreditCardCharge> list = template.find(
		// "from CreditCardCharge ccc where ccc.company = ? and  MONTH(ccc.date) = MONTH(?) and YEAR(ccc.date) = YEAR(?)"
		// ,new Object[] {company, date, date});

		Session session = HibernateUtil.getCurrentSession();

		int month = 0;
		int year = 0;
		FinanceDate fdate = new FinanceDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fdate.getAsDateObject());
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		Query query = session.getNamedQuery("getCreditCardChargesThisMonth")
				.setInteger("month", month).setLong("companyId", companyId);
		Iterator iterator = query.list().iterator();
		List<CreditCardCharge> list = new ArrayList<CreditCardCharge>();
		while (iterator.hasNext()) {
			list.add((CreditCardCharge) iterator.next());
		}

		return new ArrayList<CreditCardCharge>(list);
	}

	public ArrayList<OverDueInvoicesList> getOverDueInvoices(long companyId)
			throws DAOException {
		// try {
		// Session session = getSessionFactory().openSession();
		// Query query = session.createSQLQuery(
		// "SELECT * FROM INVOICE I LEFT JOIN TRANSACTION T ON I.ID = T.ID WHERE T.COMPANY_ID = :C_ID AND I.BALANCE_DUE > 0.0"
		// ).addEntity(Invoice.class);
		// query.setLong("C_ID", company);
		// List<Invoice> list = query.list();
		// // HibernateTemplate template = getHibernateTemplate();
		// // List<Invoice> list = template.find(
		// "from Invoice i where i.company = ? and i.balanceDue > 0.0 and i.dueDate <= current_date"
		// ,new Object[] {company});
		//
		// if (list != null) {
		// return list;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }

		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestOverDueInvoices")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<OverDueInvoicesList> queryResult = new ArrayList<OverDueInvoicesList>();
				while ((iterator).hasNext()) {

					OverDueInvoicesList overDueInvoicesList = new OverDueInvoicesList();
					object = (Object[]) iterator.next();

					overDueInvoicesList
							.setTransactionId((object[0] == null ? null
									: ((String) object[0])));
					overDueInvoicesList.setDueDate((object[1] == null ? null
							: (new ClientFinanceDate((Long) object[1]))));
					overDueInvoicesList.setCustomerName((String) object[2]);
					overDueInvoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					overDueInvoicesList.setTotalAmount((Double) object[4]);
					overDueInvoicesList.setPayment((Double) object[5]);

					overDueInvoicesList.setBalanceDue((Double) object[6]);

					queryResult.add(overDueInvoicesList);
				}
				return new ArrayList<OverDueInvoicesList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Double> getMoneyInChartValues(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		FinanceDate currentDate = new FinanceDate();

		List<Double> gPoints = new ArrayList<Double>();

		for (int i = 4; i >= -1; i--) {
			Calendar startDateCal = Calendar.getInstance();
			startDateCal.setTime(currentDate.getAsDateObject());
			startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH)
					- i);
			startDateCal.set(Calendar.DATE, 1);

			Calendar endDateCal = Calendar.getInstance();
			endDateCal.setTime(currentDate.getAsDateObject());
			endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) - i);
			endDateCal.set(Calendar.DATE,
					endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			gPoints.add(getMoneyInForDates(
					new FinanceDate(startDateCal.getTime()).getDate(),
					new FinanceDate(endDateCal.getTime()).getDate(), companyId));
		}

		Object res = session.getNamedQuery("getInvoicesDue")
				.setParameter("presentDate", 0).setLong("companyId", companyId)
				.uniqueResult();
		double amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		res = session.getNamedQuery("getInvoicesDue")
				.setParameter("presentDate", (new FinanceDate()).getDate())
				.setLong("companyId", companyId).uniqueResult();
		amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<Double> getMoneyOutChartValues(long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		FinanceDate currentDate = new FinanceDate();

		Calendar dateCal = Calendar.getInstance();

		List<Double> gPoints = new ArrayList<Double>();
		for (int i = 0; i < 30; i++) {

			dateCal.setTime(currentDate.getAsDateObject());
			dateCal.set(Calendar.DATE, dateCal.get(Calendar.DATE) + i);

			gPoints.add(getMoneyOutOnDate(
					new FinanceDate(dateCal.getTime()).getDate(), companyId));
		}

		Object res = session.getNamedQuery("getBillsDue")
				.setParameter("presentDate", 0).setLong("companyId", companyId)
				.uniqueResult();
		double amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		res = session.getNamedQuery("getBillsDue")
				.setParameter("presentDate", (new FinanceDate()).getDate())
				.setLong("companyId", companyId).uniqueResult();
		amount = res == null ? 0 : (Double) res;
		gPoints.add(amount);

		return new ArrayList<Double>(gPoints);
	}

	public ArrayList<Double> getExpensePortletValues(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = null;

		FinanceDate currentDate = new FinanceDate();

		query = session.getNamedQuery("getExpenseTotalAmounts").setLong(
				"companyId", companyId);

		List<Double> gPoints = new ArrayList<Double>();
		List list = query.list();
		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();

				gPoints.add(object[2] == null ? 0 : (Double) object[2]);
				gPoints.add(object[3] == null ? 0 : (Double) object[3]);
				gPoints.add(object[4] == null ? 0 : (Double) object[4]);
				gPoints.add(object[5] == null ? 0 : (Double) object[5]);
			}
		}
		return new ArrayList<Double>(gPoints);
	}

	private double getMoneyInForDates(long startDate, long endDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getMoneyInForDates")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setLong("companyId", companyId);

		List<Double> list = query.list();
		double amount = 0;
		if (list != null && !list.isEmpty()) {
			for (Double d : list) {
				amount += (d != null ? d : 0);
			}
			return amount;
		} else {
			return 0.0D;
		}
	}

	private double getMoneyOutOnDate(long date, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getMoneyOutOnDate")
				.setParameter("date", date).setLong("companyId", companyId);

		List<Double> list = query.list();
		double amount = 0;
		if (list != null && !list.isEmpty()) {
			for (Double d : list) {
				amount += (d != null ? d : 0);
			}
			return amount;
		} else {
			return 0.0D;
		}
	}

	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountNo, long companyId) throws DAOException {

		if (chartType == GraphChart.BANK_ACCOUNT_CHART_TYPE) {
			return getBankingChartValues(accountNo, companyId);
		} else if (chartType == GraphChart.ACCOUNTS_RECEIVABLE_CHART_TYPE) {
			return getMoneyInChartValues(companyId);
		} else if (chartType == GraphChart.ACCOUNTS_PAYABLE_CHART_TYPE) {
			return getMoneyOutChartValues(companyId);
		} else {
			return getExpensePortletValues(companyId);
		}
	}

	public ArrayList<BillsList> getEmployeeExpensesByStatus(
			String employeeName, int status, long companyId)
			throws DAOException {

		List<BillsList> billsList = new ArrayList<BillsList>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = null;
		if (employeeName != null)
			query = session
					.getNamedQuery(
							"getCashPurchase.by.employeeNmae.expenseStatusandtype")
					.setParameter("employeeName", employeeName)
					.setParameter("expenseStatus", status)
					.setParameter("type", Transaction.TYPE_EMPLOYEE_EXPENSE)
					.setEntity("company", company);
		else
			query = session
					.getNamedQuery("getCashPurchase.by.expenseStatusandtype")
					.setParameter("expenseStatus", status)
					.setParameter("type", Transaction.TYPE_EMPLOYEE_EXPENSE)
					.setEntity("company", company);

		List<CashPurchase> cashpurchase = query.list();
		for (CashPurchase cp : cashpurchase) {
			BillsList bills = new BillsList();
			bills.setTransactionId(cp.getID());
			bills.setOriginalAmount(cp.getTotal());
			bills.setVendorName(cp.getEmployee() != null ? cp.getEmployee()
					.getName() : "");
			bills.setDate(new ClientFinanceDate(cp.getDate().getDate()));
			bills.setExpenseStatus(status);
			bills.setType(Transaction.TYPE_EMPLOYEE_EXPENSE);
			bills.setPayFrom(cp.getPayFrom() != null ? cp.getPayFrom().getID()
					: 0);

			/*
			 * Here, to set transaction created date temporarly using setDueDate
			 * method
			 */
			bills.setDueDate(new ClientFinanceDate(cp.getCreatedDate()
					.getTime()));
			billsList.add(bills);
		}
		return new ArrayList<BillsList>(billsList);
	}

	public KeyFinancialIndicators getKeyFinancialIndicators(long companyId)
			throws DAOException {

		KeyFinancialIndicators keyFinancialIndicators = new KeyFinancialIndicators();

		Map<String, Map<Integer, Double>> rows = new LinkedHashMap<String, Map<Integer, Double>>();

		Session session = HibernateUtil.getCurrentSession();
		List<Account> accounts = new ArrayList<Account>(session
				.getNamedQuery("list.Account")
				.setEntity("company", getCompany(companyId)).list());
		// List<Account> accounts = getCompany().getAccounts();

		Set<Account> sales = new HashSet<Account>();
		Set<Account> directCosts = new HashSet<Account>();
		Set<Account> indirectCosts = new HashSet<Account>();
		Set<Account> bankAccounts = new HashSet<Account>();

		for (Account account : accounts) {
			switch (account.getType()) {
			case Account.TYPE_INCOME:
				sales.add(account);
				break;
			case Account.TYPE_COST_OF_GOODS_SOLD:
				directCosts.add(account);
				break;
			case Account.TYPE_OTHER_EXPENSE:
				directCosts.add(account);
				break;
			case Account.TYPE_EXPENSE:
				indirectCosts.add(account);
				break;
			case Account.TYPE_OTHER_CURRENT_ASSET:
				Long l1 = Long.parseLong(account.getNumber());
				if (l1 >= 1100 && l1 <= 1179)
					bankAccounts.add(account);
				break;
			}
		}

		Map<Integer, Double> salesEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : sales) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (salesEntries.containsKey(key)) {
					salesEntries.put(key, salesEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					salesEntries.put(key, (Double) monthViceAmounts.get(key));
				}
			}
		}
		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_SALES,
				salesEntries);

		Map<Integer, Double> grossProfitEntries = new LinkedHashMap<Integer, Double>();

		Map<Integer, Double> directCostsEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : directCosts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (directCostsEntries.containsKey(key)) {
					directCostsEntries.put(key, directCostsEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					directCostsEntries.put(key,
							(Double) monthViceAmounts.get(key));
				}

				// double salesEntryAmount = 0.0;
				// if (salesEntries.containsKey(key)) {
				// salesEntryAmount = salesEntries.get(key);
				// }
				// double directCostsAmount = 0.0;
				// if (directCostsEntries.containsKey(key)) {
				// directCostsAmount = directCostsEntries.get(key);
				// }
				//
				// // to calculate the gross profit
				// if (grossProfitEntries.containsKey(key)) {
				// grossProfitEntries.put(key, grossProfitEntries.get(key)
				// + (salesEntryAmount - directCostsAmount));
				// } else {
				// grossProfitEntries.put(key, salesEntryAmount
				// - directCostsAmount);
				// }
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_DIRECT_COSTS,
				directCostsEntries);

		// for (Integer key : salesEntries.keySet()) {
		//
		// double grossProfitAmount = 0.0;
		// if (directCostsEntries.containsKey(key)) {
		//
		// grossProfitAmount = salesEntries.get(key)
		// - directCostsEntries.get(key);
		// } else {
		// grossProfitAmount = salesEntries.get(key);
		// }
		//
		// // to calculate the gross profit
		// if (grossProfitEntries.containsKey(key)) {
		// grossProfitEntries.put(key, grossProfitEntries.get(key)
		// + (grossProfitAmount));
		// } else {
		// grossProfitEntries.put(key, grossProfitAmount);
		// }
		//
		// }
		List<Integer> keysList = session
				.createSQLQuery(
						"select month from ACCOUNT_AMOUNTS group by month order by month")
				.list();
		for (int key : keysList) {
			double salesEntryAmount = 0.0;
			if (salesEntries.containsKey(key)) {
				salesEntryAmount = salesEntries.get(key);
			}
			double directCostsAmount = 0.0;
			if (directCostsEntries.containsKey(key)) {
				directCostsAmount = directCostsEntries.get(key);
			}

			// to calculate the gross profit
			if (grossProfitEntries.containsKey(key)) {
				grossProfitEntries.put(key, grossProfitEntries.get(key)
						+ (salesEntryAmount - directCostsAmount));
			} else {
				grossProfitEntries.put(key, salesEntryAmount
						- directCostsAmount);
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_GROSS_PROFIT,
				grossProfitEntries);

		Map<Integer, Double> netProfitEntries = new LinkedHashMap<Integer, Double>();

		Map<Integer, Double> indirectCostsEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : indirectCosts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (indirectCostsEntries.containsKey(key)) {
					indirectCostsEntries.put(key, indirectCostsEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					indirectCostsEntries.put(key,
							(Double) monthViceAmounts.get(key));
				}
				// to calculate the net profit.

				// double grossProfitAmount = 0.0;
				// if (grossProfitEntries.containsKey(key)) {
				// grossProfitAmount = grossProfitEntries.get(key);
				// }
				// double indirectCostsAmount = 0.0;
				// if (indirectCostsEntries.containsKey(key)) {
				// indirectCostsAmount = indirectCostsEntries.get(key);
				// }
				//
				// if (netProfitEntries.containsKey(key)) {
				// netProfitEntries.put(key, netProfitEntries.get(key)
				// + (grossProfitAmount - indirectCostsAmount));
				// } else {
				// netProfitEntries.put(key, grossProfitAmount
				// - indirectCostsAmount);
				// }

			}
		}
		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_INDIRECT_COSTS,
				indirectCostsEntries);

		// for (Integer key : grossProfitEntries.keySet()) {
		//
		// double netProfitAmount = 0.0;
		// if (indirectCostsEntries.containsKey(key)) {
		//
		// netProfitAmount = grossProfitEntries.get(key) -
		// indirectCostsEntries.get(key);
		// } else {
		// netProfitAmount = grossProfitEntries.get(key);
		// }
		//
		// // to calculate the gross profit
		// if (netProfitEntries.containsKey(key)) {
		// netProfitEntries.put(key, grossProfitEntries.get(key)
		// + (netProfitAmount));
		// } else {
		// netProfitEntries.put(key, netProfitAmount);
		// }
		//
		// }

		for (int key : keysList) {
			double grossProfitAmount = 0.0;
			if (grossProfitEntries.containsKey(key)) {
				grossProfitAmount = grossProfitEntries.get(key);
			}
			double indirectCostsAmount = 0.0;
			if (indirectCostsEntries.containsKey(key)) {
				indirectCostsAmount = indirectCostsEntries.get(key);
			}

			if (netProfitEntries.containsKey(key)) {
				netProfitEntries.put(key, netProfitEntries.get(key)
						+ (grossProfitAmount - indirectCostsAmount));
			} else {
				netProfitEntries.put(key, grossProfitAmount
						- indirectCostsAmount);
			}
		}

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_NET_PROFIT,
				netProfitEntries);

		Map<Integer, Double> bankEntries = new LinkedHashMap<Integer, Double>();
		for (Account account : bankAccounts) {
			Map monthViceAmounts = account.getMonthViceAmounts();
			Set<Integer> keys = monthViceAmounts.keySet();
			for (Integer key : keys) {
				if (bankEntries.containsKey(key)) {
					bankEntries.put(key, bankEntries.get(key)
							+ (Double) monthViceAmounts.get(key));
				} else {
					bankEntries.put(key, (Double) monthViceAmounts.get(key));
				}
			}
		}
		Map<Integer, Double> empty = new LinkedHashMap<Integer, Double>();
		rows.put("empty", empty);

		rows.put(AccounterServerConstants.FINANCIAL_INDICATOR_BANK_ACCOUNTS,
				bankEntries);
		keyFinancialIndicators.setIndicators(rows);
		return keyFinancialIndicators;
	}

}
