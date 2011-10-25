package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class CustomerManager extends Manager {

	public String getNextCustomerNumber(long companyId) {
		return NumberUtils.getNextAutoCustomerNumber(getCompany(companyId));
		// return NumberUtils.getNextCustomerNumber();
	}

	public ArrayList<CustomerRefundsList> getCustomerRefundsList(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<CustomerRefundsList> customerRefundsList = new ArrayList<CustomerRefundsList>();
			Query query = session.getNamedQuery("getCustomerRefund").setEntity(
					"company", company);
			List list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {
					CustomerRefundsList customerRefund = new CustomerRefundsList();
					CustomerRefund cr = (CustomerRefund) i.next();
					customerRefund.setTransactionId(cr.getID());
					customerRefund.setType(cr.getType());
					customerRefund.setPaymentDate(new ClientFinanceDate(cr
							.getDate().getDate()));
					customerRefund.setIssueDate(new ClientFinanceDate(cr
							.getDate().getDate()));
					customerRefund.setPaymentNumber(cr.getNumber());
					customerRefund.setStatus(cr.getStatus());
					customerRefund.setName((cr.getPayTo() != null) ? cr
							.getPayTo().getName() : null);
					customerRefund
							.setPaymentMethod((cr.getPaymentMethod() != null) ? cr
									.getPaymentMethod() : null);
					customerRefund.setAmountPaid(cr.getTotal());
					customerRefund.setVoided(cr.isVoid());

					customerRefundsList.add(customerRefund);
				}
			}
			query = session.getNamedQuery("getWriteCheck.by.payToType")
					.setParameter("type", WriteCheck.TYPE_CUSTOMER)
					.setEntity("company", company);
			list = query.list();

			if (list != null) {
				Iterator i = list.iterator();
				while (i.hasNext()) {

					CustomerRefundsList customerRefund = new CustomerRefundsList();
					WriteCheck wc = (WriteCheck) i.next();
					customerRefund.setTransactionId(wc.getID());
					customerRefund.setType(wc.getType());
					customerRefund.setPaymentDate(new ClientFinanceDate(wc
							.getDate().getDate()));
					customerRefund.setIssueDate(null);
					customerRefund.setPaymentNumber(wc.getNumber());
					customerRefund.setStatus(wc.getStatus());
					customerRefund.setName((wc.getCustomer() != null) ? wc
							.getCustomer().getName()
							: ((wc.getVendor() != null) ? wc.getVendor()
									.getName()
									: (wc.getTaxAgency() != null ? wc
											.getTaxAgency().getName() : null)));
					customerRefund.setPaymentMethod(null);
					customerRefund.setAmountPaid(wc.getAmount());
					customerRefund.setVoided(wc.isVoid());
					customerRefundsList.add(customerRefund);

				}
			}

			if (customerRefundsList != null) {
				return new ArrayList<CustomerRefundsList>(customerRefundsList);
			} else
				throw new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null);
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Estimate> getLatestQuotes(long companyId)
			throws DAOException {
		// SELECT E1.* FROM ESTIMATE E1 WHERE 10>(SELECT COUNT(*) FROM
		// TRANSACTION E2 WHERE E1.ID<E2.ID)

		try {
			Session session = HibernateUtil.getCurrentSession();
			// FIXME::: query optimization
			Query query = session.getNamedQuery("getLatestQuotes")
					.setParameter("companyId", companyId);
			List list2 = query.list();
			Object object[] = null;

			Iterator iterator = list2.iterator();
			List<Estimate> list = new ArrayList<Estimate>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				Estimate estimate = new Estimate();
				// TODO :: change the query
				// estimate.setID((object[0] == null ? null : ((Long)
				// object[0])));
				estimate.setDate(new FinanceDate((Long) (object[1])));
				estimate.setCustomer(object[2] != null ? (Customer) session
						.get(Customer.class, ((Long) object[2])) : null);
				estimate.setSalesPerson(object[3] != null ? (SalesPerson) session
						.get(SalesPerson.class, ((Long) object[3])) : null);
				estimate.setTotal((Double) object[4]);
				list.add(estimate);
			}
			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<CustomerRefund> getLatestCustomerRefunds(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestCustomerRefunds")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<CustomerRefund> list = new ArrayList<CustomerRefund>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				CustomerRefund customerRefund = new CustomerRefund();
				// customerRefund.setID((object[0] == null ? null
				// : ((Long) object[0])));
				customerRefund.setDate(new FinanceDate((Long) object[1]));
				customerRefund.setPayTo(object[2] != null ? (Customer) session
						.get(Customer.class, ((Long) object[2])) : null);
				customerRefund.setTotal((Double) object[3]);
				customerRefund.setBalanceDue((Double) object[3]);
				// customerRefund.setID((object[4] == null ? null
				// : ((String) object[4])));

				list.add(customerRefund);
			}
			if (list != null) {
				return new ArrayList<CustomerRefund>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Customer> getLatestCustomers(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestCustomers")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object[] object = null;
			Iterator iterator = list2.iterator();
			List<Customer> list = new ArrayList<Customer>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				Customer customer = new Customer();
				// customer.setID((object[0] == null ? null : ((Long)
				// object[0])));
				customer.setName((String) object[1]);
				customer.setDate(new FinanceDate((Long) object[2]));
				// customer.setID((String) object[3]);
				list.add(customer);
			}
			if (list != null) {
				return new ArrayList<Customer>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getEstimatesAndSalesOrdersList")
				.setParameter("customerId", customerId)
				.setParameter("companyId", companyId);

		List list = query.list();
		List<EstimatesAndSalesOrdersList> esl = new ArrayList<EstimatesAndSalesOrdersList>();

		for (int i = 0; i < list.size(); i++) {

			Object[] obj = (Object[]) list.get(i);
			// for (int j = 0; j < obj.length; j++)
			{
				EstimatesAndSalesOrdersList el = new EstimatesAndSalesOrdersList();
				el.setTransactionId(((Long) obj[0]).longValue());
				el.setType(((Integer) obj[1]).intValue());
				el.setTransactionNumber(((String) obj[2]));
				el.setTotal(((Double) obj[3]).doubleValue());
				el.setDate(new ClientFinanceDate((Long) obj[4]));
				el.setCustomerName((String) obj[5]);
				el.setRemainingTotal(((Double) obj[6]).doubleValue());
				esl.add(el);
			}
		}

		return new ArrayList<EstimatesAndSalesOrdersList>(esl);
	}

	public ArrayList<Estimate> getEstimates(long companyId, int type)
			throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();

			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getEstimate")
					.setEntity("company", company)
					.setParameter("estimateType", type);
			List<Estimate> list = query.list();

			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<ReceivePayment> getLatestReceivePayments(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLatestReceivePayments")
				.setParameter("companyId", companyId);
		List list2 = query.list();

		Object object[] = null;
		Iterator iterator = list2.iterator();
		List<ReceivePayment> list = new ArrayList<ReceivePayment>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			ReceivePayment receivePayment = new ReceivePayment();
			// receivePayment
			// .setID((object[0] == null ? null : ((Long) object[0])));
			receivePayment.setDate(new FinanceDate((Long) object[1]));
			receivePayment.setCustomer(object[2] != null ? (Customer) session
					.get(Customer.class, ((Long) object[2])) : null);
			receivePayment.setAmount((Double) object[3]);
			// receivePayment.setID((String) object[4]);

			list.add(receivePayment);
		}
		if (list != null) {
			return new ArrayList<ReceivePayment>(list);
		} else
			throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
					null));
	}

	public ArrayList<ReceivePaymentsList> getReceivePaymentsList(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getReceivePaymentsList")
					.setParameter("companyId", companyId);
			;
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				ArrayList<ReceivePaymentsList> queryResult = new ArrayList<ReceivePaymentsList>();
				while ((iterator).hasNext()) {

					ReceivePaymentsList receivePaymentsList = new ReceivePaymentsList();
					object = (Object[]) iterator.next();

					receivePaymentsList
							.setTransactionId((object[0] == null ? null
									: ((Long) object[0])));
					receivePaymentsList.setType((Integer) object[1]);
					receivePaymentsList.setPaymentDate(new ClientFinanceDate(
							object[2] == null ? null : ((Long) object[2])));
					receivePaymentsList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					receivePaymentsList.setCustomerName((String) object[4]);
					receivePaymentsList
							.setPaymentMethodName((String) object[5]);
					receivePaymentsList.setAmountPaid((Double) object[6]);
					receivePaymentsList.setVoided((Boolean) object[7]);
					receivePaymentsList.setStatus((Integer) object[8]);
					receivePaymentsList.setCheckNumber((String) object[9]);

					queryResult.add(receivePaymentsList);
				}
				return queryResult;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<PaymentsList> getLatestPayments(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestPayments")
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<PaymentsList> queryResult = new ArrayList<PaymentsList>();
				while ((iterator).hasNext()) {

					PaymentsList paymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					paymentsList.setTransactionId((object[0] == null ? null
							: ((Long) object[0])));
					paymentsList.setType((Integer) object[1]);
					paymentsList.setPaymentDate(new ClientFinanceDate(
							(Long) object[2]));
					paymentsList.setPaymentNumber((object[3] == null ? null
							: ((String) object[3])));
					paymentsList.setStatus((Integer) object[4]);
					paymentsList.setIssuedDate(new ClientFinanceDate(
							(Long) object[5]));
					paymentsList.setName((String) object[6]);
					paymentsList.setPaymentMethodName((String) object[7]);
					paymentsList.setAmountPaid((Double) object[8]);

					queryResult.add(paymentsList);
				}
				return new ArrayList<PaymentsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<PaymentsList> getPaymentsList(long companyId)
			throws DAOException {
		List<PaymentsList> queryResult = new ArrayList<PaymentsList>();
		try {
			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getPaymentsList")
					.setParameter("companyId", companyId);
			;
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					PaymentsList paymentsList = new PaymentsList();
					object = (Object[]) iterator.next();

					String name = (String) object[6];
					// if (name != null) {
					paymentsList.setTransactionId((object[0] == null ? 0
							: ((Long) object[0])));
					paymentsList.setType((Integer) object[1]);
					paymentsList.setPaymentDate((object[2] == null ? null
							: (new ClientFinanceDate(((Long) object[2])))));
					paymentsList.setPaymentNumber((object[3] == null ? null
							: ((String) object[3])));
					paymentsList.setStatus((Integer) object[4]);
					paymentsList.setIssuedDate(new ClientFinanceDate(
							(Long) object[5]));
					paymentsList.setName(name);
					paymentsList.setPaymentMethodName((String) object[7]);
					paymentsList.setAmountPaid((Double) object[8]);
					paymentsList.setVoided((Boolean) object[9]);
					paymentsList
							.setPayBillType(object[10] != null ? (Integer) object[10]
									: 0);
					paymentsList.setCheckNumber((String) object[11]);
					queryResult.add(paymentsList);
					// }
				}
				return new ArrayList<PaymentsList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Estimate> getEstimates(long customer, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery("getEstimate.by.check.id.status")
					.setParameter("id", customer).setEntity("company", company);
			List<Estimate> list = query.list();

			if (list != null) {
				return new ArrayList<Estimate>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<CreditsAndPayments> getCustomerCreditsAndPayments(
			long customer, long companyId) throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery(
							"getCreditsAndPayments.by.check.payeeidandbalanceid")
					.setParameter("id", customer).setEntity("company", company);
			List list = query.list();

			if (list != null) {
				return new ArrayList<CreditsAndPayments>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public void mergeCustomer(ClientCustomer fromClientCustomer,
			ClientCustomer toClientCustomer, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		double mergeBalance = fromClientCustomer.getBalance()
				+ toClientCustomer.getBalance();
		Company company = getCompany(companyId);
		// Updating
		try {
			session.getNamedQuery(
					"update.merge.Payee.mergeoldbalance.tonewbalance")
					.setLong("id", toClientCustomer.getID())
					.setDouble("balance", mergeBalance)
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.invoice.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.cashsale.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.customercreditmemo.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.salesOrder.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.Entry.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setString("memo", toClientCustomer.getName())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.CustomerPrePayment.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.CustomerRefund.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.ReceivePayment.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.Estimate.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.transactionMakeDeposit.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.writeCheck.old.tonew")
					.setLong("fromID", fromClientCustomer.getID())
					.setLong("toID", toClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("delete.entry.old")
					.setLong("from", fromClientCustomer.getID())
					.setEntity("company", company).executeUpdate();

			ServerConvertUtil convertUtil = new ServerConvertUtil();
			Customer customer = new Customer();

			customer = convertUtil.toServerObject(customer, fromClientCustomer,
					session);
			session.delete(customer);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}

	}

	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("getMostProfitableCustomers")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<MostProfitableCustomers> queryResult = new ArrayList<MostProfitableCustomers>();
		while ((iterator).hasNext()) {

			MostProfitableCustomers mostProfitableCustomers = new MostProfitableCustomers();
			object = (Object[]) iterator.next();

			mostProfitableCustomers.setCustomer((String) object[0]);
			mostProfitableCustomers.setInvoicedAmount(object[1] == null ? 0
					: ((Double) object[1]).doubleValue());
			mostProfitableCustomers.setStandardCost(object[2] == null ? 0
					: ((Double) object[2]).doubleValue());
			mostProfitableCustomers.setMargin(object[3] == null ? 0
					: ((Double) object[3]).doubleValue());
			mostProfitableCustomers.setCustomerGroup((String) object[4]);
			mostProfitableCustomers.setFileAs((String) object[5]);
			if (mostProfitableCustomers.getInvoicedAmount() == 0.0)
				mostProfitableCustomers.setMarginPercentage(0.0);
			else {
				mostProfitableCustomers
						.setMarginPercentage(mostProfitableCustomers
								.getMargin()
								/ mostProfitableCustomers.getInvoicedAmount()
								* 100);
			}
			mostProfitableCustomers.setCost(mostProfitableCustomers
					.getStandardCost()
					+ mostProfitableCustomers.getBilledCost());

			queryResult.add(mostProfitableCustomers);
		}

		query = session.getNamedQuery("getEntry.from.journalEntryType")
				.setParameter("company", company);

		List<JournalEntry> nonInvoicedLines = (List<JournalEntry>) query.list();

		for (MostProfitableCustomers mpc : queryResult) {
			for (JournalEntry je : nonInvoicedLines) {
				Entry e1 = je.getEntry().get(0);
				Entry e2 = je.getEntry().get(1);
				if (e1.getType() == Entry.TYPE_CUSTOMER
						&& e1.getCustomer().getName().equals(mpc.getCustomer())) {
					mpc.setBilledCost(mpc.getBilledCost()
							+ (!DecimalUtil.isEquals(e2.getDebit(), 0.0) ? -1
									* e2.getDebit() : e2.getCredit()));
				} else if (e2.getType() == Entry.TYPE_CUSTOMER
						&& e2.getCustomer().getName().equals(mpc.getCustomer())) {
					mpc.setBilledCost(mpc.getBilledCost()
							+ (!DecimalUtil.isEquals(e1.getDebit(), 0.0) ? -1
									* e1.getDebit() : e1.getCredit()));
				}

			}

		}

		return new ArrayList<MostProfitableCustomers>(queryResult);
	}

	public ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		List<PayeeStatementsList> result = new ArrayList<PayeeStatementsList>();

		Query query = session.getNamedQuery("getCustomerPreviousBalance");
		query.setParameter("customerId", customer);
		query.setParameter("companyId", companyId);
		query.setParameter("fromDate", fromDate);
		Object uniqueResult = query.uniqueResult();
		PayeeStatementsList ob = new PayeeStatementsList();
		ob.setTransactionDate(new ClientFinanceDate(fromDate));
		ob.setTotal((Double) uniqueResult);
		result.add(ob);

		Query query1 = session.getNamedQuery("getCustomerStatement");
		query1.setParameter("customerId", customer);
		query1.setParameter("companyId", companyId);
		query1.setParameter("fromDate", fromDate);
		query1.setParameter("toDate", toDate);

		List l = query1.list();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			PayeeStatementsList record = new PayeeStatementsList();
			record.setTransactionId((Long) object[0]);
			record.setTransactionDate(new ClientFinanceDate((Long) object[1]));
			record.setTransactiontype((Integer) object[2]);
			record.setTransactionNumber((String) object[3]);
			record.setTotal((Double) object[4]);

			result.add(record);
		}
		return new ArrayList<PayeeStatementsList>(result);
	}

	public ArrayList<Customer> getTransactionHistoryCustomers(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransactionHistoryCustomers")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<Customer> queryResult = new ArrayList<Customer>();
		while ((iterator).hasNext()) {

			Customer customer = new Customer();
			object = (Object[]) iterator.next();
			customer.setName((String) object[1]);
			queryResult.add(customer);
		}
		return new ArrayList<Customer>(queryResult);
	}

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		ClientFinanceDate date[] = this
				.getMinimumAndMaximumTransactionDate(companyId);
		long start = date[0] != null ? date[0].getDate() : startDate.getDate();
		// Calendar cal = Calendar.getInstance();
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// try {
		// cal.setTime(startDate.getAsDateObject());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		//
		// String end = cal.get(Calendar.YEAR) + "-";
		// end += ((((cal.get(Calendar.MONTH) + 1) + "").length() == 1) ? "0"
		// + cal.get(Calendar.MONTH) : cal.get(Calendar.MONTH) + 1)
		// + "-";
		// end += (((cal.get(Calendar.DAY_OF_MONTH)) + "").length() == 1) ? "0"
		// + cal.get(Calendar.DAY_OF_MONTH) : cal
		// .get(Calendar.DAY_OF_MONTH);

		long end = date[1] != null ? date[1].getDate() : endDate.getDate();

		Query query = session.getNamedQuery("getCustomerTransactionHistory")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("start", start).setParameter("end", end);

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<TransactionHistory> queryResult = new ArrayList<TransactionHistory>();
		Set<String> payee = new HashSet<String>();
		Map<String, TransactionHistory> openingBalnaceEntries = new HashMap<String, TransactionHistory>();
		while ((iterator).hasNext()) {

			TransactionHistory transactionHistory = new TransactionHistory();
			object = (Object[]) iterator.next();

			transactionHistory.setName((String) object[0]);
			transactionHistory
					.setType((object[1] == null || ((String) object[16] != null ? (String) object[16]
							: "")
							.equals(AccounterServerConstants.MEMO_OPENING_BALANCE)) ? 0
							: ((Integer) object[1]).intValue());
			transactionHistory.setNumber((String) object[2]);

			transactionHistory.setDate(new ClientFinanceDate(
					((BigInteger) object[3]).longValue()));
			transactionHistory.setInvoicedAmount(object[4] == null ? 0
					: ((Double) object[4]).doubleValue());
			transactionHistory.setPaidAmount(object[5] == null ? 0
					: ((Double) object[5]).doubleValue());
			transactionHistory.setPaymentTerm((String) object[6]);
			transactionHistory
					.setDueDate(((BigInteger) object[7]) == null ? null
							: new ClientFinanceDate(((BigInteger) object[7])
									.longValue()));
			transactionHistory.setDebit(object[8] == null ? 0
					: ((Double) object[8]).doubleValue());
			transactionHistory.setCredit(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			transactionHistory.setDiscount(object[10] == null ? 0
					: ((Double) object[10]).doubleValue());
			transactionHistory.setWriteOff(object[11] == null ? 0
					: ((Double) object[11]).doubleValue());
			transactionHistory.setTransactionId(((BigInteger) object[12])
					.longValue());

			transactionHistory
					.setBeginningBalance((object[13] != null ? ((Double) object[13])
							.doubleValue() : 0.0));
			transactionHistory
					.setIsVoid(object[14] != null ? (Boolean) object[14]
							: false);
			transactionHistory
					.setStatus((object[15] != null) ? (Integer) object[15] : 0);
			transactionHistory.setMemo((String) object[16]);

			// Transaction t = (Transaction) getServerObjectForid(
			// AccounterCoreType.TRANSACTION,
			// transactionHistory.getTransactionId());
			// Account account = (t).getEffectingAccount() == null ?
			// t.getPayee() == null ? null
			// : t.getPayee().getAccount()
			// : t.getEffectingAccount();

			transactionHistory.setAccount((String) object[17]);

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

}
