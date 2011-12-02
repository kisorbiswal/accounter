package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ItemStatus;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.StockAdjustmentItem;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;

public class InventoryManager extends Manager {
	public ArrayList<InvoicesList> getInvoiceList(long companyId)
			throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			// FIXME :: query optimization
			Query query = session.getNamedQuery("getInvoicesList")
					.setParameter("companyId", companyId);
			;
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
					invoicesList.setDueDate(object[6] == null ? null
							: new ClientFinanceDate((Long) object[6]));
					invoicesList.setTotalPrice((Double) object[7]);
					invoicesList.setBalance(object[8] == null ? null
							: (Double) object[8]);
					invoicesList.setVoided((Boolean) object[9]);
					invoicesList.setStatus((Integer) object[10]);
					invoicesList.setCurrency((Long) object[11]);
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

	public ArrayList<InvoicesList> getInvoiceList(long companyId,
			ClientFinanceDate fromDate, ClientFinanceDate toDate)
			throws DAOException {

		List<InvoicesList> invoicesList = null;
		List<InvoicesList> filteredList = null;

		filteredList = new ArrayList<InvoicesList>();
		invoicesList = getInvoiceList(companyId);
		for (InvoicesList list : invoicesList) {
			if (!list.getDate().before(fromDate)
					&& !list.getDate().after(toDate))

				filteredList.add(list);

		}
		return new ArrayList<InvoicesList>(filteredList);

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

	public ArrayList<ClientMeasurement> getAllUnits(Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.All.Units").setEntity(
				"company", company);
		List<Measurement> measurements = query.list();
		if (measurements == null) {
			return null;
		}
		ArrayList<ClientMeasurement> clientMeasurements = new ArrayList<ClientMeasurement>();
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

	public ArrayList<ClientWarehouse> getWarehouses(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Warehouse").setEntity(
				"company", company);
		List<Warehouse> warehouses = query.list();
		if (warehouses == null) {
			return null;
		}
		ArrayList<ClientWarehouse> clientWarehouses = new ArrayList<ClientWarehouse>();
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

}
