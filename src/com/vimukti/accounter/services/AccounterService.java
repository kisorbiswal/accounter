package com.vimukti.accounter.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.classic.Lifecycle;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.utils.SecureUtils;

/**
 * 
 * @author Devesh Satwani
 * 
 */

public class AccounterService extends HibernateDaoSupport implements
		IAccounterService {

	TransactionTemplate transactionTemplate;
	IAccounterDAOService accounterDao;
	IAccounterGUIDAOService accounterGUIDao;

	public void setTransactionTemplate(TransactionTemplate template) {
		this.transactionTemplate = template;
	}

	public void setAccounterDao(IAccounterDAOService accounterDao) {
		this.accounterDao = accounterDao;
	}

	public void setAccounterGUIDao(IAccounterGUIDAOService accounterGUIDao) {
		this.accounterGUIDao = accounterGUIDao;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public <T extends IAccounterServerCore> String createObject(T object)
			throws DAOException {
		if (object.getID() == null) {
			object.setStringID(SecureUtils.createID());
		}
		Session session = Utility.getCurrentSession();
		Transaction t = session.beginTransaction();
		session.save(object);
		t.commit();
		return ((T) object).getID();
	}

	@Override
	public <T extends IAccounterServerCore> Boolean deleteObject(T object)
			throws DAOException {
		try {
			Session session = Utility.getCurrentSession();
			Transaction t = session.beginTransaction();
			session.delete(object);
			t.commit();
			return true;
		} catch (HibernateException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAccounterServerCore> T getObjectById(Class clazz,
			String stringID) throws DAOException {
		Session session = Utility.getCurrentSession();
		Query query = session
				.createQuery(
						"from " + clazz.getName()
								+ " entity where entity.stringID = ?")
				.setParameter(0, stringID);
		List l = query.list();
		T entity = null;
		if (l != null && l.size() > 0 && l.get(0) != null) {
			entity = (T) l.get(0);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAccounterServerCore> T getObjectByName(Class clazz,
			String name) throws DAOException {
		Session session = Utility.getCurrentSession();
		Query query = session.createQuery(
				"from " + clazz.getName() + " entity where entity.name = ?")
				.setParameter(0, name);
		List l = query.list();
		T entity = null;
		if (l != null && l.size() > 0 && l.get(0) != null) {
			entity = (T) l.get(0);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAccounterServerCore> List<T> getObjects(Class clazz)
			throws DAOException {
		Session session = Utility.getCurrentSession();
		Query query = session.createQuery("from " + clazz.getName()
				+ " entity ");
		List<T> entity = query.list();
		if (entity != null && entity.size() > 0) {
			return entity;
		}
		return null;
	}

	@Override
	public <T extends IAccounterServerCore> Boolean updateObject(T object)
			throws DAOException {
		try {
			Session s = Utility.getCurrentSession();
			Transaction t = s.beginTransaction();

			s.saveOrUpdate(object);
			if (object instanceof Lifecycle) {
				Lifecycle lifecycle = (Lifecycle) object;
				lifecycle.onUpdate(s);
			}
			t.commit();

			return true;
		} catch (HibernateException e) {
			return false;
		}
		// 20- 9000347779
	}

	@SuppressWarnings("unchecked")
	private long getLongIdForGivenStringId(Class clazz, String stringID) {

		Session session = Utility.getCurrentSession();
		String hqlQuery = "select entity.id from " + clazz.getName()
				+ " entity where entity.stringID=?";
		Query query = session.createQuery(hqlQuery).setString(0, stringID);
		List l = query.list();
		if (l != null && l.get(0) != null) {
			return (Long) l.get(0);
		} else
			return 0;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAccounterServerCore> Boolean canDelete(Class clazz,
			String stringID) throws DAOException {

		Session session = Utility.getCurrentSession();
		long inputId = getLongIdForGivenStringId(clazz, stringID);
		String queryName = new StringBuilder().append("canDelete").append(
				clazz.getSimpleName()).toString();
		Query query = session.getNamedQuery(queryName).setParameter("inputId",
				inputId);
		return executeQuery(query);
	}

	@SuppressWarnings("unchecked")
	private Boolean executeQuery(Query query) {

		List queryResult = query.list();
		Boolean flag = true;
		if (queryResult != null && queryResult.get(0) != null) {
			Object[] result = (Object[]) queryResult.get(0);
			for (Object object : result) {
				if (object != null) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * // public AccounterService() { // SessionFactory factory=new //
	 * Configuration().addResource("com/vimukti/accounter/mapping.xml" //
	 * ).buildSessionFactory(); // HibernateTemplate template=new
	 * HibernateTemplate(factory); // this.setHibernateTemplate(template); // }
	 * 
	 * @Override public void alterAccount(Account account) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from Account a where a.id= ? ", new Object[] {
	 * account.getId() }); if (list.size() > 0) { Account existingAccount =
	 * (Account) list.get(0);
	 * 
	 * if (account.getId() != (existingAccount.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * 
	 * } template.saveOrUpdate(account);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void alterCashPurchase(CashPurchase cashPurchase) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from CashPurchase cp where cp.id= ? ", new
	 * Object[] { cashPurchase.getId() }); if (list.size() > 0) { CashPurchase
	 * existingCashPurchase = (CashPurchase) list.get(0);
	 * 
	 * if (cashPurchase.getId() != (existingCashPurchase.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(cashPurchase);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCashSales(CashSales cashSales) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from CashSales cs where cs.id= ? ", new
	 * Object[] { cashSales.getId() }); if (list.size() > 0) { CashSales
	 * existingCashSales = (CashSales) list.get(0);
	 * 
	 * if (cashSales.getId() != (existingCashSales.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(cashSales);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCompany(Company company) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from Company c where c.id= ? ", new Object[] {
	 * company.getId() }); if (list.size() > 0) { Company old = (Company)
	 * list.get(0);
	 * 
	 * if (old.getId() != (company.getId())) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(company);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCreditCardCharge(CreditCardCharge
	 * creditCardCharge) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find( "from CreditCardCharge cc where cc.id= ? ", new Object[] {
	 * creditCardCharge.getId() }); if (list.size() > 0) { CreditCardCharge
	 * existingCreditCardCharge = (CreditCardCharge) list .get(0);
	 * 
	 * if (creditCardCharge.getId() != (existingCreditCardCharge .getId())) {
	 * throw (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null));
	 * } } template.saveOrUpdate(creditCardCharge);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCreditRating(CreditRating creditRating) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from CreditRating cr where cr.id= ? ", new Object[] {
	 * creditRating.getId() }); if (list.size() > 0) { CreditRating
	 * existingCreditRating = (CreditRating) list.get(0);
	 * 
	 * if (creditRating.getId() != (existingCreditRating.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(creditRating);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCurrency(Currency currency) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from Currency c where c.id= ? ", new Object[]
	 * { currency.getId() }); if (list.size() > 0) { Currency existingCurrency =
	 * (Currency) list.get(0);
	 * 
	 * if (existingCurrency.getId() != (currency.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(currency);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCustomer(Customer customer) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from Customer c where c.id= ? ", new Object[]
	 * { customer.getId() }); if (list.size() > 0) { Customer old = (Customer)
	 * list.get(0);
	 * 
	 * if (old.getId() != (customer.getId())) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(customer);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCustomerCreditMemo(CustomerCreditMemo
	 * creditMemo) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list = template.find(
	 * "from CustomerCreditMemo cm where cm.id= ? ", new Object[] {
	 * creditMemo.getId() }); if (list.size() > 0) { CustomerCreditMemo
	 * existingCustomerCreditMemo = (CustomerCreditMemo) list .get(0);
	 * 
	 * if (creditMemo.getId() != (existingCustomerCreditMemo.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(creditMemo);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCustomerGroup(CustomerGroup customerGroup)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list =
	 * template.find("from CustomerGroup c where c.id= ? ", new Object[] {
	 * customerGroup.getId() }); if (list.size() > 0) { CustomerGroup old =
	 * (CustomerGroup) list.get(0);
	 * 
	 * if (old.getId() != (customerGroup.getId())) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(customerGroup);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterCustomerRefunds(CustomerRefund
	 * customerRefunds) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from CustomerRefund cr where cr.id= ? ", new Object[] {
	 * customerRefunds.getId() }); if (list.size() > 0) { CustomerRefund
	 * existingCustomerRefunds = (CustomerRefund) list .get(0);
	 * 
	 * if (existingCustomerRefunds.getId() != (customerRefunds.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(customerRefunds);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterEnterBill(EnterBill enterBill) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from EnterBill e where e.id= ? ", new Object[]
	 * { enterBill.getId() }); if (list.size() > 0) { EnterBill
	 * existingenterbill = (EnterBill) list.get(0);
	 * 
	 * if (enterBill.getId() != (existingenterbill.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * 
	 * template.saveOrUpdate(enterBill);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterExpense(Expense expense) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from Expense e where e.id= ? ", new Object[] {
	 * expense.getId() }); if (list.size() > 0) { Expense existingExpense =
	 * (Expense) list.get(0);
	 * 
	 * if (expense.getId() != (existingExpense.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * 
	 * template.update(expense);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterEstimate(Estimate estimate) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from Estimate e where e.id= ? ", new Object[]
	 * { estimate.getId() }); if (list.size() > 0) { Estimate existingEstimate =
	 * (Estimate) list.get(0);
	 * 
	 * if (estimate.getId() != (existingEstimate.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(estimate);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterInvoice(Invoice invoice) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from Invoice i where i.id= ? ", new Object[] {
	 * invoice.getId() }); if (list.size() > 0) { Invoice existinginvoice =
	 * (Invoice) list.get(0);
	 * 
	 * if (invoice.getId() != (existinginvoice.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(invoice);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterIssuePayment(IssuePayment issuePayment) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from IssuePayment i where i.id= ? ", new
	 * Object[] { issuePayment.getId() }); if (list.size() > 0) { IssuePayment
	 * existingIssuePayment = (IssuePayment) list.get(0);
	 * 
	 * if (issuePayment.getId() != (existingIssuePayment.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(issuePayment);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterItem(Item item) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from Item i where i.id= ? ", new Object[] { item.getId()
	 * }); if (list.size() > 0) { Item old = (Item) list.get(0);
	 * 
	 * if (old.getId() != (item.getId())) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(item);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterItemGroup(ItemGroup itemGroup) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from ItemGroup i where i.id= ? ", new Object[] {
	 * itemGroup.getId() }); if (list.size() > 0) { ItemGroup existingItemGroup
	 * = (ItemGroup) list.get(0);
	 * 
	 * if (itemGroup.getId() != (existingItemGroup.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(itemGroup);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterMakeDeposit(MakeDeposit makeDeposit) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from MakeDeposit m where m.id= ? ", new
	 * Object[] { makeDeposit.getId() }); if (list.size() > 0) { MakeDeposit
	 * existingMakeDeposit = (MakeDeposit) list.get(0);
	 * 
	 * if (makeDeposit.getId() != (existingMakeDeposit.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(makeDeposit);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterPayBill(PayBill payBill) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from PayBill p where p.id= ? ", new Object[] {
	 * payBill.getId() }); if (list.size() > 0) { PayBill existingpaybill =
	 * (PayBill) list.get(0);
	 * 
	 * if (payBill.getId() != (existingpaybill.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(payBill);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterPaymentTerms(PaymentTerms paymentTerms) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from PaymentTerms pt where pt.id= ?", new
	 * Object[] { paymentTerms.getId() });
	 * 
	 * if (list.size() > 0) { PaymentTerms old = (PaymentTerms) list.get(0); if
	 * ((old.getId() != (paymentTerms.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(paymentTerms);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterPriceLevel(PriceLevel priceLevel) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from PriceLevel pl where pl.id= ?", new
	 * Object[] { priceLevel.getId() });
	 * 
	 * if (list.size() > 0) { PriceLevel old = (PriceLevel) list.get(0); if
	 * ((old.getId() != (priceLevel.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(priceLevel);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterPurchaseOrder(PurchaseOrder purchaseOrder)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list =
	 * template.find("from PurchaseOrder p where p.id= ? ", new Object[] {
	 * purchaseOrder.getId() }); if (list.size() > 0) { PurchaseOrder
	 * existingpurchaseorder = (PurchaseOrder) list .get(0);
	 * 
	 * if (purchaseOrder.getId() != (existingpurchaseorder.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(purchaseOrder);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterReceivePayment(ReceivePayment receivePayment)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list =
	 * template.find("from ReceivePayment r where r.id= ? ", new Object[] {
	 * receivePayment.getId() }); if (list.size() > 0) { ReceivePayment
	 * existingreceivePayment = (ReceivePayment) list .get(0);
	 * 
	 * if (receivePayment.getId() != (existingreceivePayment.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(receivePayment);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterSalesOrder(SalesOrder salesOrder) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from SalesOrder s where s.id= ? ", new
	 * Object[] { salesOrder.getId() }); if (list.size() > 0) { SalesOrder
	 * existingsalesorder = (SalesOrder) list.get(0);
	 * 
	 * if (salesOrder.getId() != (existingsalesorder.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(salesOrder);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterSalesPerson(SalesPerson salesPerson) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from SalesPerson sp where sp.id= ?", new
	 * Object[] { salesPerson.getId() });
	 * 
	 * if (list.size() > 0) { SalesPerson old = (SalesPerson) list.get(0); if
	 * ((old.getId() != (salesPerson.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(salesPerson);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterShippingMethod(ShippingMethod shippingMethod)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list =
	 * template.find("from ShippingMethod s where s.id= ? ", new Object[] {
	 * shippingMethod.getId() }); if (list.size() > 0) { ShippingMethod
	 * existingShippingMethod = (ShippingMethod) list .get(0);
	 * 
	 * if (shippingMethod.getId() != (existingShippingMethod.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(shippingMethod);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void alterShippingTerms(ShippingTerms shippingTerms)
	 * throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from ShippingTerms s where s.id= ? ", new Object[] {
	 * shippingTerms.getId() }); if (list.size() > 0) { ShippingTerms
	 * existingShippingTerms = (ShippingTerms) list .get(0);
	 * 
	 * if (shippingTerms.getId() != (existingShippingTerms.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(shippingTerms);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterTaxAgency(TaxAgency taxAgency) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from TaxAgency t where t.id= ? ", new Object[] {
	 * taxAgency.getId() }); if (list.size() > 0) { TaxAgency existingTaxAgency
	 * = (TaxAgency) list.get(0);
	 * 
	 * if (taxAgency.getId() != (existingTaxAgency.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(taxAgency);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterTaxCode(TaxCode taxCode) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from TaxCode t where t.id= ? ", new Object[] {
	 * taxCode.getId() }); if (list.size() > 0) { TaxCode existingTaxCode =
	 * (TaxCode) list.get(0);
	 * 
	 * if (taxCode.getId() != (existingTaxCode.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(taxCode);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterTaxGroup(TaxGroup taxGroup) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from TaxGroup t where t.id= ? ", new Object[] {
	 * taxGroup.getId() }); if (list.size() > 0) { TaxGroup existingTaxGroup =
	 * (TaxGroup) list.get(0);
	 * 
	 * if (taxGroup.getId() != (existingTaxGroup.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(taxGroup);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterTaxRates(TaxRates taxRates) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from TaxRates t where t.id= ? ", new Object[] {
	 * taxRates.getId() }); if (list.size() > 0) { TaxRates existingTaxRates =
	 * (TaxRates) list.get(0);
	 * 
	 * if (taxRates.getId() != (existingTaxRates.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(taxRates);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterTransferFund(TransferFund transferFund) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from TransferFund tf where tf.id= ? ", new
	 * Object[] { transferFund.getId() }); if (list.size() > 0) { TransferFund
	 * existingTransferFund = (TransferFund) list.get(0);
	 * 
	 * if (transferFund.getId() != (existingTransferFund.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(transferFund);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterUnitOfMeasure(UnitOfMeasure untiOfMeasure)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); List list =
	 * template.find("from UnitOfMeasure u where u.id= ? ", new Object[] {
	 * untiOfMeasure.getId() }); if (list.size() > 0) { UnitOfMeasure
	 * existingUnitOfMeasure = (UnitOfMeasure) list .get(0);
	 * 
	 * if (untiOfMeasure.getId() != (existingUnitOfMeasure.getId())) { throw
	 * (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(untiOfMeasure);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void alterUser(User user) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate(); List list =
	 * template.find("from User u where u.id= ? ", new Object[] { user.getId()
	 * }); if (list.size() > 0) { User existingUser = (User) list.get(0);
	 * 
	 * if (user.getId() != (existingUser.getId())) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(user);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void alterVendor(Vendor vendor) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Vendor v where v.id= ?", new Object[] {
	 * vendor.getId() });
	 * 
	 * if (list.size() > 0) { Vendor old = (Vendor) list.get(0); if
	 * ((old.getId() != (vendor.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(vendor);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterVendorCreditMemo(VendorCreditMemo
	 * vendorCreditMemo) throws DAOException { try { HibernateTemplate template
	 * = getHibernateTemplate();
	 * 
	 * List list = template.find("from VendorCreditMemo v where v.id= ?", new
	 * Object[] { vendorCreditMemo.getId() });
	 * 
	 * if (list.size() > 0) { VendorCreditMemo existingVendorCreditMemo =
	 * (VendorCreditMemo) list .get(0); if ((existingVendorCreditMemo.getId() !=
	 * (vendorCreditMemo .getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(vendorCreditMemo);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from VendorGroup vg where vg.id= ?", new
	 * Object[] { vendorGroup.getId() });
	 * 
	 * if (list.size() > 0) { VendorGroup old = (VendorGroup) list.get(0); if
	 * ((old.getId() != (vendorGroup.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(vendorGroup);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterWriteCheck(WriteCheck writeCheck) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from WriteCheck wc where wc.id= ?", new
	 * Object[] { writeCheck.getId() });
	 * 
	 * if (list.size() > 0) { WriteCheck old = (WriteCheck) list.get(0); if
	 * ((old.getId() != (writeCheck.getId()))) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(writeCheck);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createAccount(final Account account) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template .find(
	 * "from Account a where a.company = ? and a.name = ? and a.number = ?", new
	 * Object[] { account.getCompany(), account.getName(), account.getNumber()
	 * }); if (list.size() > 0) {
	 * 
	 * throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * // Account parent = account.getParent(); // if (parent != null) { //
	 * parent = SessionUtils.fromSession(session, parent); //
	 * account.setParent(parent); // account.setCompany(parent.getCompany()); //
	 * }
	 * 
	 * SessionUtils.update(account, session);
	 * 
	 * // int type = account.getType(); // if (type == Account.TYPE_INCOME // ||
	 * type == Account.TYPE_OTHER_INCOME // || type == Account.TYPE_CREDIT_CARD
	 * // || type == Account.TYPE_PAYROLL_LIABILITY // || type ==
	 * Account.TYPE_OTHER_CURRENT_LIABILITY // || type ==
	 * Account.TYPE_LONG_TERM_LIABILITY // || type == Account.TYPE_EQUITY // ||
	 * type == Account.TYPE_ACCOUNT_PAYABLE) { //
	 * account.setIncrease(Boolean.TRUE); // } else { //
	 * account.setIncrease(Boolean.FALSE); // } // if (type ==
	 * Account.TYPE_INVENTORY_ASSET) { //
	 * account.setOpeningBalanceEditable(Boolean.FALSE); // }
	 * 
	 * session.save(account);
	 * 
	 * return null; } }); // List list = template // .find( //
	 * "from Account a where a.company = ? and a.name = ? and a.number = ?" // ,
	 * // new Object[] { account.getCompany(), // account.getName(),
	 * account.getNumber() });
	 * 
	 * // if(!account.getOpeningBalance().equals(0.0)){ //
	 * account.setOpeningBalanceEditable(Boolean.FALSE); // } // if (type ==
	 * Account.TYPE_ACCOUNT_RECEIVABLE // || type ==
	 * Account.TYPE_ACCOUNT_PAYABLE // || type == Account.TYPE_INVENTORY_ASSET)
	 * { // account.setOpeningBalanceEditable(Boolean.FALSE); // // } //
	 * template.save(account); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCashPurchase(final CashPurchase cashPurchase)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object template.execute(new
	 * HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException { SessionUtils.update(cashPurchase,
	 * session);
	 * 
	 * cashPurchase.setType(Transaction.TYPE_CASH_PURCHASE);
	 * session.save(cashPurchase); return null; }
	 * 
	 * }); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCashSales(final CashSales cashSales) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(cashSales, session);
	 * 
	 * cashSales.setType(Transaction.TYPE_CASH_SALES);
	 * 
	 * session.save(cashSales); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createExpense(final Expense expense) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(expense, session);
	 * 
	 * // expense.setType(Transaction.TYPE_EXPENSE);
	 * 
	 * session.save(expense); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPayExpense(final PayExpense payExpense)
	 * throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(payExpense, session);
	 * 
	 * // payExpense.setType(Transaction.TYPE_PAY_EXPENSE);
	 * 
	 * session.save(payExpense); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCompany(final Company company) throws
	 * DAOException { try { final HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.save(company); switch (company.getAccountingType()) { case
	 * Company.ACCOUNTING_TYPE_US: createUSDefaultTaxGroup(company); break; case
	 * Company.ACCOUNTING_TYPE_UK: createUKDefaultVATCodesAndVATAgency(company);
	 * break; case Company.ACCOUNTING_TYPE_INDIA: break; }
	 * 
	 * } catch (DataAccessException e) { e.printStackTrace(); throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * private void createUKDefaultVATCodesAndVATAgency(Company company) {
	 * 
	 * try { TaxAgency defaultVATAgency = new TaxAgency();
	 * defaultVATAgency.setCompany(company);
	 * defaultVATAgency.setActive(Boolean.FALSE);
	 * defaultVATAgency.setName(company.getPreferences()
	 * .getVATtaxAgencyName()); //
	 * defaultVATAgency.setPaymentTerm(accounterDao.getPaymentTerms( //
	 * company.getId(), "Quarterly"));
	 * defaultVATAgency.setSalesVATAccount(accounterDao.getAccount(company
	 * .getId(), AccounterConstants.SALES_OUTPUT_VAT));
	 * defaultVATAgency.setPurchasesVATAccount(accounterDao.getAccount(
	 * company.getId(), AccounterConstants.PURCHASE_INPUT_VAT));
	 * 
	 * this.createTaxAgency(defaultVATAgency);
	 * 
	 * Date asOf = new Date();
	 * 
	 * TaxCode defaultVATCode1 = new TaxCode();
	 * defaultVATCode1.setCompany(company);
	 * defaultVATCode1.setIsActive(Boolean.TRUE); defaultVATCode1.setName("E");
	 * defaultVATCode1.setDescription("Tax Exempt");
	 * defaultVATCode1.setTaxAgency(defaultVATAgency); Set<TaxRates> taxRates1 =
	 * new HashSet<TaxRates>(); TaxRates taxRateA = new TaxRates();
	 * taxRateA.setRate(0); taxRateA.setAsOf(asOf); taxRates1.add(taxRateA);
	 * defaultVATCode1.setTaxRates(taxRates1);
	 * this.createTaxCode(defaultVATCode1);
	 * 
	 * TaxCode defaultVATCode2 = new TaxCode();
	 * defaultVATCode2.setCompany(company);
	 * defaultVATCode2.setIsActive(Boolean.TRUE); defaultVATCode2.setName("R");
	 * defaultVATCode2.setDescription("Reduced");
	 * defaultVATCode2.setTaxAgency(defaultVATAgency); Set<TaxRates> taxRates2 =
	 * new HashSet<TaxRates>(); TaxRates taxRateB = new TaxRates();
	 * taxRateB.setRate(5); taxRateB.setAsOf(asOf); taxRates2.add(taxRateB);
	 * defaultVATCode2.setTaxRates(taxRates2);
	 * this.createTaxCode(defaultVATCode2);
	 * 
	 * TaxCode defaultVATCode3 = new TaxCode();
	 * defaultVATCode3.setCompany(company);
	 * defaultVATCode3.setIsActive(Boolean.TRUE); defaultVATCode3.setName("S");
	 * defaultVATCode3.setDescription("Standard rated");
	 * defaultVATCode3.setTaxAgency(defaultVATAgency); Set<TaxRates> taxRates3 =
	 * new HashSet<TaxRates>(); TaxRates taxRateC = new TaxRates();
	 * taxRateC.setRate(17.5); taxRateC.setAsOf(asOf); taxRates3.add(taxRateC);
	 * defaultVATCode3.setTaxRates(taxRates3);
	 * this.createTaxCode(defaultVATCode3);
	 * 
	 * TaxCode defaultVATCode4 = new TaxCode();
	 * defaultVATCode4.setCompany(company);
	 * defaultVATCode4.setIsActive(Boolean.TRUE); defaultVATCode4.setName("Z");
	 * defaultVATCode4.setDescription("Zero rated");
	 * defaultVATCode4.setTaxAgency(defaultVATAgency); Set<TaxRates> taxRates4 =
	 * new HashSet<TaxRates>(); TaxRates taxRateD = new TaxRates();
	 * taxRateD.setRate(0); taxRateD.setAsOf(asOf); taxRates4.add(taxRateD);
	 * defaultVATCode4.setTaxRates(taxRates4);
	 * this.createTaxCode(defaultVATCode4);
	 * 
	 * } catch (DAOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * private void createUSDefaultTaxGroup(Company company) {
	 * 
	 * try { // Default TaxGroup Creation
	 * 
	 * TaxAgency defaultTaxAgency = new TaxAgency();
	 * defaultTaxAgency.setCompany(company);
	 * defaultTaxAgency.setActive(Boolean.FALSE);
	 * defaultTaxAgency.setName("Tax Agency");
	 * 
	 * defaultTaxAgency.setPaymentTerm(accounterDao.getPaymentTerms(
	 * company.getId(), "Quarterly"));
	 * defaultTaxAgency.setLiabilityAccount(accounterDao.getAccount(
	 * company.getId(), "Sales Tax Payable"));
	 * 
	 * this.createTaxAgency(defaultTaxAgency);
	 * 
	 * Set<TaxRates> taxRates = new HashSet<TaxRates>();
	 * 
	 * TaxRates taxRate = new TaxRates(); taxRate.setRate(0);
	 * taxRate.setAsOf(new Date()); taxRates.add(taxRate);
	 * 
	 * TaxCode defaultTaxCode = new TaxCode();
	 * defaultTaxCode.setCompany(company);
	 * defaultTaxCode.setIsActive(Boolean.TRUE); defaultTaxCode.setName("None");
	 * defaultTaxCode.setTaxAgency(defaultTaxAgency);
	 * defaultTaxCode.setTaxRates(taxRates);
	 * 
	 * this.createTaxCode(defaultTaxCode);
	 * 
	 * TaxGroup defaultTaxGroup = new TaxGroup();
	 * defaultTaxGroup.setCompany(company); defaultTaxGroup.setName("None");
	 * 
	 * Set<TaxCode> taxCodes = new HashSet<TaxCode>();
	 * taxCodes.add(defaultTaxCode); defaultTaxGroup.setTaxCodes(taxCodes);
	 * 
	 * this.createTaxGroup(defaultTaxGroup); } catch (DAOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * @Override public void createCreditCardCharge(final CreditCardCharge
	 * creditCardCharge) throws DAOException { try { HibernateTemplate template
	 * = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(creditCardCharge, session);
	 * 
	 * creditCardCharge .setType(Transaction.TYPE_CREDIT_CARD_CHARGE);
	 * session.save(creditCardCharge); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCreditRating(CreditRating creditRating)
	 * throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * template.save(creditRating);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createCurrency(Currency currency) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from Currency cu where cu.formalName=? and cu.company=? ", new Object[]
	 * { currency.getFormalName(), currency.getCompany() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(currency); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCustomer(Customer customer) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from Customer a where a.company = ? and a.name = ?", new Object[] {
	 * customer.getCompany(), customer.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * 
	 * if (customer.getCompany().getAccountingType() ==
	 * Company.ACCOUNTING_TYPE_US) { List<TaxAgency> taxAgencies = accounterDao
	 * .getTaxAgencies(customer.getCompany().getId());
	 * 
	 * for (TaxAgency taxAgency : taxAgencies) {
	 * 
	 * if (taxAgency.getLiabilityAccount().getName() .equalsIgnoreCase(
	 * AccounterConstants.OPENING_BALANCE)) {
	 * 
	 * if (list.size() > 0 && customer.getBalance() > 0.0 &&
	 * customer.getTaxGroup() != null) {
	 * 
	 * throw (new DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null));
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * customer.setType(Payee.TYPE_CUSTOMER);
	 * customer.setDate(customer.getPayeeSince()); template.save(customer);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * public void createJournalEntry(JournalEntry journalEntry) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * journalEntry.setType(Transaction.TYPE_JOURNAL_ENTRY);
	 * 
	 * template.save(journalEntry); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCustomerCreditMemo(final CustomerCreditMemo
	 * creditMemo) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);
	 * 
	 * SessionUtils.update(creditMemo, session);
	 * 
	 * session.save(creditMemo);
	 * 
	 * return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCustomerGroup(CustomerGroup customerGroup)
	 * throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from CustomerGroup cg where cg.name = ? and cg.company = ?", new
	 * Object[] { customerGroup.getName(), customerGroup.getCompany() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(customerGroup); } catch (DataAccessException e) { throw
	 * (new DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createCustomerRefunds(final CustomerRefund
	 * customerRefunds) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * customerRefunds.setType(Transaction.TYPE_CUSTOMER_REFUNDS);
	 * 
	 * SessionUtils.update(customerRefunds, session);
	 * session.save(customerRefunds); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createEnterBill(final EnterBill enterBill) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(enterBill, session);
	 * enterBill.setType(Transaction.TYPE_ENTER_BILL);
	 * enterBill.setBalanceDue(enterBill.getTotal()); session.save(enterBill);
	 * return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createEstimate(Estimate estimate) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * estimate.setType(Transaction.TYPE_ESTIMATE); template.save(estimate); }
	 * catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createInvoice(final Invoice invoice) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException { SessionUtils.update(invoice, session);
	 * 
	 * invoice.setType(Transaction.TYPE_INVOICE);
	 * 
	 * session.save(invoice); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createIssuePayment(IssuePayment issuePayment)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * issuePayment.setType(Transaction.TYPE_ISSUE_PAYMENT);
	 * 
	 * // Get the ID assigned to the Dummy WriteCheck or the // CustomerRefund
	 * Object and Set the Actual Object // After retrieving from Database
	 * Iterator<TransactionIssuePayment> it = issuePayment
	 * .getTransactionIssuePayment().iterator();
	 * 
	 * while (it.hasNext()) {
	 * 
	 * TransactionIssuePayment transactionIssuePayment = it.next();
	 * 
	 * if (transactionIssuePayment.getWriteCheck() != null) {
	 * 
	 * Long writeCheckId = transactionIssuePayment.getWriteCheck() .getId();
	 * 
	 * WriteCheck writeCheck = accounterDao.getwriterCheck(
	 * transactionIssuePayment.getTransaction() .getCompany().getId(),
	 * writeCheckId);
	 * 
	 * if (writeCheck == null) throw new
	 * DAOException(DAOException.DATABASE_EXCEPTION, new Exception(),
	 * "WriteCheck Expected, But Found Null");
	 * 
	 * transactionIssuePayment.setWriteCheck(writeCheck); } else {
	 * 
	 * Long customerRefundId = transactionIssuePayment
	 * .getCustomerRefund().getId();
	 * 
	 * CustomerRefund customerRefund = accounterDao
	 * .getCustomerRefunds(transactionIssuePayment
	 * .getTransaction().getCompany().getId(), customerRefundId);
	 * 
	 * if (customerRefund == null) throw new
	 * DAOException(DAOException.DATABASE_EXCEPTION, new Exception(),
	 * "Customer Refund Expected, But Found Null");
	 * transactionIssuePayment.setCustomerRefund(customerRefund);
	 * 
	 * } }
	 * 
	 * template.save(issuePayment); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createItem(Item item) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from Item i where i.company = ? and i.name = ?", new Object[] {
	 * item.getCompany(), item.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(item); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createItemGroup(ItemGroup itemGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from ItemGroup i where i.company = ? and i.name = ?", new Object[] {
	 * itemGroup.getCompany(), itemGroup.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(itemGroup); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createMakeDeposit(final MakeDeposit makeDeposit)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(makeDeposit, session);
	 * 
	 * makeDeposit.setType(Transaction.TYPE_MAKE_DEPOSIT);
	 * session.save(makeDeposit); return null; } });
	 * 
	 * // Iterator<TransactionMakeDeposit> it = makeDeposit //
	 * .getTransactionMakeDeposit().iterator(); // // Iterator i =
	 * makeDeposit.getTransactionMakeDeposit().iterator(); // while
	 * (i.hasNext()) { // ((TransactionMakeDeposit) i.next()) //
	 * .setAccountsPayable(accounterDao.getAccount(makeDeposit // .getCompany(),
	 * // AccounterConstants.ACCOUNTS_PAYABLE)); // } // i =
	 * makeDeposit.getTransactionMakeDeposit().iterator(); // while
	 * (i.hasNext()) { // ((TransactionMakeDeposit) i.next()) //
	 * .setAccountsReceivable(accounterDao.getAccount( //
	 * makeDeposit.getCompany(), // AccounterConstants.ACCOUNTS_RECEIVABLE)); //
	 * }
	 * 
	 * // Excess Information need to be set to this Object
	 * makeDeposit.setType(Transaction.TYPE_MAKE_DEPOSIT);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPayBill(final PayBill payBill) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(payBill, session);
	 * 
	 * payBill.setType(Transaction.TYPE_PAY_BILL);
	 * 
	 * if (payBill.getPayBillType() == PayBill.TYPE_VENDOR_PAYMENT) {
	 * payBill.setUnusedAmount(payBill.getTotal()); } // else { //
	 * Iterator<TransactionPayBill> it = payBill //
	 * .getTransactionPayBill().iterator(); // // while (it.hasNext()) { //
	 * TransactionPayBill transactionPayBill = // (TransactionPayBill) it //
	 * .next(); // // EnterBill enterBill = transactionPayBill //
	 * .getEnterBill(); // if (enterBill != null) { // try { //
	 * transactionPayBill // .setEnterBill(accounterDao // .getEnterBill(payBill
	 * // .getCompany() // .getId(), enterBill // .getId())); // } catch
	 * (DAOException e) { // // TODO Auto-generated catch block //
	 * e.printStackTrace(); // } // } else { // // TransactionMakeDeposit
	 * txMakeDeposit = transactionPayBill // .getTransactionMakeDeposit(); // //
	 * try { // transactionPayBill // .setTransactionMakeDeposit(accounterGUIDao
	 * // .getTransactionMakeDeposit( // payBill // .getCompany() // .getId(),
	 * // txMakeDeposit // .getId())); // } catch (DAOException e) { // // TODO
	 * Auto-generated catch block // e.printStackTrace(); // } // // } // // }
	 * // // }
	 * 
	 * session.save(payBill); return null; }
	 * 
	 * });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPaymentTerms(PaymentTerms paymentTerms)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from PaymentTerms pt where pt.company=? and pt.name=?", new Object[] {
	 * paymentTerms.getCompany(), paymentTerms.getName() }); if (list.size() >
	 * 0) {
	 * 
	 * throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null));
	 * 
	 * } template.save(paymentTerms); } catch (DataAccessException e) { throw
	 * (new DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPriceLevel(PriceLevel priceLevel) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from PriceLevel pl where pl.company = ? and pl.name = ?", new Object[] {
	 * priceLevel.getCompany(), priceLevel.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(priceLevel); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPurchaseOrder(PurchaseOrder purchaseOrder)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate(); template.save(purchaseOrder); } catch
	 * (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createReceivePayment(final ReceivePayment
	 * receivePayment) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(receivePayment, session);
	 * 
	 * // Excess Information need to be set to this Object
	 * receivePayment.setType(Transaction.TYPE_RECEIVE_PAYMENT);
	 * 
	 * session.save(receivePayment); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createSalesOrder(SalesOrder salesOrder) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * // Excess Information need to be set to this Object //
	 * salesOrder.setType();
	 * 
	 * template.save(salesOrder); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createSalesPerson(SalesPerson salesPerson) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from SalesPerson sr where sr.company = ? and sr.firstName = ?", new
	 * Object[] { salesPerson.getCompany(), salesPerson.getFirstName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * salesPerson.setType(Payee.TYPE_EMPLOYEE); template.save(salesPerson); }
	 * catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createShippingMethod(ShippingMethod shippingMethod)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from ShippingMethod sm where sm.company = ? and sm.name = ?", new
	 * Object[] { shippingMethod.getCompany(), shippingMethod.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(shippingMethod); } catch (DataAccessException e) { throw
	 * (new DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createShippingTerms(ShippingTerms shippingTerms)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from ShippingTerms st where st.company = ? and st.name = ?", new
	 * Object[] { shippingTerms.getCompany(), shippingTerms.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(shippingTerms); } catch (DataAccessException e) { throw
	 * (new DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createTaxAgency(TaxAgency taxAgency) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template .find(
	 * "from TaxAgency ta where ta.company = ? and ta.name = ?", new Object[] {
	 * taxAgency.getCompany(), taxAgency.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * taxAgency.setType(Payee.TYPE_TAX_AGENCY); template.save(taxAgency); }
	 * catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createTaxCode(TaxCode taxCode) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from TaxCode tc where tc.company = ? and tc.name = ?", new Object[] {
	 * taxCode.getCompany(), taxCode.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); } if
	 * (taxCode.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK)
	 * { Iterator it = taxCode.getCompany().getTaxAgencies().iterator(); if
	 * (it.hasNext()) { taxCode.setTaxAgency((TaxAgency) it.next()); } }
	 * template.save(taxCode);
	 * 
	 * if (taxCode.getCompany().getAccountingType() == 1) {
	 * 
	 * TaxGroup taxGroup = new TaxGroup(); taxGroup.setName(taxCode.getName());
	 * taxGroup.setCompany(taxCode.getCompany()); Set<TaxCode> taxCodes = new
	 * HashSet<TaxCode>();
	 * 
	 * taxCodes.add(taxCode);
	 * 
	 * taxGroup.setTaxCodes(taxCodes);
	 * 
	 * template.save(taxGroup);
	 * 
	 * }
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createTaxGroup(TaxGroup taxGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from TaxGroup tg where tg.company = ? and tg.name = ?", new Object[] {
	 * taxGroup.getCompany(), taxGroup.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(taxGroup); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createTaxRates(TaxRates taxRates) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from TaxRates tr where tr.company = ? and tr.rate = ?", new Object[] {
	 * taxRates.getCompany(), taxRates.getRate() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(taxRates); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createTransferFund(final TransferFund transferFund)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * transferFund.setType(Transaction.TYPE_TRANSFER_FUND);
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(transferFund, session);
	 * 
	 * session.save(transferFund); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createUnitOfMeasure(UnitOfMeasure untiOfMeasure)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from UnitOfMeasure u where u.company = ? and u.name = ?", new Object[] {
	 * untiOfMeasure.getCompany(), untiOfMeasure.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(untiOfMeasure); } catch (DataAccessException e) { throw
	 * (new DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createUser(User user) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate();
	 * 
	 * 
	 * List list = template.find(
	 * "from User u where u.company = ? and u.name = ?", new Object[] {
	 * user.getCompany(), user.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * 
	 * 
	 * template.save(user); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void createVendor(Vendor vendor) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from Vendor v where v.company = ? and v.name = ?", new Object[] {
	 * vendor.getCompany(), vendor.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * vendor.setType(Payee.TYPE_VENDOR);
	 * vendor.setDate(vendor.getPayeeSince()); template.save(vendor); } catch
	 * (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createVendorCreditMemo(final VendorCreditMemo
	 * vendorCreditMemo) throws DAOException { try { HibernateTemplate template
	 * = getHibernateTemplate();
	 * 
	 * // Excess Information need to be set to this Object
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(vendorCreditMemo, session);
	 * 
	 * vendorCreditMemo .setType(Transaction.TYPE_VENDOR_CREDIT_MEMO);
	 * session.save(vendorCreditMemo); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from VendorGroup vg where vg.company = ? and vg.name = ?", new Object[]
	 * { vendorGroup.getCompany(), vendorGroup.getName() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(vendorGroup); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createWriteCheck(final WriteCheck writeCheck)
	 * throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(writeCheck, session);
	 * writeCheck.setType(Transaction.TYPE_WRITE_CHECK);
	 * 
	 * if (writeCheck.isToBePrinted()) { writeCheck
	 * .setStatus(Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED); }
	 * else { writeCheck
	 * .setStatus(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED); }
	 * session.save(writeCheck); return null; } }); // Excess Information need
	 * to be set to this Object
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteAccount(Account account) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Account a where a.id = ?", new Object[] {
	 * account.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(account);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCashPurchase(CashPurchase cashPurchase)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from CashPurchase cp where cp.id = ?", new
	 * Object[] { cashPurchase.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(cashPurchase);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCashSales(CashSales cashSales) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from CashSales cs where cs.id = ?", new
	 * Object[] { cashSales.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(cashSales);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCompany(Company company) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Company c where c.id = ?", new Object[] {
	 * company.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(company);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCreditCardCharge(CreditCardCharge
	 * creditCardCharge) throws DAOException { try { HibernateTemplate template
	 * = getHibernateTemplate();
	 * 
	 * List list = template.find( "from CreditCardCharge cc where cc.id = ?",
	 * new Object[] { creditCardCharge.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(creditCardCharge);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCreditRating(CreditRating creditRating)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from CreditRating cr where cr.id = ?", new
	 * Object[] { creditRating.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(creditRating);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCurrency(Currency currency) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Currency c where c.id = ?", new Object[]
	 * { currency.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(currency);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCustomer(Customer customer) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Customer c where c.id = ?", new Object[]
	 * { customer.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(customer);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCustomerCreditMemo(CustomerCreditMemo
	 * creditMemo) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find( "from CustomerCreditMemo cm where cm.id = ?",
	 * new Object[] { creditMemo.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(creditMemo);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCustomerGroup(CustomerGroup customerGroup)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from CustomerGroup c where c.id = ?", new
	 * Object[] { customerGroup.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(customerGroup);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteCustomerRefunds(CustomerRefund
	 * customerRefunds) throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from CustomerRefund cr where cr.id = ?", new
	 * Object[] { customerRefunds.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(customerRefunds);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void deleteEnterBill(EnterBill enterBill) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from EnterBill e where e.id = ?", new Object[]
	 * { enterBill.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(enterBill);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteEstimate(Estimate estimate) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Estimate e where e.id = ?", new Object[]
	 * { estimate.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(estimate);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteInvoice(Invoice invoice) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Invoice i where i.id = ?", new Object[] {
	 * invoice.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(invoice);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteIssuePayment(IssuePayment issuePayment)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from IssuePayment ip where ip.id = ?", new
	 * Object[] { issuePayment.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(issuePayment);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteItem(Item item) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Item i where i.id = ?", new Object[] {
	 * item.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(item);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteItemGroup(ItemGroup itemGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from ItemGroup i where i.id = ?", new Object[]
	 * { itemGroup.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(itemGroup);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteMakeDeposit(MakeDeposit makeDeposit) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from MakeDeposit md where md.id = ?", new
	 * Object[] { makeDeposit.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(makeDeposit);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deletePayBill(PayBill payBill) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from PayBill p where p.id = ?", new Object[] {
	 * payBill.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(payBill);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deletePaymentTerms(PaymentTerms paymentTerms)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from PaymentTerms st where st.id = ?", new
	 * Object[] { paymentTerms.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(paymentTerms);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deletePriceLevel(PriceLevel priceLevel) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from PriceLevel pl where pl.id = ?", new
	 * Object[] { priceLevel.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(priceLevel);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deletePurchaseOrder(PurchaseOrder purchaseOrder)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from PurchaseOrder p where p.id = ?", new
	 * Object[] { purchaseOrder.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(purchaseOrder);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteReceivePayment(ReceivePayment receivePayment)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from ReceivePayment r where r.id = ?", new
	 * Object[] { receivePayment.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(receivePayment);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteSalesOrder(SalesOrder salesOrder) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from SalesOrder s where s.id = ?", new
	 * Object[] { salesOrder.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(salesOrder);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteSalesPerson(SalesPerson salesPerson) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from SalesPerson sr where sr.id = ?", new
	 * Object[] { salesPerson.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(salesPerson);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteShippingMethod(ShippingMethod shippingMethod)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from ShippingMethod sm where sm.id = ?", new
	 * Object[] { shippingMethod.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(shippingMethod);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteShippingTerms(ShippingTerms shippingTerms)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from ShippingTerms st where st.id = ?", new
	 * Object[] { shippingTerms.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(shippingTerms);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteTaxAgency(TaxAgency taxAgency) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from TaxAgency t where t.id = ?", new Object[]
	 * { taxAgency.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(taxAgency);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteTaxCode(TaxCode taxCode) throws DAOException
	 * { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from TaxCode tc where tc.id = ?", new Object[]
	 * { taxCode.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(taxCode);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteTaxGroup(TaxGroup taxGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from TaxGroup tg where tg.id = ?", new
	 * Object[] { taxGroup.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(taxGroup);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteTaxRates(TaxRates taxRates) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from TaxRates tr where tr.id = ?", new
	 * Object[] { taxRates.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(taxRates);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteTransferFund(TransferFund transferFund)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from TransferFund tf where tf.id = ?", new
	 * Object[] { transferFund.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(transferFund);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteUnitOfMeasure(UnitOfMeasure untiOfMeasure)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * List list = template.find("from UnitOfMeasure u where u.id = ?", new
	 * Object[] { untiOfMeasure.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(untiOfMeasure);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteUser(User user) throws DAOException { try {
	 * HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from User a where a.id = ?", new Object[] {
	 * user.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(user);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); } }
	 * 
	 * @Override public void deleteVendor(Vendor vendor) throws DAOException {
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from Vendor v where v.id = ?", new Object[] {
	 * vendor.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(vendor);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteVendorCreditMemo(VendorCreditMemo
	 * vendorCreditMemo) throws DAOException { try { HibernateTemplate template
	 * = getHibernateTemplate();
	 * 
	 * List list = template.find("from VendorCreditMemo v where v.id = ?", new
	 * Object[] { vendorCreditMemo.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(vendorCreditMemo);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteVendorGroup(VendorGroup vendorGroup) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from VendorGroup vg where vg.id = ?", new
	 * Object[] { vendorGroup.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(vendorGroup);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteWriteCheck(WriteCheck writeCheck) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from WriteCheck wc where wc.id = ?", new
	 * Object[] { writeCheck.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(writeCheck);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterJournalEntry(JournalEntry journalEntry) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from JournalEntry je where je.id= ? ", new
	 * Object[] { journalEntry.getId() }); if (list.size() > 0) { JournalEntry
	 * existingJournalEntry = (JournalEntry) list.get(0);
	 * 
	 * if (journalEntry.getId() != (existingJournalEntry.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(journalEntry);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterFiscalYear(final FiscalYear fiscalYear) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from FiscalYear f where f.id= ? ", new
	 * Object[] { fiscalYear.getId() }); if (list.size() > 0) { FiscalYear
	 * existingFiscalYear = (FiscalYear) list.get(0);
	 * 
	 * if (fiscalYear.getId() != (existingFiscalYear.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } } if
	 * (fiscalYear.getStartDate().equals( fiscalYear.getPreviousStartDate())) {
	 * template.saveOrUpdate(fiscalYear); } else if
	 * (!fiscalYear.getStartDate().equals( fiscalYear.getPreviousStartDate())) {
	 * Date modifiedStartDate = fiscalYear.getStartDate(); Date
	 * existingLeastStartDate = modifiedStartDate; Date existingHighestEndDate =
	 * modifiedStartDate; Boolean exist = Boolean.FALSE; list =
	 * template.find("from FiscalYear f"); if (list.size() > 0) { Iterator i =
	 * list.iterator(); if (i.hasNext()) { FiscalYear fs = (FiscalYear)
	 * i.next(); existingLeastStartDate = fs.getStartDate();
	 * existingHighestEndDate = fs.getEndDate(); } i = list.iterator(); while
	 * (i.hasNext()) { FiscalYear fs = (FiscalYear) i.next(); if
	 * (modifiedStartDate.after(fs.getStartDate()) &&
	 * modifiedStartDate.before(fs.getEndDate())) { exist = Boolean.TRUE; break;
	 * } if (fs.getStartDate().before(existingLeastStartDate)) {
	 * existingLeastStartDate = fs.getStartDate(); } if
	 * (fs.getEndDate().after(existingHighestEndDate)) { existingHighestEndDate
	 * = fs.getEndDate(); }
	 * 
	 * } if (!exist) { Calendar cal = Calendar.getInstance();
	 * cal.setTime(modifiedStartDate); Integer modifiedYear =
	 * cal.get(Calendar.YEAR);
	 * 
	 * cal.setTime(existingLeastStartDate); Integer existingLeastYear =
	 * cal.get(Calendar.YEAR);
	 * 
	 * cal.setTime(existingHighestEndDate); Integer existingHighestYear =
	 * cal.get(Calendar.YEAR); if
	 * (modifiedStartDate.before(existingLeastStartDate)) { int diff =
	 * existingLeastYear - modifiedYear; for (int k = 0; k < diff; k++) {
	 * 
	 * cal.set(modifiedYear + k, 0, 1); Date startDate = cal.getTime();
	 * 
	 * cal.set(modifiedYear + k, 11, 31); Date endDate = cal.getTime();
	 * 
	 * FiscalYear fs = new FiscalYear(); fs.setCompany(fiscalYear.getCompany());
	 * fs.setStartDate(startDate); fs.setPreviousStartDate(startDate);
	 * fs.setEndDate(endDate); fs.setStatus(FiscalYear.STATUS_OPEN);
	 * fs.setIsCurrentFiscalYear(Boolean.FALSE); this.createFiscalYear(fs); }
	 * 
	 * } else if (modifiedStartDate .after(existingHighestEndDate)) { int diff =
	 * modifiedYear - existingLeastYear; for (int k = 1; k <= diff; k++) {
	 * cal.set(existingLeastYear + k, 0, 1); Date startDate = cal.getTime();
	 * 
	 * cal.set(existingLeastYear + k, 11, 31); Date endDate = cal.getTime();
	 * FiscalYear fs = new FiscalYear(); fs.setCompany(fiscalYear.getCompany());
	 * fs.setStartDate(startDate); fs.setPreviousStartDate(startDate);
	 * fs.setEndDate(endDate); fs.setStatus(FiscalYear.STATUS_OPEN);
	 * fs.setIsCurrentFiscalYear(Boolean.FALSE); this.createFiscalYear(fs); } }
	 * 
	 * } fiscalYear.setStartDate(fiscalYear.getPreviousStartDate());
	 * template.saveOrUpdate(fiscalYear);
	 * 
	 * } } else if (fiscalYear.getStatus() == FiscalYear.STATUS_CLOSE) { if
	 * (!fiscalYear.getIsCurrentFiscalYear()) { throw (new DAOException(
	 * DAOException.INVALID_REQUEST_EXCEPTION, null)); } else {
	 * template.saveOrUpdate(fiscalYear); } }
	 * 
	 * // template.execute(new HibernateCallback(){ // // @Override // public
	 * Object doInHibernate(Session session) // throws HibernateException,
	 * SQLException { // // SessionUtils.update(fiscalYear, session); // if
	 * (fiscalYear.getStartDate().equals( // fiscalYear.getPreviousStartDate()))
	 * { // session.saveOrUpdate(fiscalYear); // } else if
	 * (!fiscalYear.getStartDate().equals( //
	 * fiscalYear.getPreviousStartDate())) { // Date modifiedStartDate =
	 * fiscalYear.getStartDate(); // Date existingLeastStartDate =
	 * modifiedStartDate; // Date existingHighestEndDate = modifiedStartDate; //
	 * Boolean exist = Boolean.FALSE; // final List<FiscalYear> list = //
	 * session.createQuery("from FiscalYear f").list(); // if (list.size() > 0)
	 * { // Iterator i = list.iterator(); // if (i.hasNext()) { // FiscalYear fs
	 * = (FiscalYear) i.next(); // existingLeastStartDate = fs.getStartDate();
	 * // existingHighestEndDate = fs.getEndDate(); // } // i = list.iterator();
	 * // while (i.hasNext()) { // FiscalYear fs = (FiscalYear) i.next(); // if
	 * (modifiedStartDate.after(fs.getStartDate()) // &&
	 * modifiedStartDate.before(fs.getEndDate())) { // exist = Boolean.TRUE; //
	 * break; // } // if (fs.getStartDate().before(existingLeastStartDate)) { //
	 * existingLeastStartDate = fs.getStartDate(); // } // if
	 * (fs.getEndDate().after(existingHighestEndDate)) { //
	 * existingHighestEndDate = fs.getEndDate(); // } // // } // if (!exist) {
	 * // Calendar cal = Calendar.getInstance(); //
	 * cal.setTime(modifiedStartDate); // Integer modifiedYear =
	 * cal.get(Calendar.YEAR); // // cal.setTime(existingLeastStartDate); //
	 * Integer existingLeastYear = cal.get(Calendar.YEAR); // //
	 * cal.setTime(existingHighestEndDate); // Integer existingHighestYear =
	 * cal.get(Calendar.YEAR); // if
	 * (modifiedStartDate.before(existingLeastStartDate)) { // int diff =
	 * existingLeastYear - modifiedYear; // for (int k = 0; k < diff; k++) { //
	 * // cal.set(modifiedYear + k, 0, 1); // Date startDate = cal.getTime(); //
	 * // cal.set(modifiedYear + k, 11, 31); // Date endDate = cal.getTime(); //
	 * // FiscalYear fs = new FiscalYear(); //
	 * fs.setCompany(fiscalYear.getCompany()); // fs.setStartDate(startDate); //
	 * fs.setPreviousStartDate(startDate); // fs.setEndDate(endDate); //
	 * fs.setStatus(FiscalYear.STATUS_OPEN); //
	 * fs.setIsCurrentFiscalYear(Boolean.FALSE); // session.save(fs); // } // //
	 * } else if (modifiedStartDate // .after(existingHighestEndDate)) { // int
	 * diff = modifiedYear - existingLeastYear; // for (int k = 1; k <= diff;
	 * k++) { // cal.set(existingLeastYear + k, 0, 1); // Date startDate =
	 * cal.getTime(); // // cal.set(existingLeastYear + k, 11, 31); // Date
	 * endDate = cal.getTime(); // FiscalYear fs = new FiscalYear(); //
	 * fs.setCompany(fiscalYear.getCompany()); // fs.setStartDate(startDate); //
	 * fs.setPreviousStartDate(startDate); // fs.setEndDate(endDate); //
	 * fs.setStatus(FiscalYear.STATUS_OPEN); //
	 * fs.setIsCurrentFiscalYear(Boolean.FALSE); // session.save(fs); // } // }
	 * // // } // fiscalYear.setStartDate(fiscalYear.getPreviousStartDate()); //
	 * session.saveOrUpdate(fiscalYear); // // } // } else if
	 * (fiscalYear.getStatus() == FiscalYear.STATUS_CLOSE) { // if
	 * (!fiscalYear.getIsCurrentFiscalYear()) { // try { // throw (new
	 * DAOException( // DAOException.INVALID_REQUEST_EXCEPTION, null)); // }
	 * catch (DAOException e) { // // TODO Auto-generated catch block //
	 * e.printStackTrace(); // } // } else { //
	 * session.saveOrUpdate(fiscalYear); // } // } // return null; // }});
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createFiscalYear(FiscalYear fiscalYear) throws
	 * DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * fiscalYear.setPreviousStartDate(fiscalYear.getStartDate());
	 * fiscalYear.setStatus(FiscalYear.STATUS_OPEN);
	 * 
	 * template.save(fiscalYear); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteFiscalYear(FiscalYear fiscalYear) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from FiscalYear f where f.id = ?", new
	 * Object[] { fiscalYear.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(fiscalYear);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createBank(Bank bank) throws DAOException {
	 * 
	 * try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find(
	 * "from Bank b where b.name = ? and b.company = ?", new Object[] {
	 * bank.getName(), bank.getCompany() });
	 * 
	 * if (list.size() > 0) { throw (new
	 * DAOException(DAOException.INVALID_REQUEST_EXCEPTION, null)); }
	 * template.save(bank); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createPaySalesTax(final PaySalesTax paySalesTax)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * 
	 * SessionUtils.update(paySalesTax, session);
	 * 
	 * // Excess Information need to be set to this Object
	 * paySalesTax.setType(Transaction.TYPE_PAY_SALES_TAX);
	 * session.save(paySalesTax); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterPaySalesTax(PaySalesTax paySalesTax) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * List list = template.find("from PaySalesTax pst where pst.id= ? ", new
	 * Object[] { paySalesTax.getId() }); if (list.size() > 0) { PaySalesTax
	 * existingPaySalesTax = (PaySalesTax) list.get(0);
	 * 
	 * if (paySalesTax.getId() != (existingPaySalesTax.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.saveOrUpdate(paySalesTax);
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createFixedAsset(final FixedAsset fixedAsset)
	 * throws DAOException { try { HibernateTemplate template =
	 * getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException { SessionUtils.update(fixedAsset,
	 * session);
	 * 
	 * session.save(fixedAsset); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void alterFixedAsset(FixedAsset fixedAsset) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from FixedAsset fa where fa.id= ? ", new
	 * Object[] { fixedAsset.getId() }); if (list.size() > 0) { FixedAsset
	 * existingFixedAsset = (FixedAsset) list.get(0);
	 * 
	 * if (fixedAsset.getId() != (existingFixedAsset.getId())) { throw (new
	 * DAOException( DAOException.INVALID_REQUEST_EXCEPTION, null)); } }
	 * template.update(fixedAsset); } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void deleteFixedAsset(FixedAsset fixedAsset) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * List list = template.find("from FixedAsset fa where fa.id = ?", new
	 * Object[] { fixedAsset.getId() });
	 * 
	 * if (list.size() > 0) { template.delete(fixedAsset);
	 * 
	 * } else throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
	 * null));
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 * 
	 * @Override public void createSellingOrDisposingFixedAsset( final
	 * SellingOrDisposingFixedAsset sellingOrDisposingFixedAsset) throws
	 * DAOException { try { HibernateTemplate template = getHibernateTemplate();
	 * 
	 * template.execute(new HibernateCallback() {
	 * 
	 * @Override public Object doInHibernate(Session session) throws
	 * HibernateException, SQLException {
	 * SessionUtils.update(sellingOrDisposingFixedAsset, session);
	 * 
	 * session.save(sellingOrDisposingFixedAsset); return null; } });
	 * 
	 * } catch (DataAccessException e) { throw (new
	 * DAOException(DAOException.DATABASE_EXCEPTION, e)); }
	 * 
	 * }
	 */
}
