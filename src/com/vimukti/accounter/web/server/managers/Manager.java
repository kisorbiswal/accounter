package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class Manager {
	protected Logger log = Logger.getLogger(FinanceTool.class);

	/**
	 * Returns the Current Company
	 * 
	 * @return
	 */
	public Company getCompany(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		return (Company) session.get(Company.class, companyId);
	}

	public boolean canEdit(IAccounterServerCore clonedObject,
			IAccounterCore clientObject) throws AccounterException {

		IAccounterServerCore serverObject = new ServerConvertUtil()
				.toServerObject(null, clientObject,
						HibernateUtil.getCurrentSession());

		return serverObject.canEdit(clonedObject);

	}

	public <T extends IAccounterCore> T getServerObjectByName(
			AccounterCoreType type, String name, Company company)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		// Class<?> serverClass = getClientEquivalentServerClass(type);

		if (type != null) {

			Query hibernateQuery = session
					.getNamedQuery(
							"unique.name." + type.getServerClassSimpleName())
					.setString(0, name).setEntity("company", company);

			List objects = hibernateQuery.list();

			if (objects != null && objects.get(0) != null) {

				return (T) objects.get(0);

			}

		}

		return null;

	}

	public ArrayList<OpenAndClosedOrders> prepareQueryResult(List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<OpenAndClosedOrders> queryResult = new ArrayList<OpenAndClosedOrders>();
		while ((iterator).hasNext()) {
			OpenAndClosedOrders openAndClosedOrder = new OpenAndClosedOrders();
			object = (Object[]) iterator.next();
			openAndClosedOrder.setTransactionID(((BigInteger) object[0])
					.longValue());
			openAndClosedOrder.setTransactionType((Integer) object[1]);
			openAndClosedOrder.setTransactionDate(new ClientFinanceDate(
					((BigInteger) object[2]).longValue()));
			openAndClosedOrder.setVendorOrCustomerName((String) object[3]);
			// openAndClosedOrder
			// .setQuantity(object[4] != null ? ((BigDecimal) object[4])
			// .intValue() : 0);
			openAndClosedOrder.setAmount(object[4] != null ? (Double) object[4]
					: 0.0);
			queryResult.add(openAndClosedOrder);
		}
		return new ArrayList<OpenAndClosedOrders>(queryResult);
	}

	public ClientFinanceDate[] getMinimumAndMaximumTransactionDate(
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery(
				"getMinimumAndMaximumTransactionDate").setParameter(
				"companyId", companyId);
		List list = query.list();
		Object[] object = null;
		Iterator iterator = list.iterator();
		// List<Vendor> queryResult = new ArrayList<Vendor>();
		ClientFinanceDate startDate = new ClientFinanceDate();
		ClientFinanceDate endDate = new ClientFinanceDate();
		if ((iterator).hasNext()) {

			object = (Object[]) iterator.next();
			startDate = (object[0] == null ? null : new ClientFinanceDate(
					(Long) object[0]));
			endDate = (object[1] == null ? null : new ClientFinanceDate(
					(Long) object[1]));
		}
		return new ClientFinanceDate[] { startDate, endDate };
	}

	public Object getServerObjectForid(AccounterCoreType t, long id) {

		Session session = HibernateUtil.getCurrentSession();

		Class<?> serverClass = getClientEquivalentServerClass(t);

		if (serverClass != null) {

			return session.get(serverClass, id);

		}

		return null;
	}

	private Class<?> getClientEquivalentServerClass(AccounterCoreType type) {

		String clientClassName = type.getClientClassSimpleName();

		clientClassName = clientClassName.replaceAll("Client", "");

		Class<?> clazz = null;

		// FIXME if Class class1 if of another package other than,
		// com.vimukti.accounter.core

		try {
			String qualifiedName = "com.vimukti.accounter.core."
					+ clientClassName;
			clazz = Class.forName(qualifiedName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazz;
	}

	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id, int companyType, long companyId) throws DAOException,
			AccounterException {

		Object serverObject = getServerObjectForid(type, id);

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverObject != null) {
			T t = (T) new ClientConvertUtil().toClientObject(serverObject,
					Util.getClientEqualentClass(serverClass));
			if (t instanceof ClientTransaction
					&& companyType == Company.ACCOUNTING_TYPE_UK) {
				Session session = HibernateUtil.getCurrentSession();
				Company company = getCompany(companyId);
				Query query2 = session
						.getNamedQuery("getTAXRateCalculation.by.check.idandvatReturn");
				query2.setParameter("id", t.getID()).setEntity("company",
						company);
				List<?> list = query2.list();
				if (list != null && list.size() > 0)
					((ClientTransaction) t).setCanEdit(false);
			}
			return t;

		}

		return null;
	}

	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type, long companyId) throws DAOException,
			AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		List<T> clientObjects = new ArrayList<T>();

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverClass != null) {

			Query hibernateQuery = session.getNamedQuery(
					"list." + type.getServerClassSimpleName()).setEntity(
					"company", getCompany(companyId));

			List objects = hibernateQuery.list();

			if (objects != null) {

				for (Object o : objects) {

					clientObjects.add((T) new ClientConvertUtil()
							.toClientObject(o,
									Util.getClientEqualentClass(serverClass)));
				}

			}

		}

		ArrayList<T> arrayList = new ArrayList<T>(clientObjects);
		return arrayList;

	}

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name, long companyId) throws DAOException,
			AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		Class<?> serverClass = getClientEquivalentServerClass(type);

		if (serverClass != null) {

			Query hibernateQuery = session
					.getNamedQuery(
							"unique.name." + type.getServerClassSimpleName())
					.setString(0, name)
					.setEntity("company", getCompany(companyId));

			List objects = hibernateQuery.list();

			if (objects != null && objects.size() > 0 && objects.get(0) != null) {

				return (T) new ClientConvertUtil().toClientObject(
						objects.get(0),
						Util.getClientEqualentClass(serverClass));

			}

		}

		return null;

	}

	public void mergeOpeningBalanceEntries(
			List<TransactionHistory> queryResult, Set<String> payee,
			Map<String, TransactionHistory> openingBalnaceEntries) {

		for (String payeeName : payee) {
			int index = -1;
			for (int i = 0; i < queryResult.size(); i++) {
				TransactionHistory entry = queryResult.get(i);
				if (entry.getName().equals(payeeName)) {
					index = i;
					break;
				}
			}

			if (index != -1 && openingBalnaceEntries.containsKey(payeeName)) {
				queryResult.add(index, openingBalnaceEntries.get(payeeName));
			}
		}

	}
	
	public void updateClientUser(ClientUser clientUser, Client client) {
		clientUser.setFirstName(client.getFirstName());
		clientUser.setLastName(client.getLastName());
		clientUser.setEmail(client.getEmailId());
	}
}
