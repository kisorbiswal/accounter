/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Budget;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CloneUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.CreditNotePDFTemplete;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ObjectConvertUtil;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.core.RecurringTransaction;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransactionMakeDepositEntries;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.managers.CompanyManager;
import com.vimukti.accounter.web.server.managers.CustomerManager;
import com.vimukti.accounter.web.server.managers.DashboardManager;
import com.vimukti.accounter.web.server.managers.FixedAssestManager;
import com.vimukti.accounter.web.server.managers.InventoryManager;
import com.vimukti.accounter.web.server.managers.Manager;
import com.vimukti.accounter.web.server.managers.PurchaseManager;
import com.vimukti.accounter.web.server.managers.ReportManager;
import com.vimukti.accounter.web.server.managers.SalesManager;
import com.vimukti.accounter.web.server.managers.TaxManager;
import com.vimukti.accounter.web.server.managers.UserManager;
import com.vimukti.accounter.web.server.managers.VendorManager;

/**
 * @author Fernandez
 * 
 */
public class FinanceTool {

	Logger log = Logger.getLogger(FinanceTool.class);
	private InventoryManager inventoryManager;
	private FixedAssestManager fixedAssestManager;
	private DashboardManager dashboardManager;
	private TaxManager taxManager;
	private PurchaseManager purchaseManager;
	private ReportManager reportManager;
	private VendorManager vendorManager;
	private CustomerManager customerManager;
	private SalesManager salesManager;
	private UserManager userManager;
	private Manager manager;
	private CompanyManager companyManager;

