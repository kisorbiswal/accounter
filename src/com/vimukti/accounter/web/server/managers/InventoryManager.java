package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ItemStatus;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.StockAdjustmentItem;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.TransactionsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;

public class InventoryManager extends Manager {

	private static final Object Integer = null;

	public PaginationList<InvoicesList> getInvoiceList(long companyId,
			long fromDate, long toDate, int invoicesType, int viewType,
			int start, int length) throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;
			List list;
			// FIXME :: query optimization
			Query query = session.getNamedQuery("getInvoicesList")
					.setParameter("companyId", companyId)
					.setLong("fromDate", fromDate).setLong("toDate", toDate)
					.setParameter("viewType", viewType)
					.setParameter("todayDate", new FinanceDate().getDate());

			if (invoicesType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				query = session.getNamedQuery("getCustomerCreditMemos")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType);
			} else if (invoicesType == Transaction.TYPE_INVOICE) {
				query = session.getNamedQuery("getInvoicesOnly")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType)
						.setParameter("todayDate", new FinanceDate().getDate());
			} else if (invoicesType == Transaction.TYPE_CASH_SALES) {
				query = session.getNamedQuery("getCashSalesList")
						.setParameter("companyId", companyId)
						.setLong("fromDate", fromDate)
						.setLong("toDate", toDate)
						.setParameter("viewType", viewType);
			}
			// /If length will be -1 then get list for mobile With out limits
			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				PaginationList<InvoicesList> queryResult = new PaginationList<InvoicesList>();
				while ((iterator).hasNext()) {

					InvoicesList invoicesList = new InvoicesList();
					object = (Object[]) iterator.next();

					long transactionNumber = (object[0] == null ? 0
							: ((Long) object[0]));
					invoicesList.setTransactionId(transactionNumber);
					invoicesList.setType((Integer) object[1]);
					invoicesList
							.setDate(new ClientFinanceDate((Long) object[2]));
					invoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					invoicesList.setCustomerName((String) object[4]);
					invoicesList.setNetAmount((Double) object[5]);
					invoicesList.setDueDate(object[6] == null ? null
							: new ClientFinanceDate((Long) object[6]));
					invoicesList.setTotalPrice((Double) object[7]);
					invoicesList.setBalance(object[8] == null ? null
							: (Double) object[8]);
					invoicesList.setVoided((Boolean) object[9]);
					invoicesList.setStatus((Integer) object[10]);
					invoicesList.setCurrency((Long) object[11]);
					invoicesList.setSaveStatus((Integer) object[12]);
					queryResult.add(invoicesList);
				}
				queryResult.setTotalCount(total);
				queryResult.setStart(start);
				return queryResult;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public PaginationList<TransactionsList> getReceivedTransactionsList(
			long companyId, long fromDate, long toDate, int start, int length)
			throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;

			PaginationList<TransactionsList> queryResult = new PaginationList<TransactionsList>();
			Query query = session.getNamedQuery("getInvoicesList")
					.setParameter("companyId", companyId)
					.setLong("fromDate", fromDate).setLong("toDate", toDate)
					.setParameter("viewType", 1)
					.setParameter("todayDate", new FinanceDate().getDate());

			List list = query.list();
			Object[] object = null;
			Iterator iterator;
			if (list != null) {

				iterator = list.iterator();

				while ((iterator).hasNext()) {

					TransactionsList transaction = new TransactionsList();
					object = (Object[]) iterator.next();

					if ((Integer) object[13] == null) {

						long transactionNumber = (object[0] == null ? 0
								: ((Long) object[0]));
						transaction.setTransactionId(transactionNumber);
						transaction.setType((Integer) object[1]);
						transaction.setDate(new ClientFinanceDate(
								(Long) object[2]));
						transaction.setCustomerName((String) object[4]);
						transaction.setReceivedAmount(object[8] == null ? null
								: (Double) object[8]);
						transaction.setCurrency((Long) object[11]);

						queryResult.add(transaction);
					}
				}
			}

			// for allreceived payments
			query = session.getNamedQuery("getAllCustomersPaymentsList")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate).setParameter("viewType", 0);
			list = query.list();
			if (list != null) {
				object = null;
				iterator = list.iterator();
				while ((iterator).hasNext()) {
					TransactionsList vendorPaymentsList = new TransactionsList();
					object = (Object[]) iterator.next();
					if ((Integer) object[12] == null) {
						vendorPaymentsList
								.setTransactionId((object[0] == null ? null
										: ((Long) object[0])));
						vendorPaymentsList.setType((Integer) object[1]);
						vendorPaymentsList.setDate(new ClientFinanceDate(
								object[2] == null ? null : ((Long) object[2])));
						vendorPaymentsList.setNumber((object[3] == null ? null
								: ((String) object[3])));
						vendorPaymentsList.setCustomerName((String) object[4]);
						vendorPaymentsList
								.setReceivedAmount((Double) object[6]);
						vendorPaymentsList.setCurrency((Long) object[10]);
						queryResult.add(vendorPaymentsList);
					}
				}
			}

			return queryResult;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public PaginationList<TransactionsList> getSpentTransactionsList(
			long companyId, long fromDate, long toDate, int start, int length)
			throws DAOException {
		int total = 0;
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		PaginationList<TransactionsList> customerRefundsList = new PaginationList<TransactionsList>();

		// for all customer payments
		Query query = session.getNamedQuery("getCustomerRefund")
				.setEntity("company", company)
				.setParameter("fromDate", new FinanceDate(fromDate))
				.setParameter("toDate", new FinanceDate(toDate));
		List list = query.list();

		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				TransactionsList customerRefund = new TransactionsList();
				CustomerRefund cr = (CustomerRefund) i.next();
				if (cr.getStatementRecord() == null) {
					customerRefund.setTransactionId(cr.getID());
					customerRefund.setType(cr.getType());
					customerRefund.setDate(new ClientFinanceDate(cr.getDate()
							.getDate())); //
					// customerRefund.setPaymentDate(new ClientFinanceDate(cr //
					// .getDate().getDate())); //
					// customerRefund.setIssueDate(new
					// ClientFinanceDate(cr // .getDate().getDate()));
					customerRefund.setDate(new ClientFinanceDate(cr.getDate()
							.getDate()));
					customerRefund
							.setCustomerName(((cr.getPayTo() != null) ? cr
									.getPayTo().getName() : null));
					customerRefund.setSpentAmount(cr.getTotal());
					customerRefund.setCurrency(cr.getCurrency().getID());

					customerRefundsList.add(customerRefund);
				}
			}
		}
		query = session.getNamedQuery("getWriteCheck.by.payToType")
				.setParameter("type", WriteCheck.TYPE_CUSTOMER)
				.setEntity("company", company);
		list = query.list();
		if (list != null) {
			Iterator i = list.iterator();
			while (i.hasNext()) {

				TransactionsList customerRefund = new TransactionsList();
				WriteCheck wc = (WriteCheck) i.next();
				if (wc.getStatementRecord() == null) {
					customerRefund.setTransactionId(wc.getID());
					customerRefund.setType(wc.getType());
					customerRefund.setDate(new ClientFinanceDate(wc.getDate()
							.getDate()));
					customerRefund
							.setCustomerName((wc.getCustomer() != null) ? wc
									.getCustomer().getName()
									: ((wc.getVendor() != null) ? wc
											.getVendor().getName() : (wc
											.getTaxAgency() != null ? wc
											.getTaxAgency().getName() : null)));
					customerRefund.setSpentAmount(wc.getAmount());
					customerRefund.setCurrency(wc.getCurrency().getID());
					customerRefundsList.add(customerRefund);
				}
			}
		}

		query = session.getNamedQuery("getBillsList")
				.setParameter("companyId", companyId)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("todayDate", new FinanceDate().getDate())
				.setParameter("viewType", 0);

		list = query.list();

		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();
			while ((iterator).hasNext()) {

				TransactionsList billsList = new TransactionsList();
				object = (Object[]) iterator.next();
				// comparing the balance amount, to determine this bill is paid
				// or not.We need to display all unpaid bills
				if ((Integer) object[13] == null && (Double) object[6] != 0) {
					billsList.setTransactionId((Long) object[0]);
					billsList.setType((Integer) object[1]);
					billsList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					billsList.setCustomerName((String) object[4]);
					billsList.setSpentAmount((Double) object[5]);
					billsList.setDate(new ClientFinanceDate((Long) object[9]));
					billsList.setCurrency((Long) object[11]);

					customerRefundsList.add(billsList);
				}
			}
		}

		// for VendorPaymentList
		query = session.getNamedQuery("getVendorPaymentsList")
				.setParameter("companyId", companyId)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate).setParameter("viewType", 1000);
		list = query.list();

		if (list != null) {
			Object[] object = null;
			Iterator iterator = list.iterator();

			while ((iterator).hasNext()) {

				object = (Object[]) iterator.next();

				String name = (String) object[6];
				if ((Integer) object[14] == null) {
					TransactionsList vendorPaymentsList = new TransactionsList();
					vendorPaymentsList
							.setTransactionId((object[0] == null ? null
									: ((Long) object[0])));
					vendorPaymentsList.setType((Integer) object[1]);
					vendorPaymentsList.setDate(new ClientFinanceDate(
							(Long) object[2]));
					// vendorPaymentsList
					// .setPaymentDate(new ClientFinanceDate(
					// (Long) object[2]));
					// vendorPaymentsList.setIssuedDate(new
					// ClientFinanceDate(
					// (Long) object[5]));
					if (vendorPaymentsList.getType() == 17) {
						name = Global.get().messages().taxAgencyPayment();
					}
					if (vendorPaymentsList.getType() == 25) {
						name = Global.get().messages().vatAgencyPayment();
					}
					vendorPaymentsList.setCustomerName(name);
					vendorPaymentsList.setSpentAmount((Double) object[8]);
					vendorPaymentsList.setCurrency((Long) object[12]);
					customerRefundsList.add(vendorPaymentsList);
				}
			}
		}

		return customerRefundsList;

	}

	public ArrayList<InvoicesList> getLatestInvoices(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestInvoices")
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<InvoicesList> queryResult = new ArrayList<InvoicesList>();
				while ((iterator).hasNext()) {

					InvoicesList invoicesList = new InvoicesList();
					object = (Object[]) iterator.next();

					long transactionNumber = (object[0] == null ? 0
							: ((Long) object[0]));
					invoicesList.setTransactionId(transactionNumber);
					invoicesList.setType((Integer) object[1]);
					invoicesList
							.setDate(new ClientFinanceDate((Long) object[2]));
					invoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					invoicesList.setCustomerName((String) object[4]);
					invoicesList.setNetAmount((Double) object[5]);
					invoicesList.setDueDate(new ClientFinanceDate(
							(Long) object[6]));
					invoicesList.setTotalPrice((Double) object[7]);
					invoicesList.setBalance((Double) object[8]);
					invoicesList.setCurrency((Long) object[9]);
					queryResult.add(invoicesList);
				}
				return new ArrayList<InvoicesList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public PaginationList<ClientMeasurement> getAllUnits(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.All.Units").setEntity(
				"company", company);
		List<Measurement> measurements = query.list();
		if (measurements == null) {
			return null;
		}
		PaginationList<ClientMeasurement> clientMeasurements = new PaginationList<ClientMeasurement>();
		for (Measurement measurement : measurements) {
			ClientMeasurement clientMeasurement;
			try {
				clientMeasurement = new ClientConvertUtil().toClientObject(
						measurement, ClientMeasurement.class);
				clientMeasurements.add(clientMeasurement);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientMeasurements;
	}

	public PaginationList<ClientWarehouse> getWarehouses(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Warehouse").setEntity(
				"company", company);
		List<Warehouse> warehouses = query.list();
		if (warehouses == null) {
			return null;
		}
		PaginationList<ClientWarehouse> clientWarehouses = new PaginationList<ClientWarehouse>();
		for (Warehouse warehouse : warehouses) {
			ClientWarehouse clientWarehouse;
			try {
				clientWarehouse = new ClientConvertUtil().toClientObject(
						warehouse, ClientWarehouse.class);
				clientWarehouses.add(clientWarehouse);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientWarehouses;

	}

	public ArrayList<ClientStockTransferItem> getStockTransferItems(
			Long companyId, long wareHouse) {
		Session session = HibernateUtil.getCurrentSession();
		List<ItemStatus> itemStatuses = session
				.getNamedQuery("getItemStatuses")
				.setLong("wareHouse", wareHouse)
				.setLong("companyId", companyId).list();

		ArrayList<ClientStockTransferItem> stockTransferItems = new ArrayList<ClientStockTransferItem>();
		for (ItemStatus record : itemStatuses) {
			ClientStockTransferItem item = new ClientStockTransferItem();
			item.setItem(record.getItem().getID());
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(record.getQuantity().getValue());
			quantity.setUnit(record.getQuantity().getUnit().getID());
			item.setQuantity(new ClientQuantity());
			item.setTotalQuantity(quantity);
			stockTransferItems.add(item);
		}
		return stockTransferItems;
	}

	public ArrayList<ClientStockTransfer> getWarehouseTransfersList(
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<StockTransfer> list = session
				.getNamedQuery("getStockTransfersList")
				.setLong("companyId", companyId).list();
		ArrayList<ClientStockTransfer> result = new ArrayList<ClientStockTransfer>();
		for (StockTransfer record : list) {
			ClientStockTransfer clientRecord = new ClientConvertUtil()
					.toClientObject(record, ClientStockTransfer.class);
			result.add(clientRecord);
		}
		return result;
	}

	public ArrayList<StockAdjustmentList> getStockAdjustments(Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<StockAdjustment> list = session
				.getNamedQuery("getStockAdjustmentsList")
				.setLong("companyId", companyId).list();
		ArrayList<StockAdjustmentList> result = new ArrayList<StockAdjustmentList>();
		for (StockAdjustment record : list) {
			for (StockAdjustmentItem item : record.getStockAdjustmentItems()) {
				StockAdjustmentList s = new StockAdjustmentList();
				s.setWareHouse(record.getWareHouse().getName());
				s.setItem(item.getItem().getName());
				s.setQuantity(new ClientConvertUtil().toClientObject(
						item.getAdjustmentQty(), ClientQuantity.class));
				s.setStockAdjustment(record.getID());
				result.add(s);
			}
		}
		return result;
	}

	public ArrayList<ClientItemStatus> getItemStatuses(long wareHouse,
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<ItemStatus> itemStatuses = session
				.getNamedQuery("getItemStatuses")
				.setLong("wareHouse", wareHouse)
				.setLong("companyId", companyId).list();

		ArrayList<ClientItemStatus> result = new ArrayList<ClientItemStatus>();
		for (ItemStatus record : itemStatuses) {
			ClientItemStatus clientRecord = new ClientConvertUtil()
					.toClientObject(record, ClientItemStatus.class);
			result.add(clientRecord);
		}
		return result;
	}

	public List<InvoicesList> getPayeeCreditMemosList(Long companyId,
			boolean isCustomer, long fromDate, long toDate) throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			// FIXME :: query optimization
			Query query = session.getNamedQuery("getVendorCreditMemos")
					.setParameter("companyId", companyId)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate);
			if (isCustomer) {
				query = session.getNamedQuery("getCustomerCreditMemos")
						.setParameter("companyId", companyId)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate);
			}
			List list = query.list();

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();
				List<InvoicesList> queryResult = new ArrayList<InvoicesList>();
				while ((iterator).hasNext()) {

					InvoicesList invoicesList = new InvoicesList();
					object = (Object[]) iterator.next();

					long transactionNumber = (object[0] == null ? 0
							: ((Long) object[0]));
					invoicesList.setTransactionId(transactionNumber);
					invoicesList.setType((Integer) object[1]);
					invoicesList
							.setDate(new ClientFinanceDate((Long) object[2]));
					invoicesList.setNumber((object[3] == null ? null
							: ((String) object[3])));
					invoicesList.setCustomerName((String) object[4]);
					invoicesList.setNetAmount((Double) object[5]);
					invoicesList.setTotalPrice((Double) object[6]);
					invoicesList.setVoided((Boolean) object[7]);
					invoicesList.setStatus((Integer) object[8]);
					invoicesList.setCurrency((Long) object[9]);
					queryResult.add(invoicesList);
				}
				return new ArrayList<InvoicesList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

}
