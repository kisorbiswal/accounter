package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;

public class SalesManager extends Manager {
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesByItemDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());
		List l = query.list();

		return createSalesByItemDetail(new ArrayList<SalesByCustomerDetail>(l));

	}

	public ArrayList<CashSales> getLatestCashSales(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestCashSales")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<CashSales> list = new ArrayList<CashSales>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				CashSales cashSale = new CashSales();
				// cashSale.setID((object[0] == null ? null : ((Long)
				// object[0])));
				cashSale.setDate((new FinanceDate((Long) object[1])));
				cashSale.setCustomer(object[2] != null ? (Customer) session
						.get(Customer.class, ((Long) object[2])) : null);
				cashSale.setSalesPerson(object[3] != null ? (SalesPerson) session
						.get(Vendor.class, ((Long) object[3])) : null);
				cashSale.setTotal((Double) object[4]);
				// cashSale.setID((object[5] == null ? null : ((String)
				// object[5])));
				list.add(cashSale);
			}
			if (list != null) {
				return new ArrayList<CashSales>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean isSalesTaxPayableAccount(long accountId, long companyId)
			throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getAccount.by.id.and.type")
					.setParameter("id", accountId)
					.setParameter("type", Account.TYPE_OTHER_CURRENT_LIABILITY)
					.setEntity("company", company);
			List list = query.list();

			if (list != null) {
				if (list.size() > 0) {
					return true;
				} else
					return false;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public boolean isSalesTaxPayableAccountByName(String accountName,
			long companyId) throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getAccount.by.name.and.type")

			.setParameter("name", accountName, EncryptedStringType.INSTANCE)
					.setParameter("type", Account.TYPE_OTHER_CURRENT_LIABILITY)
					.setEntity("company", company);
			List list = query.list();

			if (list != null) {
				if (list.size() > 0) {
					return true;
				} else
					return false;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Item> getLatestSalesItems(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLatestSalesItems")
				.setParameter("companyId", companyId);
		List list2 = query.list();

		Object object[] = null;
		Iterator iterator = list2.iterator();
		List<Item> list = new ArrayList<Item>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			Item item = new Item();
			// item.setID((object[0] == null ? null : ((Long) object[0])));
			item.setName((String) object[1]);
			item.setType((Integer) object[2]);
			item.setSalesPrice((Double) object[3]);
			// item.setID((String) object[4]);

			list.add(item);
		}
		if (list != null) {
			return new ArrayList<Item>(list);
		} else
			throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
					null));
	}

	public ArrayList<Item> getSalesItems(long companyId) throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<Item> list = (List<Item>) session.getNamedQuery(
					"getItem.by.checkisISellThisItemisTrue").setEntity(
					"company", company);

			if (list != null) {
				return new ArrayList<Item>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	private ArrayList<SalesByCustomerDetail> createSalesByItemDetail(List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();

			salesByCustomerDetail.setItemType(object[0] == null ? 0
					: ((Integer) object[0]).intValue());
			salesByCustomerDetail.setItemName((String) object[1]);
			salesByCustomerDetail.setType(object[2] == null ? 0
					: ((Integer) object[2]).intValue());
			salesByCustomerDetail.setDate(new ClientFinanceDate(
					((Long) object[3]).longValue()));

			salesByCustomerDetail.setNumber((String) object[4]);

			salesByCustomerDetail.setName((String) object[5]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[6] == null ? 0 : ((Double) object[6])
					.intValue());
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setUnitPrice(object[7] == null ? 0
					: ((Double) object[7]).doubleValue());
			salesByCustomerDetail.setDiscount(object[8] == null ? 0
					: ((Double) object[8]).doubleValue());
			salesByCustomerDetail.setAmount(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			salesByCustomerDetail.setSoOrQuoteNumber((String) object[10]);
			salesByCustomerDetail.setDueDate(((Long) object[11]) == null ? null
					: new ClientFinanceDate(((Long) object[11]).longValue()));
			salesByCustomerDetail.setDeliveryDate(object[12] == null ? null
					: new ClientFinanceDate(((Long) object[12]).longValue()));
			salesByCustomerDetail.setItemGroup((String) object[13]);
			salesByCustomerDetail.setDescription((String) object[14]);
			// salesByCustomerDetail.setIsVoid(object[15] == null ? true
			// : ((Boolean) object[15]).booleanValue());
			salesByCustomerDetail.setPaymentTermName((String) object[15]);

			/*
			 * Clob cl = (Clob) object[17]; if (cl == null) {
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
			salesByCustomerDetail.setMemo((String) object[16]);
			salesByCustomerDetail.setReference((String) object[17]);
			salesByCustomerDetail.setTransactionId(((Long) object[18])
					.longValue());
			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session
				.getNamedQuery("getSalesByItemDetailForParticularItem")
				.setParameter("companyId", companyId).setParameter(

				"itemName", itemName, EncryptedStringType.INSTANCE)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return createSalesByItemDetail(new ArrayList<SalesByCustomerDetail>(l));

	}

	public ArrayList<SalesByLocationSummary> getSalesByLocationSummary(
			boolean isLocation, FinanceDate startDate, FinanceDate endDate,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		ArrayList<SalesByLocationSummary> salesByLocationDetailList = new ArrayList<SalesByLocationSummary>();
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);

		List l;
		if (isLocation) {
			l = ((Query) session.getNamedQuery("getSalesByLocationSummary")
					.setParameter("companyId", companyId)

					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())).list();
		} else {
			l = ((Query) session.getNamedQuery("getSalesByClassSummary")
					.setParameter("companyId", companyId)

					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())).list();
		}

		Iterator iterator = l.iterator();
		Object[] object = null;
		if (iterator != null) {
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				SalesByLocationSummary salDetails = new SalesByLocationSummary();
				salDetails.setTotal(object[0] != null ? ((Double) object[0])
						: 0.0);
				salDetails
						.setLocationName(object[1] != null ? ((String) object[1])
								: null);
				salesByLocationDetailList.add(salDetails);
			}
		}
		return salesByLocationDetailList;
	}

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailForLocation(
			boolean isLocation, String locationName, FinanceDate startDate,
			FinanceDate endDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);

		List l;

		if (locationName == null) {
			locationName = "";
		}
		if (isLocation) {
			l = ((Query) session
					.getNamedQuery("getSalesByLocationDetailForLocation")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter(
							"locationName",
							locationName == null || locationName.isEmpty() ? null
									: locationName,
							EncryptedStringType.INSTANCE)).list();
		} else {
			l = ((Query) session
					.getNamedQuery("getSalesByClassDetailForClass")
					.setParameter("companyId", companyId)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter(
							"className",
							locationName == null || locationName.isEmpty() ? null
									: locationName,
							EncryptedStringType.INSTANCE)).list();
		}

		Iterator iterator = l.iterator();
		Object[] object = null;
		if (iterator != null) {
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				SalesByLocationDetails salDetails = new SalesByLocationDetails();
				salDetails.setDate(object[0] != null ? ((Long) object[0])
						.longValue() : 0);
				salDetails.setType(object[1] != null ? ((Integer) object[1])
						: 0);
				salDetails.setNumber(object[2] != null ? ((String) object[2])
						: " ");
				salDetails.setAccount(object[3] != null ? ((String) object[3])
						: " ");
				salDetails
						.setProuductOrService(object[4] != null ? ((String) object[4])
								: " ");
				salDetails.setAmount(object[5] != null ? ((Double) object[5])
						: 0.0);
				salDetails
						.setLocationName(object[6] != null ? ((String) object[6])
								: null);
				salDetails
						.setTransactionid(object[7] != null ? ((Long) object[7])
								.longValue() : 0);
				salesByLocationDetailList.add(salDetails);
			}
		}
		return salesByLocationDetailList;
	}

	/**
	 * 
	 * @param isLocation
	 * @param isCustomer
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ArrayList<SalesByLocationDetails> getSalesByLocationDetail(
			boolean isLocation, boolean isCustomer, FinanceDate startDate,
			FinanceDate endDate, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate startDate1 = getCurrentFiscalYearStartDate(company);

		/*
		 * Here endDate1 is used to store the previous month of endDate value
		 */
		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();
		int year = endDate.getYear();
		int month = endDate.getMonth() - 1;
		year = (month == 0) ? year - 1 : year;
		month = (month == 0) ? 12 : month;
		FinanceDate endDate1 = new FinanceDate(year, month, 31);

		if (year != startDate1.getYear())
			startDate1 = new FinanceDate(year, 01, 01);

		List l = null;
		if (isCustomer) {
			if (isLocation) {
				l = ((Query) session.getNamedQuery("getSalesByLocationDetail")
						.setParameter("companyId", companyId)
						.setParameter("startDate", startDate.getDate())
						.setParameter("endDate", endDate.getDate())).list();
			} else {
				l = ((Query) session.getNamedQuery("getSalesByClassDetail")
						.setParameter("companyId", companyId)
						.setParameter("startDate", startDate.getDate())
						.setParameter("endDate", endDate.getDate())).list();
			}
		} else {
			if (!isLocation) {
				l = ((Query) session
						.getNamedQuery("getPurchaseByLocationDetail")
						.setParameter("companyId", companyId)
						.setParameter("startDate", startDate.getDate())
						.setParameter("endDate", endDate.getDate())).list();
			} else {
				l = ((Query) session.getNamedQuery("getPurchaseByClassDetail")
						.setParameter("companyId", companyId)
						.setParameter("startDate", startDate.getDate())
						.setParameter("endDate", endDate.getDate())).list();
			}
		}

		Iterator iterator = l.iterator();
		Object[] object = null;
		if (iterator != null) {
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				SalesByLocationDetails salDetails = new SalesByLocationDetails();
				salDetails.setDate(object[0] != null ? ((Long) object[0])
						.longValue() : 0);
				salDetails.setType(object[1] != null ? ((Integer) object[1])
						: 0);
				salDetails.setNumber(object[2] != null ? ((String) object[2])
						: " ");
				salDetails.setAccount(object[3] != null ? ((String) object[3])
						: " ");
				salDetails
						.setProuductOrService(object[4] != null ? ((String) object[4])
								: " ");
				salDetails.setAmount(object[5] != null ? ((Double) object[5])
						: 0.0);
				salDetails
						.setLocationName(object[6] != null ? ((String) object[6])
								: null);
				salDetails
						.setTransactionid(object[7] != null ? ((Long) object[7])
								.longValue() : 0);
				salesByLocationDetailList.add(salDetails);
			}
		}
		return salesByLocationDetailList;
	}

	public ArrayList<Item> getSalesReportItems(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesReportItems")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<Item> queryResult = new ArrayList<Item>();
		while ((iterator).hasNext()) {

			Item item = new Item();
			object = (Object[]) iterator.next();
			item.setType(object[1] == null ? 0 : ((Integer) object[1])
					.intValue());
			item.setName((String) object[2]);
			queryResult.add(item);
		}
		return new ArrayList<Item>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesByCustomerSummary")
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
			salesByCustomerDetail.setAmount(object[1] == null ? 0
					: ((Double) object[1]).doubleValue());

			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);

	}

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getSalesByItemSummary")
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
			salesByCustomerDetail.setItemName((String) object[0]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[1] == null ? 0 : ((Double) object[1])
					.intValue());
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setAmount(object[2] == null ? 0
					: ((Double) object[2]).doubleValue());

			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);

	}

	// public ArrayList<OpenAndClosedOrders> getOpenSalesOrders(
	// FinanceDate startDate, FinanceDate endDate, long companyId)
	// throws DAOException {
	//
	// Session session = HibernateUtil.getCurrentSession();
	//
	// List l = ((Query) session.getNamedQuery("getOpenSalesOrders")
	// .setParameter("companyId", companyId)
	//
	// .setParameter("startDate", startDate.getDate())
	// .setParameter("endDate", endDate.getDate())).list();
	//
	// return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	// }

	// public ArrayList<OpenAndClosedOrders> getClosedSalesOrders(
	// FinanceDate startDate, FinanceDate endDate, long companyId)
	// throws DAOException {
	// Session session = HibernateUtil.getCurrentSession();
	//
	// List l = ((Query) session.getNamedQuery("getClosedSalesOrders")
	// .setParameter("companyId", companyId)
	//
	// .setParameter("startDate", startDate.getDate())
	// .setParameter("endDate", endDate.getDate())).list();
	//
	// return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	// }
	//
	// public ArrayList<OpenAndClosedOrders> getCompletedSalesOrders(
	// FinanceDate startDate, FinanceDate endDate, long companyId)
	// throws DAOException {
	//
	// Session session = HibernateUtil.getCurrentSession();
	//
	// List l = ((Query) session.getNamedQuery("getCompletedSalesOrders")
	// .setParameter("companyId", companyId)
	// .setParameter("startDate", startDate.getDate())
	// .setParameter("endDate", endDate.getDate())).list();
	//
	// return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	// }

	// public PaginationList<SalesOrdersList> getSalesOrdersList(long companyId,
	// long fromDate, long toDate) throws DAOException {
	//
	// Session session = HibernateUtil.getCurrentSession();
	//
	// Query query = session.getNamedQuery("getSalesOrdersList")
	// .setParameter("companyId", companyId)
	// .setParameter("fromDate", fromDate)
	// .setParameter("toDate", toDate);
	// ;
	//
	// List list = query.list();
	// PaginationList<SalesOrdersList> esl = new
	// PaginationList<SalesOrdersList>();
	//
	// for (int i = 0; i < list.size(); i++) {
	//
	// Object[] obj = (Object[]) list.get(i);
	// // for (int j = 0; j < obj.length; j++)
	// {
	// SalesOrdersList el = new SalesOrdersList();
	// el.setTransactionId((Long) obj[0]);
	// el.setType(((Integer) obj[1]).intValue());
	// el.setNumber(((String) obj[2]));
	// el.setTotal(((Double) obj[3]).doubleValue());
	// el.setDate(new ClientFinanceDate((Long) obj[4]));
	// el.setCustomerName((String) obj[5]);
	// el.setPhone((String) obj[6]);
	// el.setDueDate(new ClientFinanceDate((Long) obj[7]));
	// el.setStatus((Integer) obj[8]);
	// esl.add(el);
	// }
	// }
	//
	// return esl;
	// }

	public ArrayList<OpenAndClosedOrders> getSalesOrders(int type,
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		List l = ((Query) session.getNamedQuery("getSalesOrders")
				.setParameter("startDate", startDate.getDate())
				.setParameter("companyId", companyId)
				.setParameter("endDate", endDate.getDate())
				.setParameter("toDay", new ClientFinanceDate().getDate())
				.setParameter("type", type)).list();
		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	// public ArrayList<OpenAndClosedOrders> getCanceledSalesOrders(
	// FinanceDate startDate, FinanceDate endDate, long companyId)
	// throws DAOException {
	//
	// Session session = HibernateUtil.getCurrentSession();
	//
	// List l = ((Query) session.getNamedQuery("getCanceledSalesOrders")
	// .setParameter("companyId", companyId)
	// .setParameter("startDate", startDate.getDate())
	// .setParameter("endDate", endDate.getDate())).list();
	//
	// return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	// }
}