	/**
	 * This will Get Called when Create Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws AccounterException
	 */
	public long create(OperationContext createContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = createContext.getData();
			String userID = createContext.getUserEmail();
			Company company = getCompany(createContext.getCompanyId());
			User user = company.getUserByUserEmail(userID);

			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + createContext);
			}

			Class<IAccounterServerCore> serverClass = ObjectConvertUtil
					.getServerEqivalentClass(data.getClass());
			IAccounterServerCore serverObject;
			try {
				serverObject = serverClass.newInstance();
			} catch (Exception e1) {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}

			serverObject = new ServerConvertUtil().toServerObject(serverObject,
					(IAccounterCore) data, session);

			ObjectConvertUtil.setCompany((IAccounterServerCore) serverObject,
					company);

			// if (serverObject instanceof CreatableObject) {
			// // get the user from user id
			// User user = getCompany().getUserByUserEmail(userID);
			// Timestamp currentTime = new Timestamp(
			// System.currentTimeMillis());
			// ((CreatableObject) serverObject).setCreatedBy(user);
			//
			// ((CreatableObject) serverObject).setCreatedDate(currentTime);
			// ((CreatableObject) serverObject).setLastModifier(user);
			//
			// ((CreatableObject) serverObject)
			// .setLastModifiedDate(currentTime);
			// }
			getManager().canEdit(serverObject, data);

			isTransactionNumberExist((IAccounterCore) data, company);
			session.save(serverObject);
			transaction.commit();

			org.hibernate.Transaction newTransaction = session
					.beginTransaction();
			Activity activity = new Activity(company, user, ActivityType.ADD,
					serverObject);
			session.save(activity);
			if (serverObject instanceof Transaction) {
				((Transaction) serverObject).setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);

			newTransaction.commit();
			ChangeTracker.put(serverObject);

			return serverObject.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	/**
	 * This will Get Called when Update Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws AccounterException
	 */
	public long update(OperationContext updateContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		String userID = updateContext.getUserEmail();
		Company company = getCompany(updateContext.getCompanyId());
		User user = company.getUserByUserEmail(userID);
		try {
			IAccounterCore data = updateContext.getData();

			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + updateContext);
			}

			Class<?> classforName = ObjectConvertUtil
					.classforName(updateContext.getArg2());

			IAccounterServerCore serverObject = (IAccounterServerCore) session
					.get(classforName, Long.parseLong(updateContext.getArg1()));

			int version = serverObject.getVersion();
			if (version != data.getVersion()) {
				throw new AccounterException(
						AccounterException.ERROR_VERSION_MISMATCH);
			}

			IAccounterServerCore clonedObject = new CloneUtil<IAccounterServerCore>(
					IAccounterServerCore.class).clone(null, serverObject);

			ObjectConvertUtil.setCompany((IAccounterServerCore) clonedObject,
					company);
		
			getManager().canEdit(clonedObject, (IAccounterCore) data);

			isTransactionNumberExist((IAccounterCore) data, company);

			new ServerConvertUtil().toServerObject(serverObject,
					(IAccounterCore) data, session);

			serverObject.setVersion(++version);

			if (serverObject instanceof Transaction) {
				Transaction transaction = (Transaction) serverObject;
				transaction.onEdit((Transaction) clonedObject);

			}
			if (serverObject instanceof Lifecycle) {
				Lifecycle lifecycle = (Lifecycle) serverObject;
				lifecycle.onUpdate(session);
			}

			// if (serverObject instanceof Company) {
			// Company cmp = (Company) serverObject;
			// cmp.toCompany((ClientCompany) command.data);
			// ChangeTracker.put(cmp.toClientCompany());
			// }

			// called this method to save on unsaved objects in
			// session.
			// before going commit. because unsaved objects getting
			// update when transaction commit,
			// Util.loadObjectByStringID(session, command.arg2,
			// command.arg1);

			if ((IAccounterServerCore) serverObject instanceof CreatableObject) {
				// get the user from user id
				((CreatableObject) serverObject).setLastModifier(getCompany(
						updateContext.getCompanyId()).getUserByUserEmail(
						updateContext.getUserEmail()));

				((CreatableObject) serverObject)
						.setLastModifiedDate(new Timestamp(System
								.currentTimeMillis()));
			}

			session.saveOrUpdate(serverObject);
			hibernateTransaction.commit();

			org.hibernate.Transaction newTransaction = session
					.beginTransaction();

			Activity activity = new Activity(company, user, ActivityType.EDIT,
					serverObject);
			session.saveOrUpdate(activity);
			if (serverObject instanceof Transaction) {
				((Transaction) serverObject).setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);
			newTransaction.commit();
			ChangeTracker.put(serverObject);
			return serverObject.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}

	}

	/**
	 * This will Get Called when Delete Operation is Invoked by the Client
	 * 
	 * @param createContext
	 * @throws AccounterException
	 * @throws HibernateException
	 */
	public boolean delete(OperationContext context) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();

		String arg1 = (context).getArg1();
		String arg2 = (context).getArg2();

		if (arg1 == null || arg2 == null) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT,
					"Delete Operation Cannot be Processed id or cmd.arg2 Found Null...."
							+ context);
		}

		Class<?> clientClass = ObjectConvertUtil.getEqivalentClientClass(arg2);

		Class<?> serverClass = ObjectConvertUtil
				.getServerEqivalentClass(clientClass);

		IAccounterServerCore serverObject = (IAccounterServerCore) session.get(
				serverClass, Long.parseLong(arg1));

		// if (objects != null && objects.size() > 0) {

		// IAccounterServerCore serverObject = (IAccounterServerCore)
		// objects
		// .get(0);
		String userID = context.getUserEmail();
		Company company = getCompany(context.getCompanyId());
		User user1 = company.getUserByUserEmail(userID);
		if (serverObject == null) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT);
		}
		if (serverObject instanceof FiscalYear) {
			((FiscalYear) serverObject).canDelete((FiscalYear) serverObject);
			session.delete(serverObject);
			// ChangeTracker.put(serverObject);
		} else if (serverObject instanceof User) {
			User user = (User) serverObject;
			user.setDeleted(true);
			session.saveOrUpdate(user);
		} else if (serverObject instanceof RecurringTransaction) {
			session.delete(serverObject);
		} else if (serverObject instanceof Reconciliation) {
			session.delete(serverObject);
		} else {
			if (canDelete(serverClass.getSimpleName(), Long.parseLong(arg1),
					company.getAccountingType(), company.getID())) {
				session.delete(serverObject);
			} else {
				throw new AccounterException(
						AccounterException.ERROR_OBJECT_IN_USE);
			}
		}
		Activity activity = new Activity(company, user1, ActivityType.DELETE,
				serverObject);
		session.save(activity);
		try {
			hibernateTransaction.commit();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}
		return true;

	}

	private boolean canDelete(String serverClass, long id, int companyType,
			long companyId) {
		String queryName = getCanDeleteQueryName(serverClass, companyType);
		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery(queryName).setParameter("inputId", id)
				.setParameter("companyId", companyId);
		return executeQuery(query);
	}

	private String getCanDeleteQueryName(String serverClass, int companyType) {
		StringBuffer query = new StringBuffer("canDelete");
		query.append(serverClass);
		if (serverClass.equals("TAXItem") || serverClass.equals("TAXGroup")) {
			if (companyType == Company.ACCOUNTING_TYPE_US) {
				query.append("ForUS");
			}
		}
		return query.toString();
	}

	/**
	 * if command type id create, alter and delete, then changes will be add in
	 * chanageTracker,Put changes in comet stream
	 */
	public void putChangesInCometStream(long ServerCompanyID) {
		try {
			IAccounterCore[] changes = ChangeTracker.getChanges();
			if (changes != null && changes.length > 0) {
				log.info("Sending Changes From ChangeTracker:" + changes.length);
				Session session = null;
				session = HibernateUtil.getCurrentSession();
				Company company = getCompany(ServerCompanyID);
				List<User> users = session.getNamedQuery("getAllUsers")
						.setEntity("company", company).list();
				for (User user : users) {
					try {
						CometStream stream = CometManager.getStream(
								ServerCompanyID, user.getEmail());
						if (stream == null) {
							continue;
						}
						for (IAccounterCore obj : changes) {
							if (obj != null) {
								stream.put(obj);
							}
						}
						log.info("Sent " + changes.length + " change to "
								+ user.getEmail());
					} catch (NotSerializableException e) {
						e.printStackTrace();
						log.error("Failed to Process Request", e);

					}
				}
				ChangeTracker.clearChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean executeQuery(Query query) {
		List<?> queryResult = query.list();
		Boolean flag = true;
		if (queryResult != null && queryResult.size() > 0
				&& queryResult.get(0) != null) {
			if (queryResult.get(0) instanceof Long) {
				Long obj = (Long) queryResult.get(0);
				if (obj != null) {
					flag = false;

				}
			} else {
				Object[] result = (Object[]) queryResult.get(0);
				for (Object object : result) {
					if (object != null) {
						flag = false;
						break;
					}
				}
			}

		}
		return flag;
	}

	@Deprecated
	/*
	 * The code in this method is shifted to onUpdate of FiscalYear
	 */
	public void alterFiscalYear(FiscalYear fiscalYear, long companyId)
			throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<FiscalYear> list = new ArrayList<FiscalYear>();
			if (fiscalYear.getStartDate().equals(
					fiscalYear.getPreviousStartDate())) {
				session.saveOrUpdate(fiscalYear);
			} else if (!fiscalYear.getStartDate().equals(
					fiscalYear.getPreviousStartDate())) {
				FinanceDate modifiedStartDate = fiscalYear.getStartDate();
				FinanceDate existingLeastStartDate = modifiedStartDate;
				FinanceDate existingHighestEndDate = modifiedStartDate;
				Boolean exist = Boolean.FALSE;
				list = session.getNamedQuery("getFiscalYearf")
						.setEntity("company", company).list();
				if (list.size() > 0) {
					Iterator i = list.iterator();
					if (i.hasNext()) {
						FiscalYear fs = (FiscalYear) i.next();
						existingLeastStartDate = fs.getStartDate();
						existingHighestEndDate = fs.getEndDate();
					}
					i = list.iterator();
					while (i.hasNext()) {
						FiscalYear fs = (FiscalYear) i.next();
						if (modifiedStartDate.after(fs.getStartDate())
								&& modifiedStartDate.before(fs.getEndDate())) {
							exist = Boolean.TRUE;
							break;
						}
						if (fs.getStartDate().before(existingLeastStartDate)) {
							existingLeastStartDate = fs.getStartDate();
						}
						if (fs.getEndDate().after(existingHighestEndDate)) {
							existingHighestEndDate = fs.getEndDate();
						}

					}
					if (!exist) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(modifiedStartDate.getAsDateObject());
						Integer modifiedYear = cal.get(Calendar.YEAR);

						cal.setTime(existingLeastStartDate.getAsDateObject());
						Integer existingLeastYear = cal.get(Calendar.YEAR);

						cal.setTime(existingHighestEndDate.getAsDateObject());
						Integer existingHighestYear = cal.get(Calendar.YEAR);
						if (modifiedStartDate.before(existingLeastStartDate)) {
							int diff = existingLeastYear - modifiedYear;
							for (int k = 0; k < diff; k++) {

								cal.set(modifiedYear + k, 0, 1);
								FinanceDate startDate = (new FinanceDate(
										cal.getTime()));

								cal.set(modifiedYear + k, 11, 31);
								FinanceDate endDate = (new FinanceDate(
										cal.getTime()));

								FiscalYear fs = new FiscalYear();
								fs.setStartDate(startDate);
								fs.setEndDate(endDate);
								fs.setStatus(FiscalYear.STATUS_OPEN);
								fs.setIsCurrentFiscalYear(Boolean.FALSE);
								session.save(fs);
							}

						} else if (modifiedStartDate
								.after(existingHighestEndDate)) {
							int diff = modifiedYear - existingLeastYear;
							for (int k = 1; k <= diff; k++) {
								cal.set(existingLeastYear + k, 0, 1);
								FinanceDate startDate = (new FinanceDate(
										cal.getTime()));

								cal.set(existingLeastYear + k, 0, 1);
								FinanceDate endDate = (new FinanceDate(
										cal.getTime()));
								FiscalYear fs = new FiscalYear();
								fs.setStartDate(startDate);
								fs.setEndDate(endDate);
								fs.setStatus(FiscalYear.STATUS_OPEN);
								fs.setIsCurrentFiscalYear(Boolean.FALSE);
								session.save(fs);
							}
						}

					}
					fiscalYear.setStartDate(fiscalYear.getPreviousStartDate());
					session.saveOrUpdate(fiscalYear);

				}
			} else if (fiscalYear.getStatus() == FiscalYear.STATUS_CLOSE) {
				if (!fiscalYear.getIsCurrentFiscalYear()) {
					throw (new DAOException(
							DAOException.INVALID_REQUEST_EXCEPTION, null));
				} else {
					session.saveOrUpdate(fiscalYear);
				}
			}

		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Entry> getEntries(long journalEntryId, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getEntry.by.id")
					.setLong("id", journalEntryId)
					.setEntity("company", company);
			List<Entry> list = query.list();

			if (list != null) {
				return new ArrayList<Entry>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<JournalEntry> getJournalEntries(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getJournalEntry").setEntity(
					"company", company);
			List<JournalEntry> list = query.list();

			if (list != null) {
				return new ArrayList<JournalEntry>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public JournalEntry getJournalEntry(long journalEntryId, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getJournalEntry.by.id")
					.setParameter("id", journalEntryId)
					.setEntity("company", company);
			List<JournalEntry> list = query.list();

			if (list.size() > 0) {
				return list.get(0);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public Long getNextIssuePaymentCheckNumber(long account, long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session
					.getNamedQuery("getNextIssuePaymentCheckNumber")
					.setParameter("accountID", account)
					.setParameter("companyId", companyId);
			List list = query.list();

			if (list != null) {
				return (list.size() > 0) ? ((Long) list.get(0)) + 1 : 1;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public String getNextFixedAssetNumber(long companyId) throws DAOException {
		// try {
		//
		// Session session = HibernateUtil.getCurrentSession();
		// Query query = session.getNamedQuery("getNextFixedAssetNumber");
		// List list = query.list();
		//
		// if (list != null) {
		// return (list.size() > 0) ? ((Long) list.get(0)) + 1 : 1;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return NumberUtils.getNextFixedAssetNumber(getCompany(companyId));

	}

	public String getNextVoucherNumber(long companyId) throws DAOException {
		// try {
		//
		// Session session = HibernateUtil.getCurrentSession();
		// Query query = session
		// .createQuery("from com.vimukti.accounter.core.Entry e ");
		// List list1 = query.list();
		//
		// if (list1.size() <= 0) {
		//
		// return this
		// .getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY);
		// }
		//
		// query = session
		// .createQuery("select e.voucherNumber from com.vimukti.accounter.core.Entry e where e.id = (select max(e1.id) from com.vimukti.accounter.core.Entry e1 )");
		// List list = query.list();
		//
		// if (list != null) {
		// return getStringwithIncreamentedDigit(((String) list.get(0)));
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return NumberUtils.getNextVoucherNumber(getCompany(companyId));
	}

	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate1, long companyId)
			throws AccounterException {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			FinanceDate paymentDate = null;
			paymentDate = new FinanceDate(paymentDate1);
			Session session = HibernateUtil.getCurrentSession();
			Query query = session
					.getNamedQuery(
							"getReceivePaymentTransactionsListForCustomer")
					.setParameter("customerId", customerId)
					.setParameter("companyId", companyId);
			List list = query.list();

			// Query query = session.getNamedQuery(
			// "getReceivePaymentTransactionsListForCustomer");
			// query.setLong("customerId", customerId);
			// List list = query.list();

			List<ReceivePaymentTransactionList> queryResult = new ArrayList<ReceivePaymentTransactionList>();
			Company company = getCompany(companyId);
			query = session
					.getNamedQuery(
							"getEntry.by.customerId.debitand.balanceDue.orderbyid")
					.setParameter("id", customerId)
					.setParameter("company", company);

			List<JournalEntry> openingBalanceEntries = query.list();

			for (JournalEntry je : openingBalanceEntries) {
				ReceivePaymentTransactionList receivePaymentTransactionList = new ReceivePaymentTransactionList();
				receivePaymentTransactionList.setTransactionId(je.getID());
				receivePaymentTransactionList.setType(je.getType());
				receivePaymentTransactionList.setDueDate(new ClientFinanceDate(
						je.getDate().getDate()));
				receivePaymentTransactionList.setNumber(je.getNumber());
				receivePaymentTransactionList.setInvoiceAmount(je
						.getDebitTotal());
				receivePaymentTransactionList.setAmountDue(je.getBalanceDue());
				receivePaymentTransactionList
						.setDiscountDate(new ClientFinanceDate(je.getDate()
								.getDate()));
				queryResult.add(receivePaymentTransactionList);
			}

			if (list != null) {
				Object[] object = null;
				Iterator iterator = list.iterator();

				while ((iterator).hasNext()) {

					ReceivePaymentTransactionList receivePaymentTransactionList = new ReceivePaymentTransactionList();
					object = (Object[]) iterator.next();

					receivePaymentTransactionList
							.setTransactionId((Long) object[0]);
					receivePaymentTransactionList.setType((Integer) object[1]);
					receivePaymentTransactionList
							.setDueDate(object[2] == null ? null
									: new ClientFinanceDate((Long) object[2]));
					receivePaymentTransactionList
							.setNumber((object[3] == null ? null
									: ((String) object[3])));

					// int discount = (object[8] != null && !paymentDate
					// .after((Long) object[6]))
					// ? ((Integer) object[8])
					// : 0;

					double invoicedAmount = (Double) object[4];
					receivePaymentTransactionList
							.setInvoiceAmount(invoicedAmount);

					double amountDue = (Double) object[5];
					receivePaymentTransactionList.setAmountDue(amountDue);
					receivePaymentTransactionList
							.setDiscountDate(object[6] == null ? null
									: new ClientFinanceDate((Long) object[6]));
					receivePaymentTransactionList
							.setPayment((Double) object[7]);
					queryResult.add(receivePaymentTransactionList);
				}
			} else {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT);
			}
			return queryResult;
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		}

	}

	public boolean canVoidOrEdit(long invoiceOrVendorBillId, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session
				.getNamedQuery(
						"get.canVoidOrEditTransaction.from.transactionID")
				.setParameter("id", invoiceOrVendorBillId)
				.setParameter("company", company);
		List list = query.list();

		return (Boolean) list.iterator().next();
	}

	public ArrayList<WriteCheck> getLatestChecks(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestChecks")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required try to fix all sql queries
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<WriteCheck> list = new ArrayList<WriteCheck>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				WriteCheck writeCheck = new WriteCheck();
				// writeCheck
				// .setID((object[0] == null ? null : ((Long) object[0])));
				writeCheck.setDate((new FinanceDate((Long) object[1])));
				writeCheck.setPayToType((Integer) object[2]);
				writeCheck.setBalance((Double) object[3]);
				writeCheck.setCustomer(object[4] != null ? (Customer) session
						.get(Customer.class, ((Long) object[4])) : null);
				writeCheck.setVendor(object[5] != null ? (Vendor) session.get(
						Vendor.class, ((Long) object[5])) : null);
				writeCheck.setTaxAgency(object[6] != null ? (TAXAgency) session
						.get(TAXAgency.class, ((Long) object[5])) : null);
				writeCheck.setAmount((Double) object[7]);
				// writeCheck.setID((object[8] == null ? null
				// : ((String) object[8])));
				list.add(writeCheck);
			}
			if (list != null) {
				return new ArrayList<WriteCheck>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<MakeDeposit> getLatestDeposits(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestDeposits")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object[] object = null;
			Iterator iterator = list2.iterator();
			List<MakeDeposit> list = new ArrayList<MakeDeposit>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				MakeDeposit makeDeposit = new MakeDeposit();
				// makeDeposit.setID((object[0] == null ? null
				// : ((Long) object[0])));
				makeDeposit.setDepositIn(object[1] == null ? null
						: (Account) session.get(Account.class,
								((Long) object[1])));
				makeDeposit.setMemo((String) object[2]);
				makeDeposit.setTotal((object[3] == null ? null
						: ((Double) object[3])));
				makeDeposit.setCashBackAccount(object[4] == null ? null
						: (Account) session.get(Account.class,
								((Long) object[4])));
				makeDeposit.setCashBackMemo((String) object[5]);
				makeDeposit.setCashBackAmount((object[6] == null ? null
						: ((Double) object[6])));
				// makeDeposit.setID((String) object[7]);
				list.add(makeDeposit);

			}
			if (list != null) {
				return new ArrayList<MakeDeposit>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<TransferFund> getLatestFundsTransfer(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestTransferFunds")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<TransferFund> list = new ArrayList<TransferFund>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				TransferFund transferFund = new TransferFund();
				// transferFund.setID((object[0] == null ? null
				// : ((Long) object[0])));
				transferFund.setDate(new FinanceDate((Long) object[1]));
				transferFund
						.setTransferFrom(object[2] != null ? (Account) session
								.get(Account.class, ((Long) object[2])) : null);
				transferFund
						.setTransferTo(object[3] != null ? (Account) session
								.get(Account.class, ((Long) object[3])) : null);
				transferFund.setTotal((Double) object[4]);
				// transferFund.setID((object[5] == null ? null
				// : ((String) object[5])));
				list.add(transferFund);
			}
			if (list != null) {
				return new ArrayList<TransferFund>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Item> getLatestItems(long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestItems").setParameter(
					"companyId", companyId);
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
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public Long getNextCheckNumber(long accountId) throws DAOException {

		// try {
		// Session session = HibernateUtil.getCurrentSession();
		// long account = getLongIdForGivenid(AccounterCoreType.ACCOUNT,
		// account2);
		// Query query = session
		// .createQuery(
		// "from com.vimukti.accounter.core.WriteCheck wc  where wc.bankAccount.id = ?")
		// .setParameter(0, account);
		// List list1 = query.list();
		// if (list1.size() <= 0) {
		// return 1L;
		// }
		//
		// query = session
		// .createQuery(
		// "select max(wc.checkNumber) from com.vimukti.accounter.core.WriteCheck wc where wc.bankAccount.id = ? ")
		// .setParameter(0, account);
		// List list = query.list();
		// if (list != null && list.size() > 0) {
		// Object obj = list.get(0);
		// return (obj != null)
		// ? (Long) obj != -1 ? ((Long) obj) + 1 : 1
		// : 1;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DAOException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return null;
	}

	public Long getNextNominalCode(int accountType, long companyId)
			throws DAOException {

		try {
			int accountSubBaseType = Utility.getAccountSubBaseType(accountType);
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Integer range[] = company.getNominalCodeRange(accountSubBaseType);

			Query query = session
					.getNamedQuery("getNextNominalCodeForGivenAccountType")
					.setParameter("subBaseType", accountSubBaseType)
					.setParameter("companyId", companyId);
			List list = query.list();
			Long nextNominalCode = (list.size() > 0) ? ((Long) list.get(0)) + 1
					: range[0];
			if (nextNominalCode > range[1]) {
				throw (new DAOException(DAOException.RANGE_EXCEED_EXCEPTION,
						null,
						"Nominal Code Range exceeded. Please delete the existing accounts of this type"));
			}

			if (list != null) {
				return nextNominalCode;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public TransactionMakeDeposit getTransactionMakeDeposit(
			long transactionMakeDepositId, long companyId) throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery("getTransactionMakeDeposit.by.id")
					.setParameter("id", transactionMakeDepositId)
					.setEntity("company", company);
			List list = query.list();

			if (list != null) {
				return (TransactionMakeDeposit) list.get(0);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<ClientTransactionMakeDeposit> getTransactionMakeDeposits(
			long companyId) throws DAOException {
		List<ClientTransactionMakeDeposit> transactionMakeDepositsList = new ArrayList<ClientTransactionMakeDeposit>();

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery(
				"getTransactionMakeDeposit.by.checking.isDepositedandisVoid")
				.setEntity("company", company);

		List<TransactionMakeDepositEntries> listing = query.list();

		return new ArrayList<ClientTransactionMakeDeposit>(query.list());

		// Query query = session
		// .createQuery(
		// "from com.vimukti.accounter.core.AccountTransaction at where at.account.name = 'Un Deposited Funds' and "
		// +
		// "at.transaction.isDeposited = 'false' and at.transaction.isVoid = 'false' "
		// );
		//
		// List<AccountTransaction> listing = query.list();

		// if (listing != null && listing.size() > 0) {
		//
		// for (TransactionMakeDepositEntries transactionMakeDepositEntry :
		// listing) {
		//
		// MakeDepositTransactionsList makeDepositTransactionsList = new
		// MakeDepositTransactionsList(
		// ((ClientTransaction) new ClientConvertUtil()
		// .toClientObject(
		// transactionMakeDepositEntry.getTransaction(),
		// Util
		// .getClientEqualentClass(transactionMakeDepositEntry
		// .getTransaction()
		// .getClass()))),
		// transactionMakeDepositEntry.getAmount());
		//
		// Payee payee = transactionMakeDepositEntry.getTransaction()
		// .getInvolvedPayee();
		//
		// makeDepositTransactionsList.setCashAccountId(
		// transactionMakeDepositEntry
		// .getAccount().getID());
		// makeDepositTransactionsList.setPaymentMethod(
		// transactionMakeDepositEntry
		// .getTransaction().getPaymentMethod());
		// makeDepositTransactionsList.setReference(transactionMakeDepositEntry
		// .getTransaction().getReference());
		// makeDepositTransactionsList.setPayeeName(transactionMakeDepositEntry
		// .getAccount().getName());
		// if (payee != null) {
		// makeDepositTransactionsList.setPayeeName(payee.getName());
		// makeDepositTransactionsList.setType(payee.getType());
		// if (payee.getType() == Payee.TYPE_CUSTOMER) {
		// makeDepositTransactionsList
		// .setType(Payee.TYPE_CUSTOMER);
		// } else if (payee.getType() == Payee.TYPE_VENDOR) {
		// makeDepositTransactionsList.setType(Payee.TYPE_VENDOR);
		// } else if (payee.getType() == Payee.TYPE_TAX_AGENCY) {
		// makeDepositTransactionsList
		// .setType(Payee.TYPE_TAX_AGENCY);
		// }
		//
		// transactionMakeDepositsList
		// .add(makeDepositTransactionsList);
		// }
		// }
		// }
		// return transactionMakeDepositsList;

	}

	public void test() throws Exception {
		// HibernateTemplate template = getHibernateTemplate();
		//
		// // List list = template.findByNamedQueryAndNamedParam(
		// // "test", "companyId", company);
		// List list = template.findByNamedQuery("test");
		// if (list != null) {
		// Iterator i = list.iterator();
		// Object o[] = null;
		// while (i.hasNext()) {
		// o = (Object[]) i.next();
		// String accName = (String) o[0];
		// String taName = (String) o[1];
		//
		// }
		// }
	}

	/**
	 * 
	 * This method will give us the total effect of Selling or Disposing a Fixed
	 * Asset, before Sell or Dispose this Fixed Asset.
	 * 
	 * @param companyId
	 * 
	 * @return
	 * @throws Exception
	 */

	public ArrayList<ClientFinanceDate> getFinancialYearStartDates(
			long companyId) throws DAOException {

		List<ClientFinanceDate> startDates = new ArrayList<ClientFinanceDate>();
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		FinanceDate depreciationStartDate = company.getPreferences()
				.getDepreciationStartDate();
		Calendar depStartDateCal = new GregorianCalendar();
		depStartDateCal.setTime(depreciationStartDate.getAsDateObject());
		Query query = session.getNamedQuery(
				"getFiscalYear.by.check.status.startDate").setEntity("company",
				company);
		List<FiscalYear> list = query.list();
		for (FiscalYear fs : list) {
			FinanceDate date = fs.getStartDate();
			Calendar cal = new GregorianCalendar();
			cal.setTime(date.getAsDateObject());
			cal.set(Calendar.DAY_OF_MONTH,
					depStartDateCal.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, depStartDateCal.get(Calendar.MONTH));
			startDates.add(new ClientFinanceDate(cal.getTime()));
		}
		return new ArrayList<ClientFinanceDate>(startDates);
	}

	public ArrayList<AccountRegister> getAccountRegister(
			final FinanceDate startDate, final FinanceDate endDate,
			final long accountId, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getAccountRegister")
				.setParameter("companyId", companyId)
				.setParameter("accountId", accountId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<AccountRegister> queryResult = new ArrayList<AccountRegister>();
		while ((iterator).hasNext()) {

			AccountRegister accountRegister = new AccountRegister();
			object = (Object[]) iterator.next();

			accountRegister.setDate(new ClientFinanceDate((Long) object[0]));
			accountRegister
					.setType(object[1] == null ? 0 : (Integer) object[1]);
			accountRegister.setNumber((String) object[2]);
			accountRegister.setAmount(object[3] == null ? 0
					: ((Double) object[3]).doubleValue());
			accountRegister.setPayTo((String) object[4]);
			accountRegister.setCheckNumber(object[5] == null ? null
					: ((String) object[5]));
			accountRegister.setAccount((String) object[6]);
			/*
			 * Clob cl = (Clob) object[7]; if (cl == null) {
			 * 
			 * accountRegister.setMemo("");
			 * 
			 * } else {
			 * 
			 * StringBuffer strOut = new StringBuffer(); String aux; try {
			 * BufferedReader br = new BufferedReader(cl .getCharacterStream());
			 * while ((aux = br.readLine()) != null) strOut.append(aux);
			 * accountRegister.setMemo(strOut.toString()); } catch
			 * (java.sql.SQLException e1) {
			 * 
			 * } catch (java.io.IOException e2) {
			 * 
			 * } }
			 */
			accountRegister.setMemo((String) object[7]);
			accountRegister.setBalance(object[8] == null ? 0
					: ((Double) object[8]).doubleValue());
			accountRegister.setTransactionId((Long) object[9]);
			accountRegister.setVoided(object[10] == null ? false
					: (Boolean) object[10]);

			queryResult.add(accountRegister);
		}
		return new ArrayList<AccountRegister>(queryResult);
	}

	public String getNextTransactionNumber(int transactionType, long companyId) {

		// Query query = HibernateUtil
		// .getCurrentSession()
		// .createQuery(
		// "select max(t.id) from com.vimukti.accounter.core.Transaction t where t.type =:transactionType")
		// .setParameter("transactionType", transactionType);
		//
		// List list = query.list();
		//
		// if (list == null || list.size() <= 0 || list.get(0) == null) {
		// return "1";
		// }
		//
		// String prevNumber = getPreviousTransactionNumber(transactionType,
		// (Long) list.get(0));
		//
		// return getStringwithIncreamentedDigit(prevNumber);
		return NumberUtils.getNextTransactionNumber(transactionType,
				getCompany(companyId));
	}

	public ArrayList<HrEmployee> getHREmployees() {
		Session session = HibernateUtil.getCurrentSession();
		SQLQuery query = session.createSQLQuery(
				"SELECT empd.FULL_NAME as name FROM USERS empd").addScalar(
				"name", Hibernate.STRING);
		List list = query.list();

		// Object[] object = null;
		Iterator iterator = list.iterator();
		List<HrEmployee> hrEmployees = new ArrayList<HrEmployee>();
		while ((iterator).hasNext()) {

			HrEmployee hrEmployee = new HrEmployee();
			// object = (Object[]) iterator.next();
			hrEmployee.setEmployeeName((String) iterator.next());
			// hrEmployee.setEmployeeNum((String) object[1]);

			hrEmployees.add(hrEmployee);
		}
		return new ArrayList<HrEmployee>(hrEmployees);
	}

	public String getPreviousTransactionNumber(int transactionType,
			long maxCount) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getTransactionNumber.from.typeandId")
				.setParameter(0, transactionType).setParameter(0, maxCount);

		List list = query.list();

		String num = (list != null) && (list.size() > 0) ? ((String) list
				.get(0)) : "";
		if (num.replaceAll("[\\D]", "").length() > 0) {
			return num;
		} else {
			if (maxCount != 0) {
				maxCount = maxCount - 1;
				return getPreviousTransactionNumber(transactionType, maxCount);
			}
		}

		return "0";
	}

	private boolean isTransactionNumberExist(IAccounterCore object,
			Company company) throws AccounterException {
		FlushMode flushMode = HibernateUtil.getCurrentSession().getFlushMode();
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.COMMIT);

		try {

			if (object instanceof ClientTransaction
					&& !(object instanceof ClientTransferFund)
					&& !(object instanceof ClientMakeDeposit)) {

				ClientTransaction clientObject = (ClientTransaction) object;

				if (clientObject.isVoid())
					return true;

				if (clientObject instanceof ClientPayBill
						&& ((ClientPayBill) clientObject).getPayBillType() == 1) {
					return true;
				}

				if (clientObject.getNumber() == null
						|| clientObject.getNumber().equals(""))
					return true;
				Query query = HibernateUtil
						.getCurrentSession()
						.getNamedQuery("getTransaction.by.check.type.number.id")
						.setParameter("company", company)
						.setParameter("type", clientObject.getType())
						.setParameter("number", clientObject.getNumber())
						.setParameter("id", clientObject.getID());

				List list = query.list();

				if (list != null
						&& list.size() > 0
						&& list.get(0) != null
						&& !(company.getPreferences()
								.getAllowDuplicateDocumentNumbers())) {
					throw new AccounterException(
							AccounterException.ERROR_NUMBER_CONFLICT,
							" A Transaction already exists with this number. Please give another one. ");
				}

			}
			return true;

		} finally {
			HibernateUtil.getCurrentSession().setFlushMode(flushMode);
		}
	}

	/**
	 * Returns the Current Company
	 * 
	 * @return
	 */
	public Company getCompany(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		return (Company) session.get(Company.class, companyId);
	}

	public static void createView(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("createSalesPurchasesView")
				.setParameter("companyId", companyId).executeUpdate();
		session.getNamedQuery("createTransactionHistoryView")
				.setParameter("companyId", companyId).executeUpdate();
	}

	public static void createViewsForclient(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("createSalesPurchasesViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
		session.getNamedQuery("createTransactionHistoryViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
	}

	public ArrayList<PayeeList> getPayeeList(int transactionCategory,
			long companyId) throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();

			FinanceDate currentDate = new FinanceDate();

			Calendar currentMonthStartDateCal = new GregorianCalendar();
			currentMonthStartDateCal.setTime(currentDate.getAsDateObject());
			currentMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar currentMonthEndDateCal = new GregorianCalendar();
			currentMonthEndDateCal.setTime(currentDate.getAsDateObject());
			currentMonthEndDateCal.set(Calendar.DATE, currentMonthEndDateCal
					.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFirstMonthStartDateCal = new GregorianCalendar();
			previousFirstMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFirstMonthStartDateCal.set(Calendar.MONTH,
					previousFirstMonthStartDateCal.get(Calendar.MONTH) - 1);
			previousFirstMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFirstMonthEndDateCal = new GregorianCalendar();
			previousFirstMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousFirstMonthEndDateCal.set(Calendar.MONTH,
					previousFirstMonthEndDateCal.get(Calendar.MONTH) - 1);
			previousFirstMonthEndDateCal.set(Calendar.DATE,
					previousFirstMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousSecondMonthStartDateCal = new GregorianCalendar();
			previousSecondMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousSecondMonthStartDateCal.set(Calendar.MONTH,
					previousSecondMonthStartDateCal.get(Calendar.MONTH) - 2);
			previousSecondMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousSecondMonthEndDateCal = new GregorianCalendar();
			previousSecondMonthEndDateCal
					.setTime(currentDate.getAsDateObject());
			previousSecondMonthEndDateCal.set(Calendar.MONTH,
					previousSecondMonthEndDateCal.get(Calendar.MONTH) - 2);
			previousSecondMonthEndDateCal.set(Calendar.DATE,
					previousSecondMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousThirdMonthStartDateCal = new GregorianCalendar();
			previousThirdMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousThirdMonthStartDateCal.set(Calendar.MONTH,
					previousThirdMonthStartDateCal.get(Calendar.MONTH) - 3);
			previousThirdMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousThirdMonthEndDateCal = new GregorianCalendar();
			previousThirdMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousThirdMonthEndDateCal.set(Calendar.MONTH,
					previousThirdMonthEndDateCal.get(Calendar.MONTH) - 3);
			previousThirdMonthEndDateCal.set(Calendar.DATE,
					previousThirdMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFourthMonthStartDateCal = new GregorianCalendar();
			previousFourthMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFourthMonthStartDateCal.set(Calendar.MONTH,
					previousFourthMonthStartDateCal.get(Calendar.MONTH) - 4);
			previousFourthMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFourthMonthEndDateCal = new GregorianCalendar();
			previousFourthMonthEndDateCal
					.setTime(currentDate.getAsDateObject());
			previousFourthMonthEndDateCal.set(Calendar.MONTH,
					previousFourthMonthEndDateCal.get(Calendar.MONTH) - 4);
			previousFourthMonthEndDateCal.set(Calendar.DATE,
					previousFourthMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Calendar previousFifthMonthStartDateCal = new GregorianCalendar();
			previousFifthMonthStartDateCal.setTime(currentDate
					.getAsDateObject());
			previousFifthMonthStartDateCal.set(Calendar.MONTH,
					previousFifthMonthStartDateCal.get(Calendar.MONTH) - 5);
			previousFifthMonthStartDateCal.set(Calendar.DATE, 1);

			Calendar previousFifthMonthEndDateCal = new GregorianCalendar();
			previousFifthMonthEndDateCal.setTime(currentDate.getAsDateObject());
			previousFifthMonthEndDateCal.set(Calendar.MONTH,
					previousFifthMonthEndDateCal.get(Calendar.MONTH) - 5);
			previousFifthMonthEndDateCal.set(Calendar.DATE,
					previousFifthMonthEndDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			Query query = null;

			if (transactionCategory == Transaction.CATEGORY_CUSTOMER) {

				query = session
						.getNamedQuery("getCustomersList")
						.setParameter("companyId", companyId)
						.setParameter(
								"currentMonthStartDateCal",
								new FinanceDate(currentMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"currentMonthEndDateCal",
								new FinanceDate(currentMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthStartDateCal",
								new FinanceDate(previousFirstMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthEndDateCal",
								new FinanceDate(previousFirstMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthStartDateCal",
								new FinanceDate(previousSecondMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthEndDateCal",
								new FinanceDate(previousSecondMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthStartDateCal",
								new FinanceDate(previousThirdMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthEndDateCal",
								new FinanceDate(previousThirdMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthStartDateCal",
								new FinanceDate(previousFourthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthEndDateCal",
								new FinanceDate(previousFourthMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthStartDateCal",
								new FinanceDate(previousFifthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthEndDateCal",
								new FinanceDate(previousFifthMonthEndDateCal
										.getTime()).getDate());

			} else if (transactionCategory == Transaction.CATEGORY_VENDOR) {

				query = session
						.getNamedQuery("getVendorsList")
						.setParameter("companyId", companyId)
						.setParameter(
								"currentMonthStartDateCal",
								new FinanceDate(currentMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"currentMonthEndDateCal",
								new FinanceDate(currentMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthStartDateCal",
								new FinanceDate(previousFirstMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFirstMonthEndDateCal",
								new FinanceDate(previousFirstMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthStartDateCal",
								new FinanceDate(previousSecondMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousSecondMonthEndDateCal",
								new FinanceDate(previousSecondMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthStartDateCal",
								new FinanceDate(previousThirdMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousThirdMonthEndDateCal",
								new FinanceDate(previousThirdMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthStartDateCal",
								new FinanceDate(previousFourthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFourthMonthEndDateCal",
								new FinanceDate(previousFourthMonthEndDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthStartDateCal",
								new FinanceDate(previousFifthMonthStartDateCal
										.getTime()).getDate())
						.setParameter(
								"previousFifthMonthEndDateCal",
								new FinanceDate(previousFifthMonthEndDateCal
										.getTime()).getDate());
			}

			List list = query.list();
			if (list != null) {
				Object[] object = null;
				String payeeName = null;
				PayeeList payeeList = null;
				Iterator iterator = list.iterator();
				List<PayeeList> queryResult = new ArrayList<PayeeList>();
				while ((iterator).hasNext()) {

					object = (Object[]) iterator.next();

					if (payeeName != null && ((String) object[1]) != null
							&& payeeName.equals((String) object[1])) {

						payeeList.setCurrentMonth(payeeList.getCurrentMonth()
								+ (Double) object[4]);

						payeeList.setPreviousMonth(payeeList.getPreviousMonth()
								+ (Double) object[5]);

						payeeList.setPreviousSecondMonth(payeeList
								.getPreviousSecondMonth() + (Double) object[6]);

						payeeList.setPreviousThirdMonth(payeeList
								.getPreviousThirdMonth() + (Double) object[7]);

						payeeList.setPreviousFourthMonth(payeeList
								.getPreviousFourthMonth() + (Double) object[8]);

						payeeList.setPreviousFifthMonth(payeeList
								.getPreviousFifthMonth() + (Double) object[9]);

						payeeList.setYearToDate(payeeList.getYearToDate()
								+ (Double) object[10]);

					} else {

						payeeList = new PayeeList();

						payeeList.setActive((object[0] == null ? false
								: ((Boolean) object[0]).booleanValue()));
						payeeName = (String) object[1];
						payeeList.setPayeeName(payeeName);
						payeeList.setType((Integer) object[2]);
						payeeList.setID((Long) object[3]);
						payeeList.setCurrentMonth((Double) object[4]);
						payeeList.setPreviousMonth((Double) object[5]);
						payeeList.setPreviousSecondMonth((Double) object[6]);
						payeeList.setPreviousThirdMonth((Double) object[7]);
						payeeList.setPreviousFourthMonth((Double) object[8]);
						payeeList.setPreviousFifthMonth((Double) object[9]);
						payeeList.setYearToDate((Double) object[10]);
						payeeList.setBalance((Double) object[11]);

						queryResult.add(payeeList);
					}
				}
				return new ArrayList<PayeeList>(queryResult);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<DepositDetail> getDepositDetail(FinanceDate startDate,
			FinanceDate endDate, long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("getDepositDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate()).list();
		Map<Long, List<DepositDetail>> map = new LinkedHashMap<Long, List<DepositDetail>>();
		List<DepositDetail> depositDetails = new ArrayList<DepositDetail>();
		Iterator it = list.iterator();
		Long tempTransactionID = null;
		double tempAmount = 0;
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			DepositDetail d = new DepositDetail();
			d.setTransactionId((Long) object[0]);
			d.setTransactionType((Integer) object[1]);
			d.setTransactionNumber((String) object[2]);
			d.setTransactionDate(new ClientFinanceDate((Long) object[3]));
			d.setName((String) object[4]);
			d.setAccountName((String) object[5]);
			if (tempTransactionID == null
					|| (d.getTransactionId() != tempTransactionID)) {
				d.setTotal((Double) object[6]);
				tempAmount = d.getTotal();
			} else {
				// double amount = DecimalUtil.isGreaterThan(tempAmount, 0) ? 1
				// * ((Double) object[6]) : ((Double) object[6]);
				d.setTotal((Double) object[6]);
			}
			if (map.containsKey(d.getTransactionId())) {
				map.get(d.getTransactionId()).add(d);
			} else {
				List<DepositDetail> tempList = new ArrayList<DepositDetail>();
				tempList.add(d);
				map.put(d.getTransactionId(), tempList);
			}
			// depositDetails.add(d);
			tempTransactionID = d.getTransactionId();
		}

		Long[] ids = new Long[map.keySet().size()];
		map.keySet().toArray(ids);
		for (Long s : ids)
			depositDetails.addAll(map.get(s));

		return new ArrayList<DepositDetail>(depositDetails);
	}

	public ArrayList<ClientRecurringTransaction> getAllRecurringTransactions(
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<RecurringTransaction> transactions = session
				.getNamedQuery("list.RecurringTransaction")
				.setEntity("company", getCompany(companyId)).list();

		List<ClientRecurringTransaction> clientObjs = new ArrayList<ClientRecurringTransaction>();
		for (RecurringTransaction recurringTransaction : transactions) {
			ClientRecurringTransaction clientObject = new ClientConvertUtil()
					.toClientObject(recurringTransaction,
							ClientRecurringTransaction.class);
			clientObject.setRefTransactionTotal(recurringTransaction
					.getReferringTransaction().getTotal());
			clientObject.setRefTransactionType(recurringTransaction
					.getReferringTransaction().getType());

			// TODO doubt
			clientObject.setReferringTransaction(recurringTransaction
					.getReferringTransaction().getID());

			clientObjs.add(clientObject);
		}

		return new ArrayList<ClientRecurringTransaction>(clientObjs);
	}

	public void mergeAcoount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount, long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);
		double mergeBalance = toClientAccount.getOpeningBalance()
				+ fromClientAccount.getOpeningBalance();

		session.getNamedQuery("update.merge.Account.oldBalance.tonew")
				.setLong("from", toClientAccount.getID())
				.setDouble("balance", mergeBalance)
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("delete.account.old")
				.setLong("from", fromClientAccount.getID())
				.setEntity("company", company).executeUpdate();

		session.getNamedQuery("delete.account.entry.old")
				.setLong("from", fromClientAccount.getID())
				.setEntity("company", company).executeUpdate();

		ServerConvertUtil convertUtil = new ServerConvertUtil();
		Account account = new Account();
		account = convertUtil.toServerObject(account, fromClientAccount,
				session);
		session.delete(account);

		tx.commit();

	}

	public void mergeItem(ClientItem fromClientItem, ClientItem toClientItem,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);
		session.getNamedQuery("update.mergeItem.oldcost.tonewcost")
				.setLong("from", toClientItem.getID())
				.setBoolean("status", fromClientItem.isActive())
				.setDouble("price", fromClientItem.getSalesPrice())
				.setEntity("company", company).executeUpdate();

		ServerConvertUtil convertUtil = new ServerConvertUtil();
		Item item = new Item();
		item = convertUtil.toServerObject(item, fromClientItem, session);
		session.delete(item);

		tx.commit();

	}

	public void recordActivity(Company company, User user, ActivityType type) {
		Activity activity = new Activity(company, user, type);

	}

	public static Transaction createDuplicateTransaction(
			RecurringTransaction recurringTransaction)
			throws AccounterException, CloneNotSupportedException {
		Transaction transaction = recurringTransaction
				.getReferringTransaction();

		// Instead of cloning directly on transaction, we'll perform cloning on
		// client transacton.
		ClientTransaction clientTransaction = (ClientTransaction) new ClientConvertUtil()
				.toClientObject(transaction,
						Util.getClientEqualentClass(transaction.getClass()));

		ClientTransaction clone = new CloneUtil<IAccounterCore>(
				IAccounterCore.class).clone(null, clientTransaction, true);

		// reset IDs
		clone.setID(0);
		clone.setRecurringTransaction(0);
		clone.setNumber(NumberUtils.getNextTransactionNumber(clone.getType(),
				transaction.getCompany()));

		List<ClientTransactionItem> transactionItems2 = clone
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems2) {
			clientTransactionItem.setID(0);
		}

		// reset credits and payments
		clone.setCreditsAndPayments(null);
		clone.setTransactionMakeDeposit(new ArrayList<ClientTransactionMakeDeposit>());
		clone.setTransactionPayBill(new ArrayList<ClientTransactionPayBill>());
		clone.setTransactionReceivePayment(new ArrayList<ClientTransactionReceivePayment>());
		clone.setTransactionIssuePayment(new ArrayList<ClientTransactionIssuePayment>());
		clone.setTransactionPaySalesTax(new ArrayList<ClientTransactionPaySalesTax>());

		Session session = HibernateUtil.getCurrentSession();

		Transaction transaction2 = null;/*
										 * (Transaction) session.get(
										 * transaction.getClass(),
										 * transaction.getID())
										 */

		transaction2 = new ServerConvertUtil().toServerObject(null, clone,
				session);

		// set transaction date and duedate
		transaction2.setDate(recurringTransaction
				.getNextScheduledTransactionDate());
		if (recurringTransaction.canHaveDueDate()) {
			if (transaction2 instanceof Invoice) {
				((Invoice) transaction2).setDueDate(recurringTransaction
						.getNextTransactionDueDate());
			} else {
				((PayBill) transaction2)
						.setBillDueOnOrBefore(recurringTransaction
								.getNextTransactionDueDate());
			}
		}
		return transaction2;
	}

	/**
	 * this method is used to send Pdf as an attachment in email
	 * 
	 * @param objectID
	 * @param type
	 * @param brandingThemeId
	 * @param mimeType
	 * @param subject
	 * @param content
	 * @param senderEmail
	 * @param toEmail
	 * @param ccEmail
	 * @throws TemplateSyntaxException
	 * @throws IOException
	 */
	public void sendPdfInMail(long objectID, int type, long brandingThemeId,
			String mimeType, String subject, String content,
			String senderEmail, String toEmail, String ccEmail, long companyId)
			throws Exception, IOException {
		Manager manager = getManager();
		BrandingTheme brandingTheme = (BrandingTheme) manager
				.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
						brandingThemeId);

		String fileName = "";
		String output = "";
		Company company = getCompany(companyId);
		String companyName = company.getFullName();

		// for printing individual pdf documents
		if (type == Transaction.TYPE_INVOICE) {
			Invoice invoice = (Invoice) manager.getServerObjectForid(
					AccounterCoreType.INVOICE, objectID);

			// template = new InvoiceTemplete(invoice,
			// brandingTheme, footerImg, style);

			InvoicePDFTemplete invoiceHtmlTemplete = new InvoicePDFTemplete(
					invoice, brandingTheme, company, company.getCompanyID());

			fileName = invoiceHtmlTemplete.getFileName();

			output = invoiceHtmlTemplete.getPdfData();

		} else if (type == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			// for Credit Note
			CustomerCreditMemo memo = (CustomerCreditMemo) manager
					.getServerObjectForid(AccounterCoreType.CUSTOMERCREDITMEMO,
							objectID);

			CreditNotePDFTemplete creditNotePDFTemplete = new CreditNotePDFTemplete(
					memo, brandingTheme, company, company.getCompanyID());

			fileName = creditNotePDFTemplete.getFileName();

			output = creditNotePDFTemplete.getPdfData();

		}

		InputStream inputStream = new ByteArrayInputStream(output.getBytes());
		InputStreamReader reader = new InputStreamReader(inputStream);

		Converter converter = new Converter();
		File file = converter.getPdfFile(fileName, reader);
		// converter.getPdfFile(fileName, reader);
		// InputStream inputStream = new
		// ByteArrayInputStream(output.getBytes());

		// UsersMailSendar.sendPdfMail(fileName, inputStream, mimeType,
		// companyName, subject, content, senderEmail, toEmail, ccEmail);

		UsersMailSendar.sendPdfMail(file, companyName, subject, content,
				senderEmail, toEmail, ccEmail);
	}

	/**
	 * For profit and loss by location query.
	 * 
	 * @param isLocation
	 * 
	 * @param startDate
	 * @param endDate
	 * @return {@link ArrayList<ProfitAndLossByLocation>}
	 */

	public List<ClientBudget> getBudgetList(long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		try {

			ArrayList<Budget> budgetList = new ArrayList<Budget>(session
					.getNamedQuery("list.Budget")
					.setEntity("company", getCompany(companyId)).list());

			List<ClientBudget> clientBudgetObjs = new ArrayList<ClientBudget>();

			for (Budget budget : budgetList) {
				ClientBudget clientObject = new ClientConvertUtil()
						.toClientObject(budget, ClientBudget.class);

				clientBudgetObjs.add(clientObject);
			}

			return new ArrayList<ClientBudget>(clientBudgetObjs);

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public List<ClientTransaction> getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session.getNamedQuery("get.transactions.by.account")
				.setLong("id", id)
				.setParameter("startDate", new FinanceDate(startDate))
				.setParameter("endDate", new FinanceDate(endDate))
				.setParameter("company", company).list();
		List<ClientTransaction> transactions = new ArrayList<ClientTransaction>();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			AccountTransaction next = (AccountTransaction) iterator.next();
			Transaction transaction = next.getTransaction();
			if (!transaction.isVoid()) {
				transactions
						.add((ClientTransaction) new ClientConvertUtil()
								.toClientObject(transaction, Util
										.getClientEqualentClass(transaction
												.getClass())));
			}
		}
		return transactions;
	}

	/**
	 * @param accountID
	 */
	public List<ClientReconciliation> getReconciliationsByBankAccountID(
			long accountID, long companyID) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyID);
		List list = session.getNamedQuery("get.reconciliations.by.accountId")
				.setLong("accountID", accountID).setEntity("country", company)
				.list();

		List<ClientReconciliation> reconciliationsList = new ArrayList<ClientReconciliation>();
		Iterator iterator = list.iterator();
		ClientConvertUtil convertUtil = new ClientConvertUtil();
		while (iterator.hasNext()) {
			Reconciliation next = (Reconciliation) iterator.next();
			reconciliationsList.add(convertUtil.toClientObject(next,
					ClientReconciliation.class));
		}
		return reconciliationsList;

	}

	/**
	 * @param accountID
	 * @return
	 */
	public double getOpeningBalanceforReconciliation(long accountID,
			long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session
				.getNamedQuery(
						"get.OpeningBalance.Of.Account.from.Reconciliations")
				.setLong("accountID", accountID).setEntity("company", company)
				.list();
		if (list.isEmpty()) {
			return 0.0;
		}
		return ((Reconciliation) list.get(0)).getClosingBalance();
	}

	/**
	 * @param transactionId
	 * @param transactionId2
	 * @param noteDescription
	 * @throws AccounterException
	 */
	public long createNote(long companyId, long transactionId,
			String noteDescription) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			Company company = getCompany(companyId);
			Activity noteActivity = new Activity(company,
					AccounterThreadLocal.get(), ActivityType.NOTE);
			noteActivity.setObjectID(transactionId);
			noteActivity.setDescription(noteDescription);
			session.save(noteActivity);
			transaction.commit();
			return noteActivity.getID();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		}

	}

	/**
	 * Return Transactions All Activity
	 * 
	 * @param transactionId
	 * @return
	 * @throws AccounterException
	 */
	public List<ClientActivity> getTransactionHistory(long transactionId,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session.getNamedQuery("get.all.activities.of.transaction")
				.setEntity("company", company).list();
		List<ClientActivity> history = new ArrayList<ClientActivity>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Activity activity = (Activity) iterator.next();
			ClientActivity clientActivity = new ClientConvertUtil()
					.toClientObject(activity, ClientActivity.class);
			history.add(clientActivity);
		}
		return history;
	}

	public SalesManager getSalesManager() {
		if (salesManager == null) {
			salesManager = new SalesManager();
		}
		return salesManager;
	}

	public CustomerManager getCustomerManager() {
		if (customerManager == null) {
			customerManager = new CustomerManager();
		}
		return customerManager;
	}

	public VendorManager getVendorManager() {
		if (vendorManager == null) {
			vendorManager = new VendorManager();
		}
		return vendorManager;
	}

	public ReportManager getReportManager() {
		if (reportManager == null) {
			reportManager = new ReportManager();
		}
		return reportManager;
	}

	public PurchaseManager getPurchageManager() {
		if (purchaseManager == null) {
			purchaseManager = new PurchaseManager();
		}
		return purchaseManager;
	}

	public TaxManager getTaxManager() {
		if (taxManager == null) {
			taxManager = new TaxManager();
		}
		return taxManager;
	}

	public DashboardManager getDashboardManager() {
		if (dashboardManager == null) {
			dashboardManager = new DashboardManager();
		}
		return dashboardManager;
	}

	public FixedAssestManager getFixedAssetManager() {
		if (fixedAssestManager == null) {
			fixedAssestManager = new FixedAssestManager();
		}
		return fixedAssestManager;
	}

	public InventoryManager getInventoryManager() {
		if (inventoryManager == null) {
			inventoryManager = new InventoryManager();
		}
		return inventoryManager;
	}

	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager();
		}
		return userManager;
	}

	public CompanyManager getCompanyManager() {
		if (companyManager == null) {
			companyManager = new CompanyManager();
		}
		return companyManager;
	}

	public Manager getManager() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}
}
