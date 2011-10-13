package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PaySalesTax;
import com.vimukti.accounter.core.PayVAT;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionPayBill;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class VendorManager extends Manager {
	HashMap<Integer, Integer> boxThresholds = new HashMap<Integer, Integer>();

	public VendorManager() {
		boxThresholds.put(1, 600);
		boxThresholds.put(2, 10);
		boxThresholds.put(3, 600);
		boxThresholds.put(4, 0);
		boxThresholds.put(5, 0);
		boxThresholds.put(6, 600);
		boxThresholds.put(7, 600);
		boxThresholds.put(8, 10);
		boxThresholds.put(9, 5000);
		boxThresholds.put(10, 600);
		boxThresholds.put(13, 0);
		boxThresholds.put(14, 0);
	}

	public ArrayList<BillsList> getBillsList(boolean isExpensesList,
			long companyId) throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			// FIXME:: change the sql query to hql query
			Query query = session.getNamedQuery("getBillsList").setParameter(
					"companyId", companyId);
			;
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<BillsList> queryResult = new ArrayList<BillsList>();
				while ((iterator).hasNext()) {

					BillsList billsList = new BillsList();
					object = (Object[]) iterator.next();

					billsList.setTransactionId((Long) object[0]);
					billsList.setType((Integer) object[1]);
					billsList.setDueDate(((Long) object[2]) == null ? null
							: new ClientFinanceDate((Long) object[2]));
					billsList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					billsList.setVendorName((String) object[4]);
					billsList.setOriginalAmount((Double) object[5]);
					billsList.setBalance(((Double) object[6]) == null ? null
							: (Double) object[6]);
					billsList.setVoided((Boolean) object[7]);
					billsList.setStatus((Integer) object[8]);
					billsList.setDate(new ClientFinanceDate((Long) object[9]));
					billsList.setExpenseStatus((Integer) object[10]);

					// if (object[4] != null) {
					if (isExpensesList) {
						if (billsList.getType() == ClientTransaction.TYPE_CASH_EXPENSE
								|| billsList.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE
								|| billsList.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
							queryResult.add(billsList);
					} else
						queryResult.add(billsList);
					// }
				}
				BillsList tmpBillsList;
				for (int i = 0; i < queryResult.size(); i++) {
					for (int j = 0; j < queryResult.size() - 1 - i; j++)
						if (queryResult.get(j + 1).getDate().getDate() > queryResult
								.get(j).getDate().getDate()) {
							tmpBillsList = queryResult.get(j);
							queryResult.set(j, queryResult.get(j + 1));
							queryResult.set(j + 1, tmpBillsList);
						}
				}

				return new ArrayList<BillsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getPayBillTransactionsList")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			List<PayBillTransactionList> queryResult = new ArrayList<PayBillTransactionList>();
			Company company = getCompany(companyId);
			query = session.getNamedQuery(
					"getEntry.by.debitand.balanceDue.orderbyid").setParameter(
					"company", company);
			List<JournalEntry> openingBalanceEntries = query.list();

			for (JournalEntry je : openingBalanceEntries) {
				PayBillTransactionList payBillTransactionList = new PayBillTransactionList();
				payBillTransactionList.setTransactionId(je.getID());
				payBillTransactionList.setType(je.getType());
				payBillTransactionList.setDueDate(new ClientFinanceDate(je
						.getDate().getDate()));
				payBillTransactionList.setBillNumber(je.getNumber());
				payBillTransactionList.setOriginalAmount(je.getDebitTotal());
				payBillTransactionList.setAmountDue(je.getBalanceDue());
				queryResult.add(payBillTransactionList);
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					PayBillTransactionList payBillTransactionList = new PayBillTransactionList();
					object = (Object[]) iterator.next();

					payBillTransactionList.setTransactionId((Long) object[0]);
					payBillTransactionList.setType((Integer) object[1]);
					payBillTransactionList.setDueDate(new ClientFinanceDate(
							(Long) object[2]));
					payBillTransactionList.setVendorName((String) object[3]);
					payBillTransactionList
							.setBillNumber((object[4] == null ? null
									: ((String) object[4])));

					payBillTransactionList
							.setOriginalAmount((Double) object[5]);
					payBillTransactionList.setAmountDue((Double) object[6]);
					payBillTransactionList.setPayment((Double) object[7]);
					payBillTransactionList.setPaymentMethod((String) object[8]);
					queryResult.add(payBillTransactionList);
				}

			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
			return new ArrayList<PayBillTransactionList>(queryResult);
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<ClientTDSInfo> getPayBillsByTDS(long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<ClientTDSInfo> tdsInfos = new ArrayList<ClientTDSInfo>();

		List<PayBill> paybills = new ArrayList<PayBill>();
		List<PayBill> transactionPaybills = new ArrayList<PayBill>();
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		ClientVendor clientVendor = null;
		ClientFinanceDate clientFinanceDate = null;
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("get.PayBills.by.tds")
				.setParameter("company", company);
		paybills = (List<PayBill>) query.list();
		ClientTDSInfo clientTDSInfo = null;
		ClientPayBill clientPayBill = null;
		for (PayBill p : paybills) {

			try {
				clientVendor = clientConvertUtil.toClientObject(p.getVendor(),
						ClientVendor.class);
				clientFinanceDate = clientConvertUtil.toClientObject(
						p.getDate(), ClientFinanceDate.class);
			} catch (Exception e) {

			}
			clientTDSInfo = new ClientTDSInfo();
			clientTDSInfo.setVendor(clientVendor);
			clientTDSInfo.setDate(clientFinanceDate);

			for (TransactionPayBill tp : p.getTransactionPayBill()) {

				clientTDSInfo.setTdsAmount(tp.getTdsAmount());
				clientTDSInfo.setPayment(tp.getPayment());
				clientTDSInfo.setOrginalBalance(tp.getOriginalAmount());
				tdsInfos.add(clientTDSInfo);

			}
		}

		return tdsInfos;
	}

	public ArrayList<Vendor> getLatestVendors(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestVendors")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<Vendor> list = new ArrayList<Vendor>();
			while (iterator.hasNext()) {
				while (iterator.hasNext()) {
					object = (Object[]) iterator.next();
					Vendor vendor = new Vendor();
					// vendor.setID((object[0] == null ? null : ((Long)
					// object[0])));
					vendor.setName((String) object[1]);
					vendor.setDate(new FinanceDate((Long) object[2]));
					// vendor.setID((String) object[3]);
					list.add(vendor);
				}
			}
			if (list != null) {
				return new ArrayList<Vendor>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<IssuePaymentTransactionsList> getChecks(long companyId)
			throws DAOException {

		// Session session = getSessionFactory().openSession();
		// Query query = session.createSQLQuery(new StringBuilder().append(
		// " SELECT WC.ID, WC.VERSION, WC.CREATED_DATE, WC.MODIFIED_ON, WC.KEY, WC.PAY_TO_TYPE, WC.ACCOUNT_ID, WC.CUSTOMER_ID, WC.VENDOR_ID, WC.TAX_AGENCY_ID, WC.ADDRESS_ID, WC.AMOUNT, WC.TOTAL, WC.MEMO FROM WRITE_CHECKS WC JOIN TRANSACTION  T ON T.ID = WC.ID AND T.STATUS = 0"
		// ).toString());
		// Iterator iterator = query.list().iterator();
		// List<WriteCheck> list = new ArrayList<WriteCheck>();
		// while(iterator.hasNext()){
		// list.add((WriteCheck)iterator.next());
		// }

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List<IssuePaymentTransactionsList> issuePaymentTransactionsList = new ArrayList<IssuePaymentTransactionsList>();
		Query query = session
				.getNamedQuery("getWriteCheck.by.status")
				.setInteger("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		List list = query.list();
		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				WriteCheck wc = (WriteCheck) i.next();
				issuePaymentTransaction.setTransactionId(wc.getID());
				issuePaymentTransaction.setType(wc.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate(wc
						.getDate().getDate()));
				issuePaymentTransaction.setNumber(wc.getNumber());
				issuePaymentTransaction.setName((wc.getCustomer() != null) ? wc
						.getCustomer().getName()
						: ((wc.getVendor() != null) ? wc.getVendor().getName()
								: null));
				issuePaymentTransaction.setMemo(wc.getMemo());
				issuePaymentTransaction.setAmount(wc.getAmount());
				issuePaymentTransaction.setPaymentMethod(wc.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);
			}
		}
		query = session
				.getNamedQuery("getCustomerRefund.by.isvoidandstatus")
				.setInteger("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		;
		list = query.list();
		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				CustomerRefund cr = (CustomerRefund) i.next();
				issuePaymentTransaction.setTransactionId(cr.getID());
				issuePaymentTransaction.setType(cr.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate((long) cr
						.getDate().getDate()));
				issuePaymentTransaction.setNumber(cr.getNumber());
				issuePaymentTransaction.setName(cr.getPayTo().getName());
				issuePaymentTransaction.setMemo(cr.getMemo());
				issuePaymentTransaction.setAmount(cr.getTotal());
				issuePaymentTransaction.setPaymentMethod(cr.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);

			}
		}

		query = session
				.getNamedQuery("getPaySalesTax.by.status")
				.setInteger("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		list = query.list();

		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				PaySalesTax pst = (PaySalesTax) i.next();
				issuePaymentTransaction.setTransactionId(pst.getID());
				issuePaymentTransaction.setType(pst.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate(
						(long) pst.getDate().getDate()));
				issuePaymentTransaction.setNumber(pst.getNumber());
				issuePaymentTransaction.setName("TaxAgency Payment");
				// issuePaymentTransaction.setMemo(pst.getMemo());
				issuePaymentTransaction.setAmount(pst.getTotal());
				issuePaymentTransaction
						.setPaymentMethod(pst.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);

			}
		}
		query = session
				.getNamedQuery("getPayBill.form.status")
				.setParameter("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		list = query.list();

		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				PayBill pb = (PayBill) i.next();
				issuePaymentTransaction.setTransactionId(pb.getID());
				issuePaymentTransaction.setType(pb.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate((long) pb
						.getDate().getDate()));
				issuePaymentTransaction.setNumber(pb.getNumber());
				issuePaymentTransaction.setName(pb.getVendor() != null ? pb
						.getVendor().getName() : null);
				issuePaymentTransaction.setMemo(pb.getMemo());
				issuePaymentTransaction.setAmount(pb.getTotal());
				issuePaymentTransaction.setPaymentMethod(pb.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);

			}
		}

		query = session
				.getNamedQuery("getCreditCardCharge.form.status")
				.setParameter("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		list = query.list();

		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				CreditCardCharge cc = (CreditCardCharge) i.next();
				issuePaymentTransaction.setTransactionId(cc.getID());
				issuePaymentTransaction.setType(cc.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate((long) cc
						.getDate().getDate()));
				issuePaymentTransaction.setNumber(cc.getNumber());
				issuePaymentTransaction.setName(cc.getVendor() != null ? cc
						.getVendor().getName() : null);
				issuePaymentTransaction.setMemo(cc.getMemo());
				issuePaymentTransaction.setAmount(cc.getTotal());
				issuePaymentTransaction.setPaymentMethod(cc.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);

			}
		}

		query = session
				.getNamedQuery("getCashPurchase.form.status")
				.setParameter("status",
						Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
				.setEntity("company", company);
		list = query.list();

		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
				CashPurchase cc = (CashPurchase) i.next();
				issuePaymentTransaction.setTransactionId(cc.getID());
				issuePaymentTransaction.setType(cc.getType());
				issuePaymentTransaction.setDate(new ClientFinanceDate((long) cc
						.getDate().getDate()));
				issuePaymentTransaction.setNumber(cc.getNumber());
				issuePaymentTransaction.setName(cc.getVendor() != null ? cc
						.getVendor().getName() : null);
				issuePaymentTransaction.setMemo(cc.getMemo());
				issuePaymentTransaction.setAmount(cc.getTotal());
				issuePaymentTransaction.setPaymentMethod(cc.getPaymentMethod());

				issuePaymentTransactionsList.add(issuePaymentTransaction);

			}
		}

		if (issuePaymentTransactionsList != null) {
			return new ArrayList<IssuePaymentTransactionsList>(
					issuePaymentTransactionsList);
		} else
			throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
					null));
	}

	public ArrayList<IssuePaymentTransactionsList> getChecks(long accountId,
			long companyId) throws DAOException {
		try {
			// Session session = getSessionFactory().openSession();
			// Query query = session.createSQLQuery(new StringBuilder().append(
			// " SELECT WC.ID, WC.VERSION, WC.CREATED_DATE, WC.MODIFIED_ON, WC.KEY, WC.PAY_TO_TYPE, WC.ACCOUNT_ID, WC.CUSTOMER_ID, WC.VENDOR_ID, WC.TAX_AGENCY_ID, WC.ADDRESS_ID, WC.AMOUNT, WC.TOTAL, WC.MEMO FROM WRITE_CHECKS WC JOIN TRANSACTION  T ON T.ID = WC.ID AND  WC.ACCOUNT_ID = "
			// ).append(accountid).append(" AND T.STATUS = 0").toString());
			// // query.setLong(1, accountid);
			// Iterator iterator = query.list().iterator();
			// List<WriteCheck> list = new ArrayList<WriteCheck>();
			// while(iterator.hasNext()){
			// list.add((WriteCheck)iterator.next());
			// }

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<IssuePaymentTransactionsList> issuePaymentTransactionsList = new ArrayList<IssuePaymentTransactionsList>();

			Query query = session
					.getNamedQuery("getWriteCheck.by.bankacountIdandstatus")
					.setLong("id", accountId)
					.setInteger(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			List list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					WriteCheck wc = (WriteCheck) i.next();
					issuePaymentTransaction.setTransactionId(wc.getID());
					issuePaymentTransaction.setType(wc.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) wc.getDate().getDate()));
					issuePaymentTransaction.setNumber(wc.getNumber());
					issuePaymentTransaction
							.setName((wc.getCustomer() != null) ? wc
									.getCustomer().getName()
									: ((wc.getVendor() != null) ? wc
											.getVendor().getName() : wc
											.getTaxAgency().getName()));
					issuePaymentTransaction.setMemo(wc.getMemo());
					issuePaymentTransaction.setAmount(wc.getAmount());
					issuePaymentTransaction.setPaymentMethod(wc
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);
				}
			}

			query = session
					.getNamedQuery(
							"getCustomerRefund.by.payFromand.isvoid.status")
					.setParameter("id", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);

			list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					CustomerRefund cr = (CustomerRefund) i.next();
					issuePaymentTransaction.setTransactionId(cr.getID());
					issuePaymentTransaction.setType(cr.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) cr.getDate().getDate()));
					issuePaymentTransaction.setNumber(cr.getNumber());
					issuePaymentTransaction.setName(cr.getPayTo().getName());
					issuePaymentTransaction.setMemo(cr.getMemo());
					issuePaymentTransaction.setAmount(cr.getTotal());
					issuePaymentTransaction.setPaymentMethod(cr
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			query = session
					.getNamedQuery("getPaySalesTax.by.payFromand.isvoid.status")
					.setParameter("id", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					PaySalesTax pst = (PaySalesTax) i.next();
					issuePaymentTransaction.setTransactionId(pst.getID());
					issuePaymentTransaction.setType(pst.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) pst.getDate().getDate()));
					issuePaymentTransaction.setNumber(pst.getNumber());
					issuePaymentTransaction.setName("TaxAgency Payment");
					// issuePaymentTransaction.setMemo(pst.getMemo());
					issuePaymentTransaction.setAmount(pst.getTotal());
					issuePaymentTransaction.setPaymentMethod(pst
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			query = session
					.getNamedQuery("getPayBill.form.accountId.and.status")
					.setLong("accountId", accountId)
					.setInteger(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					PayBill pst = (PayBill) i.next();
					issuePaymentTransaction.setTransactionId(pst.getID());
					issuePaymentTransaction.setType(pst.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) pst.getDate().getDate()));
					issuePaymentTransaction.setNumber(pst.getNumber());
					issuePaymentTransaction
							.setName(pst.getVendor() != null ? pst.getVendor()
									.getName() : null);
					issuePaymentTransaction.setMemo(pst.getMemo());
					issuePaymentTransaction.setAmount(pst.getTotal());
					issuePaymentTransaction.setPaymentMethod(pst
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}
			query = session
					.getNamedQuery("getPayVAT.form.accountId.and.status")
					.setParameter("accountId", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					PayVAT pv = (PayVAT) i.next();
					issuePaymentTransaction.setTransactionId(pv.getID());
					issuePaymentTransaction.setType(pv.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) pv.getDate().getDate()));
					issuePaymentTransaction.setNumber(pv.getNumber());
					issuePaymentTransaction.setName("VAT Agency Payment");
					issuePaymentTransaction.setMemo(null);
					issuePaymentTransaction.setAmount(pv.getTotal());
					issuePaymentTransaction.setPaymentMethod(pv
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}
			query = session
					.getNamedQuery("getReceiveVAT.form.accountId.and.status")
					.setParameter("accountId", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					ReceiveVAT rv = (ReceiveVAT) i.next();
					issuePaymentTransaction.setTransactionId(rv.getID());
					issuePaymentTransaction.setType(rv.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) rv.getDate().getDate()));
					issuePaymentTransaction.setNumber(rv.getNumber());
					issuePaymentTransaction.setName("VAT Agency Payment");
					issuePaymentTransaction.setMemo(null);
					issuePaymentTransaction.setAmount(rv.getTotal());
					issuePaymentTransaction.setPaymentMethod(rv
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			query = session
					.getNamedQuery(
							"getCreditCardCharge.form.accountId.and.status")
					.setParameter("accountId", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					CreditCardCharge pst = (CreditCardCharge) i.next();
					issuePaymentTransaction.setTransactionId(pst.getID());
					issuePaymentTransaction.setType(pst.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) pst.getDate().getDate()));
					issuePaymentTransaction.setNumber(pst.getNumber());
					issuePaymentTransaction
							.setName(pst.getVendor() != null ? pst.getVendor()
									.getName() : null);
					issuePaymentTransaction.setMemo(pst.getMemo());
					issuePaymentTransaction.setAmount(pst.getTotal());
					issuePaymentTransaction.setPaymentMethod(pst
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			query = session
					.getNamedQuery("getCashPurchase.form.accountId.and.status")
					.setParameter("accountId", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();
			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					CashPurchase pst = (CashPurchase) i.next();
					issuePaymentTransaction.setTransactionId(pst.getID());
					issuePaymentTransaction.setType(pst.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) pst.getDate().getDate()));
					issuePaymentTransaction.setNumber(pst.getNumber());
					issuePaymentTransaction
							.setName(pst.getVendor() != null ? pst.getVendor()
									.getName()
									: (pst.getEmployee() != null ? pst
											.getEmployee().getName() : null));
					issuePaymentTransaction.setMemo(pst.getMemo());
					issuePaymentTransaction.setAmount(pst.getTotal());
					issuePaymentTransaction.setPaymentMethod(pst
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			query = session
					.getNamedQuery(
							"getCustomerPrePayment.form.accountId.and.status")
					.setParameter("accountId", accountId)
					.setParameter(
							"status",
							Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.setEntity("company", company);
			list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					IssuePaymentTransactionsList issuePaymentTransaction = new IssuePaymentTransactionsList();
					CustomerPrePayment cpp = (CustomerPrePayment) i.next();
					issuePaymentTransaction.setTransactionId(cpp.getID());
					issuePaymentTransaction.setType(cpp.getType());
					issuePaymentTransaction.setDate(new ClientFinanceDate(
							(long) cpp.getDate().getDate()));
					issuePaymentTransaction.setNumber(cpp.getNumber());
					issuePaymentTransaction
							.setName(cpp.getCustomer() != null ? cpp
									.getCustomer().getName() : null);
					issuePaymentTransaction.setMemo(cpp.getMemo());
					issuePaymentTransaction.setAmount(cpp.getTotal());
					issuePaymentTransaction.setPaymentMethod(cpp
							.getPaymentMethod());

					issuePaymentTransactionsList.add(issuePaymentTransaction);

				}
			}

			if (issuePaymentTransactionsList != null) {
				return new ArrayList<IssuePaymentTransactionsList>(
						issuePaymentTransactionsList);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<PaymentsList> getLatestVendorPayments(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestVendorPayments")
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<PaymentsList> queryResult = new ArrayList<PaymentsList>();
				while ((iterator).hasNext()) {

					PaymentsList vendorPaymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					vendorPaymentsList
							.setTransactionId((object[0] == null ? null
									: ((Long) object[0])));
					vendorPaymentsList.setType((Integer) object[1]);
					vendorPaymentsList.setPaymentDate(new ClientFinanceDate(
							(Long) object[2]));
					vendorPaymentsList
							.setPaymentNumber((object[3] == null ? null
									: ((String) object[3])));
					vendorPaymentsList.setStatus((Integer) object[4]);
					vendorPaymentsList.setIssuedDate(new ClientFinanceDate(
							(Long) object[5]));
					vendorPaymentsList.setName((String) object[6]);
					vendorPaymentsList.setPaymentMethodName((String) object[7]);
					vendorPaymentsList.setAmountPaid((Double) object[8]);

					queryResult.add(vendorPaymentsList);
				}
				return new ArrayList<PaymentsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<BillsList> getLatestBills(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestBills").setParameter(
					"companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<BillsList> queryResult = new ArrayList<BillsList>();
				while ((iterator).hasNext()) {

					BillsList billsList = new BillsList();
					object = (Object[]) iterator.next();

					billsList.setTransactionId((Long) object[0]);
					billsList.setType((Integer) object[1]);
					billsList
							.setDueDate(new ClientFinanceDate((Long) object[2]));
					billsList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					billsList.setVendorName((String) object[4]);
					billsList.setOriginalAmount((Double) object[5]);
					billsList.setBalance((Double) object[6]);

					queryResult.add(billsList);
				}
				return new ArrayList<BillsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			final long vendorId, long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery(
							"getEntry.by.vendorId.creditand.balanceDue.orderbyid")
					.setParameter("id", vendorId)
					.setParameter("company", company);

			List<JournalEntry> openingBalanceEntries = query.list();

			List<PayBillTransactionList> queryResult = new ArrayList<PayBillTransactionList>();

			for (JournalEntry je : openingBalanceEntries) {
				PayBillTransactionList payBillTransactionList = new PayBillTransactionList();
				payBillTransactionList.setTransactionId(je.getID());
				payBillTransactionList.setType(je.getType());
				payBillTransactionList.setDueDate(new ClientFinanceDate(je
						.getDate().getDate()));
				payBillTransactionList.setBillNumber(je.getNumber());
				payBillTransactionList.setOriginalAmount(je.getDebitTotal());
				payBillTransactionList.setAmountDue(je.getBalanceDue());
				payBillTransactionList.setDiscountDate(new ClientFinanceDate(je
						.getDate().getDate()));
				payBillTransactionList.setVendorName(je.getEntry().get(1)
						.getVendor().getName());
				queryResult.add(payBillTransactionList);
			}

			query = session
					.getNamedQuery("getPayBillTransactionsListForVendor")
					.setParameter("vendorId", vendorId)
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				while ((iterator).hasNext()) {

					PayBillTransactionList payBillTransactionList = new PayBillTransactionList();
					object = (Object[]) iterator.next();

					payBillTransactionList.setTransactionId((Long) object[0]);
					payBillTransactionList.setType((Integer) object[1]);
					payBillTransactionList.setDueDate(object[2] == null ? null
							: new ClientFinanceDate((Long) object[2]));
					payBillTransactionList.setVendorName((String) object[3]);
					payBillTransactionList
							.setBillNumber((object[4] == null ? null
									: ((String) object[4])));

					payBillTransactionList
							.setOriginalAmount((Double) object[5]);
					payBillTransactionList.setAmountDue((Double) object[6]);
					// payBillTransactionList.setPayment((Double) object[7]);
					payBillTransactionList.setPaymentMethod((String) object[8]);
					payBillTransactionList
							.setDiscountDate(object[9] == null ? null
									: new ClientFinanceDate((Long) object[9]));
					queryResult.add(payBillTransactionList);
				}
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
			return new ArrayList<PayBillTransactionList>(queryResult);
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public String getNextVendorNumber(long companyId) throws DAOException {
		return NumberUtils.getNextAutoVendorNumber(getCompany(companyId));
		// return NumberUtils.getNextVendorNumber();
	}

	public ArrayList<CreditsAndPayments> getVendorCreditsAndPayments(
			long vendor, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("getcreditandPayments.by.Payieeid.and.balance")
				.setParameter("id", vendor).setEntity("company", company);
		List<CreditsAndPayments> list = query.list();

		// if (list != null) {
		return new ArrayList<CreditsAndPayments>(list);
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));

	}

	public ArrayList<PaymentsList> getVendorPaymentsList(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getVendorPaymentsList")
					.setParameter("companyId", companyId);
			;
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				ArrayList<PaymentsList> queryResult = new ArrayList<PaymentsList>();
				while ((iterator).hasNext()) {

					PaymentsList vendorPaymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					String name = (String) object[6];
					if (name != null) {

						vendorPaymentsList
								.setTransactionId((object[0] == null ? null
										: ((Long) object[0])));
						vendorPaymentsList.setType((Integer) object[1]);
						vendorPaymentsList
								.setPaymentDate(new ClientFinanceDate(
										(Long) object[2]));
						vendorPaymentsList
								.setPaymentNumber((object[3] == null ? null
										: ((String) object[3])));
						vendorPaymentsList.setStatus((Integer) object[4]);
						vendorPaymentsList.setIssuedDate(new ClientFinanceDate(
								(Long) object[5]));
						vendorPaymentsList.setName(name);
						vendorPaymentsList
								.setPaymentMethodName((String) object[7]);
						vendorPaymentsList.setAmountPaid((Double) object[8]);
						vendorPaymentsList
								.setPayBillType((Integer) object[9] != null ? (Integer) object[9]
										: 0);
						vendorPaymentsList
								.setVoided(object[10] != null ? (Boolean) object[10]
										: false);

						queryResult.add(vendorPaymentsList);
					}
				}
				return new ArrayList<PaymentsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public Client1099Form get1099InformationByVendor(long vendorId,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		Client1099Form client1099Form = new Client1099Form();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery("get.selected.vendors.enterbills.list.by.id")
				.setParameter("vendorId", vendorId)
				.setEntity("company", company);
		ArrayList<EnterBill> list = (ArrayList<EnterBill>) query.list();

		for (EnterBill enterBill : list) {
			double totalPayments = client1099Form.getTotalAllPayments();

			double payments = enterBill.getPayments();
			totalPayments += payments;
			for (TransactionItem ti : enterBill.getTransactionItems()) {
				double amount = ti.getEffectiveAmount();
				if (payments >= amount) {
					// This is paid.
					int boxNumber = ti.getEffectingAccount().getBoxNumber();
					if (boxNumber > 0) {
						double box = client1099Form.getBox(boxNumber);
						box += amount;
						client1099Form.setBox(boxNumber, box);
					}
				}
			}
			client1099Form.setTotalAllPayments(totalPayments);
		}

		for (java.util.Map.Entry<Integer, Integer> box : boxThresholds
				.entrySet()) {
			Integer boxNum = box.getKey();
			Integer boxThreshold = box.getValue();
			if (client1099Form.getBox(boxNum) < boxThreshold) {
				client1099Form.setBox(boxNum, 0);
			}
		}

		return client1099Form;
	}

	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAmountsDueToVendor")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<AmountsDueToVendor> queryResult = new ArrayList<AmountsDueToVendor>();
		while ((iterator).hasNext()) {

			AmountsDueToVendor amountsDueToVendor = new AmountsDueToVendor();
			object = (Object[]) iterator.next();

			amountsDueToVendor.setName((String) object[0]);
			amountsDueToVendor.setFileAs((String) object[1]);
			amountsDueToVendor.setIsActive(object[2] == null ? true
					: ((Boolean) object[2]).booleanValue());
			amountsDueToVendor.setWebPageAddress((String) object[3]);
			amountsDueToVendor.setTaxAgencySince(object[4] == null ? null
					: new ClientFinanceDate((Long) object[4]));
			amountsDueToVendor.setAmount(object[5] == null ? 0
					: ((Double) object[5]).doubleValue());
			amountsDueToVendor.setAddress((String) object[6]);
			amountsDueToVendor.setCity((String) object[7]);
			amountsDueToVendor.setState((String) object[8]);
			amountsDueToVendor.setZip((String) object[9]);
			amountsDueToVendor.setPhone((String) object[10]);
			amountsDueToVendor.setFax((String) object[11]);
			amountsDueToVendor.setEmail((String) object[12]);
			amountsDueToVendor.setType(object[13] == null ? 0
					: ((Integer) object[13]).intValue());

			queryResult.add(amountsDueToVendor);
		}
		return new ArrayList<AmountsDueToVendor>(queryResult);
	}

	public ArrayList<Client1099Form> get1099Vendors(int selected, long companyId)
			throws AccounterException {
		ArrayList<EnterBill> list = new ArrayList<EnterBill>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		org.hibernate.Transaction transaction = session.beginTransaction();
		HashMap<Vendor, Client1099Form> map = new HashMap<Vendor, Client1099Form>();

		if (selected == 0 || selected == 1) {
			Query query = session.getNamedQuery(
					"get.selected.vendors.enterbills.list").setEntity(
					"company", company);
			list = (ArrayList<EnterBill>) query.list();
		} else {
			Query query = session.getNamedQuery(
					"get.notselected.vendors.enterbills.list").setEntity(
					"company", company);
			list = (ArrayList<EnterBill>) query.list();
		}

		Client1099Form client1099Form = null;

		for (EnterBill enterBill : list) {

			Vendor vendor = enterBill.getVendor();

			client1099Form = map.get(vendor);
			if (client1099Form == null) {
				client1099Form = new Client1099Form();
			}

			double totalPayments = client1099Form.getTotalAllPayments();

			double payments = enterBill.getPayments();
			totalPayments += payments;
			for (TransactionItem ti : enterBill.getTransactionItems()) {
				double amount = ti.getEffectiveAmount();
				if (payments >= amount) {
					// This is paid.
					int boxNumber = ti.getEffectingAccount().getBoxNumber();
					if (boxNumber > 0) {
						double box = client1099Form.getBox(boxNumber);
						box += amount;
						client1099Form.setBox(boxNumber, box);
					}
				}
			}
			client1099Form.setTotalAllPayments(totalPayments);

			map.put(vendor, client1099Form);
		}

		ArrayList<Client1099Form> aboveThresholdList = new ArrayList<Client1099Form>();
		ArrayList<Client1099Form> belowThresholdList = new ArrayList<Client1099Form>();
		ArrayList<Client1099Form> non1099List = new ArrayList<Client1099Form>();

		for (java.util.Map.Entry<Vendor, Client1099Form> element : map
				.entrySet()) {
			Vendor key = element.getKey();
			Client1099Form value = element.getValue();

			boolean flag = false;

			for (java.util.Map.Entry<Integer, Integer> box : boxThresholds
					.entrySet()) {
				Integer boxNum = box.getKey();
				Integer boxThreshold = box.getValue();
				if (value.getBox(boxNum) < boxThreshold) {
					value.setBox(boxNum, 0);
				} else if (value.getBox(boxNum) > boxThreshold) {
					flag = true;
				}
			}

			ClientVendor clientVendor = new ClientConvertUtil().toClientObject(
					key, ClientVendor.class);
			value.setVendor(clientVendor);
			if (selected == 0 || selected == 1) {
				if (flag) {
					aboveThresholdList.add(value);
				} else {
					belowThresholdList.add(value);
				}
			} else {
				non1099List.add(value);
			}
		}
		if (selected == 0) {
			return aboveThresholdList;
		} else if (selected == 1) {
			return belowThresholdList;
		} else {
			return non1099List;
		}
	}

	public ArrayList<MISC1099TransactionDetail> getPaybillsByVendorAndBoxNumber(
			FinanceDate startDate, FinanceDate endDate, long vendorId,
			int boxNo, long companyId) {

		ArrayList<EnterBill> list = new ArrayList<EnterBill>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		org.hibernate.Transaction begintransaction = session.beginTransaction();

		Query query = session
				.getNamedQuery("get.selected.vendors.enterbills.list.by.id")
				.setLong("vendorId", vendorId).setEntity("company", company);
		;
		list = (ArrayList<EnterBill>) query.list();

		ArrayList<MISC1099TransactionDetail> arraylist = new ArrayList<MISC1099TransactionDetail>();

		for (EnterBill enterBill : list) {

			Vendor vendor = enterBill.getVendor();

			double payments = enterBill.getPayments();
			for (TransactionItem ti : enterBill.getTransactionItems()) {
				Transaction transaction = ti.getTransaction();
				long date = transaction.getDate().getDate();
				if (startDate.getDate() <= date && date <= endDate.getDate()) {
					MISC1099TransactionDetail misc1099TransactionDetail = null;
					double amount = ti.getEffectiveAmount();
					Account account = ti.getEffectingAccount();
					int boxNumber = account.getBoxNumber();
					if (boxNo == Client1099Form.TOATAL_ALL_PAYMENTS) {
						if (payments >= amount) {

							misc1099TransactionDetail = new MISC1099TransactionDetail();
							misc1099TransactionDetail.setAccount(account
									.getName());
							misc1099TransactionDetail.setAmount(amount);
							misc1099TransactionDetail.setBox1099(account
									.getBoxNumber());
							misc1099TransactionDetail
									.setDate(new ClientFinanceDate(date));
							misc1099TransactionDetail.setMemo(transaction
									.getMemo());
							misc1099TransactionDetail.setName(vendor.getName());
							misc1099TransactionDetail.setNumber(account
									.getNumber());
							misc1099TransactionDetail
									.setTransactionId(transaction.getID());
							misc1099TransactionDetail.setType(transaction
									.getType());
						}
					} else if (boxNo == Client1099Form.TOTAL_1099_PAYMENTS) {
						if (boxNumber != 0) {
							if (payments >= amount
									&& payments >= boxThresholds.get(boxNumber)) {

								misc1099TransactionDetail = new MISC1099TransactionDetail();
								misc1099TransactionDetail.setAccount(account
										.getName());
								misc1099TransactionDetail.setAmount(amount);
								misc1099TransactionDetail.setBox1099(account
										.getBoxNumber());
								misc1099TransactionDetail
										.setDate(new ClientFinanceDate(date));
								misc1099TransactionDetail.setMemo(transaction
										.getMemo());
								misc1099TransactionDetail.setName(vendor
										.getName());
								misc1099TransactionDetail.setNumber(account
										.getNumber());
								misc1099TransactionDetail
										.setTransactionId(transaction.getID());
								misc1099TransactionDetail.setType(transaction
										.getType());
							}
						}
					} else {
						if (boxNumber == boxNo) {
							if (payments >= amount
									&& payments >= boxThresholds.get(boxNumber)) {

								misc1099TransactionDetail = new MISC1099TransactionDetail();
								misc1099TransactionDetail.setAccount(account
										.getName());
								misc1099TransactionDetail.setAmount(amount);
								misc1099TransactionDetail.setBox1099(account
										.getBoxNumber());
								misc1099TransactionDetail
										.setDate(new ClientFinanceDate(date));
								misc1099TransactionDetail.setMemo(transaction
										.getMemo());
								misc1099TransactionDetail.setName(vendor
										.getName());
								misc1099TransactionDetail.setNumber(account
										.getNumber());
								misc1099TransactionDetail
										.setTransactionId(transaction.getID());
								misc1099TransactionDetail.setType(transaction
										.getType());
							}
						}
					}
					if (misc1099TransactionDetail != null) {
						arraylist.add(misc1099TransactionDetail);
					}
				}
			}

		}

		return arraylist;
	}

	public void mergeVendor(ClientVendor fromClientVendor,
			ClientVendor toClientVendor, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		org.hibernate.Transaction tx = session.beginTransaction();
		double mergeBalance = fromClientVendor.getBalance()
				+ toClientVendor.getBalance();

		session.getNamedQuery(
				"update.mergeVendor.Payee.mergeoldbalance.tonewbalance")
				.setLong("id", toClientVendor.getID())
				.setBoolean("status", fromClientVendor.isActive())
				.setDouble("balance", mergeBalance)
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.PurchaseOrder.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.CashPurchase.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.CreditCardCharge.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.EnterBill.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.ItemReceipt.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.PayBill.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery(
				"update.mergeVendor.transactionMakeDeposit.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.vendorCreditMemo.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.writeCheck.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.Entry.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("update.mergeVendor.Item.old.tonew")
				.setLong("fromID", fromClientVendor.getID())
				.setLong("toID", toClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("delete.vendorentry.old")
				.setLong("from", fromClientVendor.getID())
				.setEntity("company", company).executeUpdate();

		ServerConvertUtil convertUtil = new ServerConvertUtil();
		Vendor vendor = new Vendor();

		vendor = convertUtil.toServerObject(vendor, fromClientVendor, session);
		session.delete(vendor);
		tx.commit();

	}

	public ArrayList<Vendor> getTransactionHistoryVendors(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransactionHistoryVendors")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<Vendor> queryResult = new ArrayList<Vendor>();
		while ((iterator).hasNext()) {

			Vendor vendor = new Vendor();
			object = (Object[]) iterator.next();
			vendor.setName((String) object[1]);
			queryResult.add(vendor);
		}
		return new ArrayList<Vendor>(queryResult);
	}

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		cal.setTime(startDate.getAsDateObject());
		cal.add(Calendar.DAY_OF_MONTH, -1);

		String end = cal.get(Calendar.YEAR) + "-";
		end += ((((cal.get(Calendar.MONTH) + 1) + "").length() == 1) ? "0"
				+ cal.get(Calendar.MONTH) : cal.get(Calendar.MONTH) + 1)
				+ "-";
		end += (((cal.get(Calendar.DAY_OF_MONTH)) + "").length() == 1) ? "0"
				+ cal.get(Calendar.DAY_OF_MONTH) : cal
				.get(Calendar.DAY_OF_MONTH);

		Query query = session.getNamedQuery("getVendorTransactionHistory")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start)
				.setParameter("end", new ClientFinanceDate(end).getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		Set<String> payee = new HashSet<String>();
		Map<String, TransactionHistory> openingBalnaceEntries = new HashMap<String, TransactionHistory>();
		List<TransactionHistory> queryResult = new ArrayList<TransactionHistory>();
		while ((iterator).hasNext()) {

			TransactionHistory transactionHistory = new TransactionHistory();
			object = (Object[]) iterator.next();

			transactionHistory.setName((String) object[0]);
			transactionHistory
					.setType((object[1] == null || ((String) object[7] != null ? (String) object[7]
							: "")
							.equals(AccounterServerConstants.MEMO_OPENING_BALANCE)) ? 0
							: ((Integer) object[1]).intValue());
			transactionHistory.setDate(new ClientFinanceDate(
					((BigInteger) object[2]).longValue()));
			transactionHistory.setNumber((String) object[3]);
			transactionHistory.setInvoicedAmount(object[4] == null ? 0
					: ((Double) object[4]).doubleValue());
			transactionHistory.setPaidAmount(object[5] == null ? 0
					: ((Double) object[5]).doubleValue());
			transactionHistory.setDiscount(object[6] == null ? 0
					: ((Double) object[6]).doubleValue());

			/*
			 * Clob cl = (Clob) object[7]; if (cl == null) {
			 * 
			 * transactionHistory.setMemo("");
			 * 
			 * } else {
			 * 
			 * StringBuffer strOut = new StringBuffer(); String aux; try {
			 * BufferedReader br = new BufferedReader(cl .getCharacterStream());
			 * while ((aux = br.readLine()) != null) strOut.append(aux);
			 * transactionHistory.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */

			transactionHistory.setMemo((String) object[7]);
			transactionHistory
					.setDueDate(((BigInteger) object[8]) == null ? null
							: new ClientFinanceDate(((BigInteger) object[8])
									.longValue()));
			transactionHistory.setPaymentTerm((String) object[9]);
			transactionHistory.setDebit(object[10] == null ? 0
					: ((Double) object[10]).doubleValue());
			transactionHistory.setCredit(object[11] == null ? 0
					: ((Double) object[11]).doubleValue());
			transactionHistory.setIsVoid(object[12] == null ? true
					: ((Boolean) object[12]).booleanValue());
			transactionHistory.setReference((String) object[13]);
			transactionHistory.setTransactionId(((BigInteger) object[14])
					.longValue());
			transactionHistory
					.setBeginningBalance((object[15] != null ? (((Double) object[15])
							.doubleValue()) : 0.0));
			transactionHistory
					.setStatus((object[16] != null) ? (Integer) object[16] : 0);

			Transaction t = (Transaction) getServerObjectForid(
					AccounterCoreType.TRANSACTION,
					transactionHistory.getTransactionId());

			// if (transactionHistory.getType() ==
			// Transaction.TYPE_CREDIT_CARD_EXPENSE)
			transactionHistory.setName(t.getInvolvedPayee().getName());

			Account account = t.getEffectingAccount();
			if (account == null) {
				account = t.getInvolvedPayee().getAccount();
			}
			transactionHistory.setAccount(account.getName());

			if (transactionHistory.getType() == 0) {

				openingBalnaceEntries.put(transactionHistory.getName(),
						transactionHistory);
			} else {
				queryResult.add(transactionHistory);
			}
			payee.add(transactionHistory.getName());

		}

		mergeOpeningBalanceEntries(queryResult, payee, openingBalnaceEntries);

		// return prepareEntriesForVoid(queryResult);
		return new ArrayList<TransactionHistory>(queryResult);

	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPurchasesByVendorDetail")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		return createPurchasesByVendorDetail(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	private ArrayList<SalesByCustomerDetail> createPurchasesByVendorDetail(
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
			salesByCustomerDetail.setPaymentTermName((String) object[4]);
			salesByCustomerDetail
					.setDueDate(((BigInteger) object[5]) == null ? null
							: new ClientFinanceDate(((BigInteger) object[5])
									.longValue()));
			salesByCustomerDetail.setItemName((String) object[6]);
			salesByCustomerDetail.setDescription((String) object[7]);
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
			salesByCustomerDetail.setTransactionId(((BigInteger) object[14])
					.longValue());
			queryResult.add(salesByCustomerDetail);
		}

		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPurchasesByVendorSummary")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();

			salesByCustomerDetail.setName((String) object[0]);
			salesByCustomerDetail.setGroupName((String) object[1]);
			salesByCustomerDetail.setAmount(object[2] == null ? 0
					: ((Double) object[2]).doubleValue());

			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session
				.getNamedQuery("getPurchasesByVendorDetailForParticularVendor")
				.setParameter("companyId", companyId)
				.setParameter("vendorName", vendorName)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return createPurchasesByVendorDetail(new ArrayList<SalesByCustomerDetail>(
				l));

	}
}
