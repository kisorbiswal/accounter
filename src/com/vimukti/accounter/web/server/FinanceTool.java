/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Advertisement;
import com.vimukti.accounter.core.Attachment;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Budget;
import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CloneUtil2;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.CustomField;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.IRASCompanyInfo;
import com.vimukti.accounter.core.IRASGeneralLedgerLineInfo;
import com.vimukti.accounter.core.IRASInformation;
import com.vimukti.accounter.core.IRASPurchaseLineInfo;
import com.vimukti.accounter.core.IRASSupplyLineInfo;
import com.vimukti.accounter.core.InventoryPurchase;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.InvoicePdfGeneration;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.MessageOrTask;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.ObjectConvertUtil;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.PortletConfiguration;
import com.vimukti.accounter.core.PortletPageConfiguration;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.core.ReconciliationItem;
import com.vimukti.accounter.core.RecurringTransaction;
import com.vimukti.accounter.core.Reminder;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.Statement;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXRateCalculation;
import com.vimukti.accounter.core.TAXReturnEntry;
import com.vimukti.accounter.core.TDSChalanDetail;
import com.vimukti.accounter.core.TDSDeductorMasters;
import com.vimukti.accounter.core.TDSResponsiblePerson;
import com.vimukti.accounter.core.TDSTransactionItem;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionDepositItem;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionLog;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorPayment;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.server.imports.CustomerImporter;
import com.vimukti.accounter.server.imports.Importer;
import com.vimukti.accounter.server.imports.InvoiceImporter;
import com.vimukti.accounter.server.imports.ItemImporter;
import com.vimukti.accounter.server.imports.VendorImporter;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.MiniTemplator.TemplateSyntaxException;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.ClientLocalMessage;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAdvertisement;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientETDSFillingItem;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientReconciliationItem;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientReminder;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransactionLog;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.RecentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.imports.ImporterType;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.UIUtils;
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
import com.vimukti.accounter.web.server.translate.Key;
import com.vimukti.accounter.web.server.translate.Language;
import com.vimukti.accounter.web.server.translate.LocalMessage;
import com.vimukti.accounter.web.server.translate.Message;
import com.vimukti.accounter.web.server.translate.Vote;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * @author Fernandez
 * 
 */
public class FinanceTool {

	private static final Object Double = null;
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
					data, session);

			ObjectConvertUtil.setCompany(serverObject, company);

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

			isTransactionNumberExist(data, company);
			session.save(serverObject);
			transaction.commit();

			org.hibernate.Transaction newTransaction = session
					.beginTransaction();
			Activity activity = new Activity(company, user, ActivityType.ADD,
					serverObject);
			session.save(activity);
			if (serverObject instanceof Transaction) {
				Transaction savingTrax = (Transaction) serverObject;
				savingTrax.setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);

			newTransaction.commit();

			if (serverObject instanceof Transaction) {

				org.hibernate.Transaction ht = session.beginTransaction();
				try {
					Transaction tt = (Transaction) serverObject;
					List<Item> inventory = tt.getInventoryUsed();
					InventoryUtils.remapSalesPurchases(inventory);
					ht.commit();
				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

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
			IAccounterServerCore clonedObject = new CloneUtil2(
					IAccounterServerCore.class).clone(null, serverObject,
					data.getClass());
			ObjectConvertUtil.setCompany(clonedObject, company);

			getManager().canEdit(clonedObject, data);

			isTransactionNumberExist(data, company);

			new ServerConvertUtil().toServerObject(serverObject, data, session);

			serverObject.setVersion(++version);

			if (serverObject instanceof Transaction) {
				Transaction transaction = (Transaction) serverObject;
				Transaction clonedTransaction = (Transaction) clonedObject;
				if (!(transaction.getSaveStatus() == Transaction.STATUS_VOID
						&& clonedTransaction.getSaveStatus() == Transaction.STATUS_VOID || clonedTransaction
							.getSaveStatus() == Transaction.STATUS_DRAFT)) {
					transaction.onEdit(clonedTransaction);
				}
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

			if (serverObject instanceof CreatableObject) {
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
			Activity activity = null;
			if (serverObject instanceof Transaction) {
				if (((Transaction) serverObject).isVoid()) {
					activity = new Activity(company, user, ActivityType.VOID,
							serverObject);
				} else {
					activity = new Activity(company, user, ActivityType.EDIT,
							serverObject);
				}
			} else {
				activity = new Activity(company, user, ActivityType.EDIT,
						serverObject);
			}
			session.saveOrUpdate(activity);
			if (serverObject instanceof Transaction) {
				((Transaction) serverObject).setLastActivity(activity);
			}
			session.saveOrUpdate(serverObject);
			newTransaction.commit();

			if (serverObject instanceof Transaction) {
				org.hibernate.Transaction ht = session.beginTransaction();
				try {
					Transaction tt = (Transaction) serverObject;
					List<Item> inventory = tt.getInventoryUsed();
					InventoryUtils.remapSalesPurchases(inventory);
					ht.commit();
				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

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

		try {
			String arg1 = (context).getArg1();
			String arg2 = (context).getArg2();

			if (arg1 == null || arg2 == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Delete Operation Cannot be Processed id or cmd.arg2 Found Null...."
								+ context);
			}

			Class<?> clientClass = ObjectConvertUtil
					.getEqivalentClientClass(arg2);

			Class<?> serverClass = ObjectConvertUtil
					.getServerEqivalentClass(clientClass);

			IAccounterServerCore serverObject = (IAccounterServerCore) session
					.get(serverClass, Long.parseLong(arg1));

			if (serverObject == null) {
				throw new AccounterException(
						AccounterException.ERROR_ALREADY_DELETED);
			}
			// if (objects != null && objects.size() > 0) {

			// IAccounterServerCore serverObject = (IAccounterServerCore)
			// objects
			// .get(0);
			String userID = context.getUserEmail();
			Company company = getCompany(context.getCompanyId());
			User user1 = company.getUserByUserEmail(userID);

			if (serverObject instanceof FiscalYear) {
				((FiscalYear) serverObject)
						.canDelete((FiscalYear) serverObject);
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
			} else if (serverObject instanceof Budget) {
				session.delete(serverObject);
			} else if (serverObject instanceof TDSChalanDetail) {
				session.delete(serverObject);
			} else if (serverObject instanceof CustomField) {
				session.delete(serverObject);
			} else if (serverObject instanceof Transaction
					&& ((Transaction) serverObject).isTemplate()) {
				session.delete(((Transaction) serverObject)
						.getRecurringTransaction());
			} else if (serverObject instanceof StockAdjustment) {
				session.delete(serverObject);
			} else if (serverObject instanceof BuildAssembly) {
				session.delete(serverObject);
			} else if (serverObject instanceof MessageOrTask) {
				session.delete(serverObject);
			} else {
				if (serverObject instanceof Transaction) {
					Transaction transaction = (Transaction) serverObject;
					if (!transaction.getReconciliationItems().isEmpty()) {
						throw new AccounterException(
								AccounterException.ERROR_DELETING_TRANSACTION_RECONCILIED);
					} else if (transaction.getCreditsAndPayments() != null) {
						transaction.getCreditsAndPayments()
								.canEdit(transaction);
					}
				}
				if (serverObject instanceof TAXItem) {
					((TAXItem) serverObject).canDelete(serverObject);
				}
				if (canDelete(serverClass.getSimpleName(),
						Long.parseLong(arg1), company.getID())) {
					session.delete(serverObject);
				} else {
					throw new AccounterException(
							AccounterException.ERROR_OBJECT_IN_USE);
				}
			}
			Activity activity = new Activity(company, user1,
					ActivityType.DELETE, serverObject);
			session.save(activity);

			hibernateTransaction.commit();

			if (serverObject instanceof Transaction) {
				org.hibernate.Transaction ht = session.beginTransaction();
				try {
					Transaction tt = (Transaction) serverObject;
					List<Item> inventory = tt.getInventoryUsed();
					InventoryUtils.remapSalesPurchases(inventory);
					ht.commit();
				} catch (Exception e) {
					e.printStackTrace();
					ht.rollback();
				}
			}

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

	public boolean canDelete(String serverClass, long id, long companyId) {
		String queryName = getCanDeleteQueryName(serverClass);
		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery(queryName).setParameter("inputId", id)
				.setParameter("companyId", companyId);
		return executeQuery(query);
	}

	private String getCanDeleteQueryName(String serverClass) {
		StringBuffer query = new StringBuffer("canDelete");
		query.append(serverClass);
		// if (serverClass.equals("TAXItem") || serverClass.equals("TAXGroup"))
		// {
		// if (companyType == Company.ACCOUNTING_TYPE_US) {
		// query.append("ForUS");
		// }
		// }
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
								ServerCompanyID, user.getClient().getEmailId());
						if (stream == null) {
							continue;
						}
						for (IAccounterCore obj : changes) {
							if (obj != null) {
								stream.put(obj);
							}
						}
						log.info("Sent " + changes.length + " change to "
								+ user.getClient().getEmailId());
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
				for (Object obj : queryResult) {
					Object[] result = (Object[]) obj;
					for (Object object : result) {
						if (object != null) {
							flag = false;
							break;
						}
					}
					if (!flag) {
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

	public PaginationList<JournalEntry> getJournalEntries(long companyId,
			FinanceDate fromDate, FinanceDate toDate, int start, int length)
			throws DAOException {
		try {
			int total = 0;
			List<JournalEntry> list;
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session.getNamedQuery("getJournalEntry")
					.setEntity("company", company)
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate);
			// /If length will be -1 then get list for mobile With out limits
			if (length == -1) {
				list = query.list();
			} else {
				total = query.list().size();
				list = query.setFirstResult(start).setMaxResults(length).list();
			}
			PaginationList<JournalEntry> result = new PaginationList<JournalEntry>();

			for (JournalEntry j : list) {
				result.add(j);
			}
			result.setTotalCount(total);

			result.setStart(start);

			if (list != null) {
				return result;
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

	public String getNextIssuePaymentCheckNumber(long account, long companyId) {
		return NumberUtils.getNextCheckNumber(companyId, account);
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
					.setParameter("companyId", companyId)
					.setParameter("paymentDate", paymentDate1);
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
				double currencyFactor = je.getCurrencyFactor();
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
					ClientFinanceDate transactionDate = object[6] == null ? null
							: new ClientFinanceDate((Long) object[6]);
					receivePaymentTransactionList
							.setDiscountDate(transactionDate);
					receivePaymentTransactionList
							.setPayment((Double) object[7]);
					double discPerc = 0;
					if (object[8] != null) {
						boolean isDateDriven = (Boolean) object[8];
						int ifPaidWithin = (Integer) object[9];
						double discount = (Double) object[10];

						if (isDateDriven) {
							ClientFinanceDate currentDate = new ClientFinanceDate(
									paymentDate.getAsDateObject());
							if (currentDate.getYear() == transactionDate
									.getYear()
									&& currentDate.getMonth() == transactionDate
											.getMonth()
									&& currentDate.getDay() <= ifPaidWithin) {

								discPerc = discount;
							} else {
								discPerc = 0;
							}
						} else {
							long differenceBetween = UIUtils.getDays_between(
									transactionDate.getDateAsObject(),
									paymentDate.getAsDateObject());
							if (differenceBetween >= 0
									&& differenceBetween <= ifPaidWithin) {
								discPerc = discount;
							} else {
								discPerc = 0;
							}
						}
					}
					receivePaymentTransactionList
							.setCashDiscount((amountDue / 100) * (discPerc));
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

	public ArrayList<TransferFund> getLatestDeposits(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestDeposits")
					.setParameter("companyId", companyId);
			List list2 = query.list();

			Object[] object = null;
			Iterator iterator = list2.iterator();
			List<TransferFund> list = new ArrayList<TransferFund>();
			while (iterator.hasNext()) {
				object = (Object[]) iterator.next();
				TransferFund makeDeposit = new TransferFund();
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

	public long getSub(long accountId, long companyId, List<Long> list) {
		if (accountId == 0) {
			return 0;
		} else {

			for (Account account : getCompany(companyId).getAccounts()) {
				if (account.getParent() != null
						&& account.getParent().getID() == accountId) {
					list.add(account.getID());
					return getSub(account.getID(), companyId, list);
				}
			}
		}
		return 0;
	}

	public List<Long> getSubAccs(long accountId, long companyId) {
		ArrayList<Long> list = new ArrayList<Long>();
		if (accountId == 0) {
			return new ArrayList<Long>();
		} else {
			if (getSub(accountId, companyId, list) == 0) {
				return list;
			}
		}
		return new ArrayList<Long>();
	}

	public PaginationList<AccountRegister> getAccountRegister(
			final FinanceDate startDate, final FinanceDate endDate,
			final long accountId, long companyId, int start, int length)
			throws DAOException {
		int total = 0;
		List<Long> parents = new ArrayList<Long>();
		parents.add(accountId);
		parents.addAll(getSubAccs(accountId, companyId));
		List<AccountRegister> queryResult = new ArrayList<AccountRegister>();
		PaginationList<AccountRegister> result = new PaginationList<AccountRegister>();
		for (Long account : parents) {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session
					.getNamedQuery("getAccountRegister")
					.setParameter("companyId", companyId)
					.setParameter("accountId", account)
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("openingBalance",
							AccounterServerConstants.OPENING_BALANCE,
							EncryptedStringType.INSTANCE)
					.setParameter("creditors",
							AccounterServerConstants.ACCOUNTS_PAYABLE,
							EncryptedStringType.INSTANCE)
					.setParameter("debtors",
							AccounterServerConstants.ACCOUNTS_RECEIVABLE,
							EncryptedStringType.INSTANCE)
					.setParameter("multiple", "Multiple",
							EncryptedStringType.INSTANCE);

			List l = query.list();
			if (l != null && !(l.isEmpty())) {
				Object[] object = null;
				String payeeName = null;
				PayeeList payeeList = null;
				Iterator iterator = l.iterator();

				while ((iterator).hasNext()) {

					AccountRegister accountRegister = new AccountRegister();
					object = (Object[]) iterator.next();

					accountRegister.setDate(new ClientFinanceDate(
							(Long) object[0]));
					accountRegister.setType(object[1] == null ? 0
							: (Integer) object[1]);
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
					 * StringBuffer strOut = new StringBuffer(); String aux; try
					 * { BufferedReader br = new BufferedReader(cl
					 * .getCharacterStream()); while ((aux = br.readLine()) !=
					 * null) strOut.append(aux);
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
					accountRegister.setCurrency(object[11] == null ? 0
							: (Long) object[11]);
					accountRegister.setCurrencyfactor(object[12] == null ? 0
							: (Double) object[12]);
					queryResult.add(accountRegister);
				}
				total = queryResult.size();

				if (length < 0) {
					result.addAll(queryResult);
				} else {
					int toIndex = start + length;
					if (toIndex > queryResult.size()) {
						toIndex = queryResult.size();
					}
					if (start < toIndex) {
						result.addAll(queryResult.subList(start, toIndex));
					} else {
						return result;
					}
				}
				result.setTotalCount(total);
				result.setStart(start);
			}
		}
		return result;
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
					&& !(object instanceof ClientTransferFund)) {

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

	public static void createViews() {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		session.getNamedQuery("createSalesPurchasesView").executeUpdate();
		session.getNamedQuery("createTransactionHistoryView").executeUpdate();
		session.getNamedQuery("createDeleteCompanyFunction").executeUpdate();
		session.getNamedQuery("createInventoryPurchaseHistory").executeUpdate();
		session.getNamedQuery("getInventoryHistoryView").executeUpdate();
		session.getNamedQuery("JobsTransactionsView").executeUpdate();
		session.getNamedQuery("createSubscriptionFunction").executeUpdate();
		transaction.commit();
	}

	public static void createViewsForclient(long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("createSalesPurchasesViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
		session.getNamedQuery("createTransactionHistoryViewForclient")
				.setParameter("companyId", companyId).executeUpdate();
	}

	public PaginationList<PayeeList> getPayeeList(int category,
			boolean isActive, int start, int length, long companyId)
			throws AccounterException {
		try {
			Session session = HibernateUtil.getCurrentSession();
			int total = 0;
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

			if (category == Payee.TYPE_CUSTOMER) {

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
										.getTime()).getDate())
						.setParameter("isActive", isActive);

			} else if (category == Payee.TYPE_VENDOR) {

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
										.getTime()).getDate())
						.setParameter("isActive", isActive);
				;
			} else if (category == Payee.TYPE_TAX_AGENCY) {

				query = session
						.getNamedQuery("getTAXAgencyList")
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
										.getTime()).getDate())
						.setParameter("isActive", isActive);
				;
			}
			List list = query.list();
			if (list != null) {
				Object[] object = null;
				String payeeName = null;
				PayeeList payeeList = null;
				Iterator iterator = list.iterator();
				PaginationList<PayeeList> queryResult = new PaginationList<PayeeList>();
				while ((iterator).hasNext()) {

					object = (Object[]) iterator.next();

					if (payeeName != null && ((String) object[1]) != null
							&& payeeName.equals(object[1])) {

						double currentMount = (Double) object[4];
						payeeList.setCurrentMonth(payeeList.getCurrentMonth()
								+ (currentMount));

						double preMnt = (Double) object[5];
						payeeList.setPreviousMonth(payeeList.getPreviousMonth()
								+ (preMnt));

						double pre2Mnt = (Double) object[6];
						payeeList.setPreviousSecondMonth(payeeList
								.getPreviousSecondMonth() + (pre2Mnt));

						double pre3Mnt = (Double) object[7];
						payeeList.setPreviousThirdMonth(payeeList
								.getPreviousThirdMonth() + (pre3Mnt));

						double pre4mnt = (Double) object[8];
						payeeList.setPreviousFourthMonth(payeeList
								.getPreviousFourthMonth() + (pre4mnt));

						double pre5Mnt = (Double) object[9];
						payeeList.setPreviousFifthMonth(payeeList
								.getPreviousFifthMonth() + (pre5Mnt));

						double yearToDate = (Double) object[10];
						payeeList.setYearToDate(payeeList.getYearToDate()
								+ (yearToDate));

					} else {

						payeeList = new PayeeList();

						payeeList.setActive((object[0] == null ? false
								: ((Boolean) object[0]).booleanValue()));
						payeeName = (String) object[1];
						payeeList.setPayeeName(payeeName);
						payeeList.setType((Integer) object[2]);
						payeeList.setID((Long) object[3]);
						payeeList.setCurrecny((Long) object[13]);

						double currentMonth = (Double) object[4];
						payeeList.setCurrentMonth(currentMonth);
						double preMnt = (Double) object[5];
						payeeList.setPreviousMonth(preMnt);
						double pre2Mnt = (Double) object[6];
						payeeList.setPreviousSecondMonth(pre2Mnt);
						double pre3Mnt = (Double) object[7];
						payeeList.setPreviousThirdMonth(pre3Mnt);
						double pre4Mnt = (Double) object[8];
						payeeList.setPreviousFourthMonth(pre4Mnt);
						double pre5Mnt = (Double) object[9];
						payeeList.setPreviousFifthMonth(pre5Mnt);
						double yearToDate = (Double) object[10];
						payeeList.setYearToDate(yearToDate);
						payeeList.setBalance((Double) object[11]);

						queryResult.add(payeeList);
					}
				}
				total = queryResult.size();
				PaginationList<PayeeList> result = new PaginationList<PayeeList>();
				if (length < 0) {
					result.addAll(queryResult);
				} else {
					int toIndex = start + length;
					if (toIndex > queryResult.size()) {
						toIndex = queryResult.size();
					}
					result.addAll(queryResult.subList(start, toIndex));
				}
				result.setTotalCount(total);
				result.setStart(start);
				return result;
			} else
				throw (new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT));
		} catch (AccounterException e) {
			throw (new AccounterException(AccounterException.ERROR_INTERNAL, e));
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

	public PaginationList<ClientRecurringTransaction> getAllRecurringTransactions(
			long companyId, FinanceDate fromDate, FinanceDate toDate)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<RecurringTransaction> transactions = session
				.getNamedQuery("list.RecurringTransaction")
				.setEntity("company", getCompany(companyId))
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate).list();

		PaginationList<ClientRecurringTransaction> clientObjs = new PaginationList<ClientRecurringTransaction>();
		for (RecurringTransaction recurringTransaction : transactions) {
			ClientRecurringTransaction clientObject = new ClientConvertUtil()
					.toClientObject(recurringTransaction,
							ClientRecurringTransaction.class);

			clientObjs.add(clientObject);
		}

		return clientObjs;
	}

	/**
	 * 
	 * @param companyId
	 * @param payee
	 * @param tdate
	 * @return
	 * @throws AccounterException
	 */
	public double getMostRecentTransactionCurreencyFactorBasedOnCurrency(
			long companyId, long currencyId, long tdate)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery(
						"getMostRecentTransactionCurrencyFactor.orderby.id.basedon.currency")
				.setLong("companyId", companyId)
				.setLong("transactionDate", tdate)
				.setLong("currency", currencyId);

		List list = query.list();
		if (list.isEmpty()) {
			return 1;
		}
		Object factorObj = list.get(0);
		if (factorObj != null) {
			return ((Double) factorObj).doubleValue();
		} else {
			return 1;
		}
	}

	public ClientAccount mergeAcoount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount, long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);
		double mergeBalance = toClientAccount
				.getTotalBalanceInAccountCurrency()
				+ fromClientAccount.getTotalBalanceInAccountCurrency();

		try {
			session.getNamedQuery("update.merge.Account.oldBalance.tonew")
					.setLong("from", toClientAccount.getID())
					.setDouble("balance", mergeBalance)
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accounttransaction.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accountcustomercreditmemo.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			// session.getNamedQuery("update.merge.issuepayment.old.tonew")
			// .setLong("fromID", fromClientAccount.getID())
			// .setLong("toID", toClientAccount.getID())
			// .setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttrasactionexpense.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.payexpense.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();
			session.getNamedQuery(
					"update.merge.accountreceivepayment.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.transactionitem.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.transactionitemeffectingAccount.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.journalEntry.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery(
					"update.merge.transactionmakedepositentries.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID()).executeUpdate();

			session.getNamedQuery("update.merge.accountwritecheck.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accounttaxrefund.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountpaytax.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcustomerrefud.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accountcustomerprepay.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountvendorrprepay.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcashsale.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.accountcashpurchase.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttransferfundsto.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery(
					"update.merge.accounttransferfundsfrom.old.tonew")
					.setLong("fromID", fromClientAccount.getID())
					.setLong("toID", toClientAccount.getID())
					.setEntity("company", company).executeUpdate();

			Account account = (Account) session.get(Account.class,
					toClientAccount.getID());

			Account fromAccount = (Account) session.get(Account.class,
					fromClientAccount.getID());
			User user = AccounterThreadLocal.get();
			Activity activity = new Activity(company, user, ActivityType.MERGE,
					account);
			session.save(activity);

			company.getAccounts().remove(fromAccount);
			session.saveOrUpdate(company);
			fromAccount.setCompany(null);
			session.delete(fromAccount);
			tx.commit();
			Account toaccount = (Account) session.get(Account.class,
					toClientAccount.getID());
			return (ClientAccount) new ClientConvertUtil().toClientObject(
					toaccount, Util.getClientClass(toaccount));
		} catch (Exception e) {
			tx.rollback();
		}
		return null;

	}

	public void mergeItem(ClientItem fromClientItem, ClientItem toClientItem,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		Company company = getCompany(companyId);

		try {

			session.getNamedQuery("update.mergeItem.oldcost.tonewcost")
					.setLong("from", toClientItem.getID())
					.setBoolean("status", fromClientItem.isActive())
					.setDouble("price", fromClientItem.getSalesPrice())
					.setDouble("p_Price", fromClientItem.getPurchasePrice())
					.setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.itemstatus.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			// session.getNamedQuery("update.merge.itemsalesorder.old.tonew")
			// .setLong("fromID", fromClientItem.getID())
			// .setLong("toID", toClientItem.getID())
			// .setEntity("company", company).executeUpdate();

			session.getNamedQuery("update.merge.trasactionexpense.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			session.getNamedQuery("update.merge.trasactionitem.old.tonew")
					.setLong("fromID", fromClientItem.getID())
					.setLong("toID", toClientItem.getID()).executeUpdate();

			Item toItem = (Item) session.get(Item.class, toClientItem.getID());

			Item fromItem = (Item) session.get(Item.class,
					fromClientItem.getID());

			User user = AccounterThreadLocal.get();

			Activity activity = new Activity(company, user, ActivityType.MERGE,
					toItem);

			session.save(activity);
			company.getItems().remove(fromItem);
			session.saveOrUpdate(company);
			fromItem.setCompany(null);
			session.delete(fromItem);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
	}

	public void recordActivity(Company company, User user, ActivityType type) {
		Activity activity = new Activity(company, user, type);

	}

	/**
	 * This method will create a duplicate transaction for argument transaction.
	 * 
	 * @param recurringTransaction
	 * @return {@link Transaction}
	 * @throws AccounterException
	 * @throws CloneNotSupportedException
	 */
	public static Transaction createDuplicateTransaction(
			RecurringTransaction recurringTransaction)
			throws AccounterException, CloneNotSupportedException {

		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);

		Transaction transaction = recurringTransaction.getTransaction();

		Transaction newTransaction = transaction.clone();
		newTransaction.setCompany(recurringTransaction.getCompany());
		newTransaction.setRecurringTransaction(recurringTransaction);
		newTransaction.setNumber(NumberUtils.getNextTransactionNumber(
				newTransaction.getType(), recurringTransaction.getCompany()));
		for (TransactionItem transactionItem : newTransaction
				.getTransactionItems()) {
			transactionItem.setTransaction(newTransaction);
			transactionItem.setReferringTransactionItem(null);
			transactionItem.setPurchases(new HashSet<InventoryPurchase>());
		}
		FinanceDate transactionDate = recurringTransaction
				.getNextScheduledTransactionDate();
		if (transactionDate != null) {
			newTransaction.setDate(transactionDate);
			if (newTransaction instanceof Invoice) {
				((Invoice) newTransaction).setDueDate(transactionDate);
				((Invoice) newTransaction).setDeliverydate(transactionDate);
			} else if (newTransaction instanceof EnterBill) {
				((EnterBill) newTransaction).setDueDate(transactionDate);
				((EnterBill) newTransaction).setDeliveryDate(transactionDate);
			} else if (newTransaction instanceof CashPurchase) {
				((CashPurchase) newTransaction)
						.setDeliveryDate(transactionDate);
			} else if (newTransaction instanceof Estimate) {
				((Estimate) newTransaction).setExpirationDate(transactionDate);
				((Estimate) newTransaction).setDeliveryDate(transactionDate);
			} else if (newTransaction instanceof MakeDeposit) {
				for (TransactionDepositItem transactionItem : ((MakeDeposit) newTransaction)
						.getTransactionDepositItems()) {
					transactionItem.setTransaction(newTransaction);
				}
			}
		}
		newTransaction
				.setAccountTransactionEntriesList(new HashSet<AccountTransaction>());
		newTransaction
				.setTaxRateCalculationEntriesList(new HashSet<TAXRateCalculation>());
		newTransaction
				.setReconciliationItems(new HashSet<ReconciliationItem>());
		newTransaction.setAttachments(new HashSet<Attachment>());
		newTransaction.setLastActivity(null);
		newTransaction.setHistory(null);

		session.setFlushMode(flushMode);

		return newTransaction;
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
	 * @return
	 * @throws TemplateSyntaxException
	 * @throws IOException
	 */
	public void sendPdfInMail(String fileName, String subject, String content,
			ClientEmailAccount sender, String toEmail, String ccEmail,
			long companyId) throws AccounterException {

		Company company = getCompany(companyId);
		String companyName = company.getTradingName();
		File file = new File(fileName);
		try {
			UsersMailSendar.sendPdfMail(file, companyName, subject, content,
					sender, toEmail, ccEmail);
		} catch (IOException e) {
			throw new AccounterException(e.getMessage());
		}

	}

	/**
	 * to generate PDF File for Invoice
	 * 
	 * @throws Exception
	 */
	public String createPdfFile(long objectID, int type, long brandingThemeId,
			long companyId) throws Exception {

		Manager manager = getManager();
		BrandingTheme brandingTheme = (BrandingTheme) manager
				.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
						brandingThemeId);

		String fileName = "";
		Company company = getCompany(companyId);
		PrintTemplete printTemplete = null;
		File file = null;
		if (type == Transaction.TYPE_INVOICE) {
			Invoice invoice = (Invoice) manager.getServerObjectForid(
					AccounterCoreType.INVOICE, objectID);

			if (brandingTheme.isCustomFile()) {
				// for custom Branding Theme

				InvoicePdfGeneration pdf = new InvoicePdfGeneration(invoice,
						company, brandingTheme);

				String templeteName = "";
				if (brandingTheme.getInvoiceTempleteName().equalsIgnoreCase(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "InvoiceOdt.odt";
				} else {
					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getInvoiceTempleteName();
				}
				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				IXDocReport report = XDocReportRegistry.getRegistry()
						.loadReport(in, TemplateEngineKind.Velocity);
				IContext context = report.createContext();
				context = pdf.assignValues(context, report);

				Options options = Options.getTo(ConverterTypeTo.PDF).via(
						ConverterTypeVia.ITEXT);
				fileName = "Invoice_" + invoice.getNumber();
				file = File.createTempFile(fileName.replace(" ", ""), ".pdf");
				java.io.FileOutputStream fos = new java.io.FileOutputStream(
						file);
				report.convert(context, options, fos);

			} else {
				// for regular html branding theme
				printTemplete = new InvoicePDFTemplete(invoice, brandingTheme,
						company, "ClassicInvoice");

				fileName = printTemplete.getFileName();

				String output = printTemplete.getPdfData();

				java.io.InputStream inputStream = new ByteArrayInputStream(
						output.getBytes("UTF-8"));

				InputStreamReader reader = new InputStreamReader(inputStream,
						Charset.forName("UTF-8"));
				Converter converter = new Converter();
				file = converter.getPdfFile(printTemplete, reader);
			}
			// UsersMailSendar.sendPdfMail(file, companyName, subject, content,
			// senderEmail, toEmail, ccEmail);
		}
		return file.getPath();

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

	public PaginationList<ClientBudget> getBudgetList(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		try {

			ArrayList<Budget> budgetList = new ArrayList<Budget>(session
					.getNamedQuery("list.Budget")
					.setEntity("company", getCompany(companyId)).list());

			PaginationList<ClientBudget> clientBudgetObjs = new PaginationList<ClientBudget>();

			for (Budget budget : budgetList) {
				ClientBudget clientObject = new ClientConvertUtil()
						.toClientObject(budget, ClientBudget.class);

				clientBudgetObjs.add(clientObject);
			}

			return clientBudgetObjs;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public List<ClientReconciliationItem> getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List list = session
				.getNamedQuery("getTransactionsOfAccount")
				.setLong("companyId", companyId)
				.setLong("accountId", id)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setLong("openingBalanceAccount",
						company.getOpeningBalancesAccount().getID()).list();
		List<ClientReconciliationItem> reconciliationItems = new ArrayList<ClientReconciliationItem>();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {

			Object[] objects = (Object[]) iterator.next();

			Transaction trasnaction = (Transaction) session.get(
					Transaction.class, (Long) objects[0]);

			ReconciliationItem reconciliationItem = new ReconciliationItem(
					trasnaction);

			Double amount = (Double) objects[1];

			reconciliationItem.setAmount(amount);

			reconciliationItems
					.add((ClientReconciliationItem) new ClientConvertUtil()
							.toClientObject(reconciliationItem,
									Util.getClientClass(reconciliationItem)));

		}
		return reconciliationItems;
	}

	/**
	 * @param accountID
	 */
	public ArrayList<ClientReconciliation> getReconciliationsByBankAccountID(
			long accountID, long companyID) throws AccounterException {
		List<Reconciliation> reconciliations = getReconciliationslist(
				accountID, companyID);
		ArrayList<ClientReconciliation> reconciliationsList = new ArrayList<ClientReconciliation>();
		Iterator iterator = reconciliations.iterator();
		ClientConvertUtil convertUtil = new ClientConvertUtil();
		while (iterator.hasNext()) {
			Reconciliation next = (Reconciliation) iterator.next();
			Hibernate.initialize(next.getItems());
			ClientReconciliation clientObject = convertUtil.toClientObject(
					next, ClientReconciliation.class);
			reconciliationsList.add(clientObject);
		}
		return reconciliationsList;

	}

	public List<Reconciliation> getReconciliationslist(long accountID,
			long companyID) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyID);
		List list = session.getNamedQuery("get.reconciliations.by.accountId")
				.setLong("accountID", accountID).setEntity("company", company)
				.list();
		return list;

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
			TransactionLog noteHistory = new TransactionLog(
					TransactionLog.TYPE_NOTE);
			Transaction transaction2 = (Transaction) session.get(
					Transaction.class, transactionId);
			noteHistory.setDescription(noteDescription);
			noteHistory.setCompany(company);
			noteHistory.setTransaction(transaction2);
			List<TransactionLog> history = transaction2.getHistory();
			history.add(noteHistory);
			session.saveOrUpdate(transaction2);
			transaction.commit();
			return noteHistory.getID();
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
	public List<ClientTransactionLog> getTransactionHistory(long transactionId,
			long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = (Transaction) session.get(Transaction.class,
				transactionId);
		if (transaction == null) {
			return new ArrayList<ClientTransactionLog>();
		}
		List<TransactionLog> history = transaction.getHistory();
		List<ClientTransactionLog> transactionLogs = new ArrayList<ClientTransactionLog>();
		Iterator iterator = history.iterator();

		while (iterator.hasNext()) {
			TransactionLog log = (TransactionLog) iterator.next();
			ClientTransactionLog clientTransactionLog = new ClientConvertUtil()
					.toClientObject(log, ClientTransactionLog.class);
			transactionLogs.add(clientTransactionLog);
		}
		return transactionLogs;
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

	public boolean createMessage(Message message) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();

			if (message.getId() == 0) {
				Query messageQuery = session.getNamedQuery("getMessageByValue")
						.setParameter("value", message.getValue());
				Message oldMessage = (Message) messageQuery.uniqueResult();
				if (oldMessage != null) {
					Set<Key> keys = oldMessage.getKeys();
					keys.addAll(message.getKeys());
					oldMessage.setKeys(keys);
					message = oldMessage;
				}
			}

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(message);
			transaction.commit();

			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// public ClientMessage getNextMessage(String lang, int lastMessageId)
	// throws AccounterException {
	// Session session = null;
	// try {
	// session = HibernateUtil.openSession();
	// Query query = session.getNamedQuery("getNextMessageId")
	// .setParameter("lastMessageId", lastMessageId);
	// int messageId = 0;
	// Object[] object = (Object[]) query.uniqueResult();
	// if (object != null) {
	// messageId = (Integer) object[0];
	// Integer tot = (Integer) object[1];
	// }
	//
	// if (messageId == 0) {
	// return null;
	// }
	// ClientMessage clientMessage = getMessage(messageId, lang);
	// int i = 0;
	// return clientMessage;
	// } catch (Exception e) {
	// return null;
	// } finally {
	// if (session != null) {
	// session.close();
	// }
	// }
	// }

	public PaginationList<ClientMessage> getMessages(int status, String lang,
			String email, int frm, int limit, String searchTerm) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();
			PaginationList<Message> messages = new PaginationList<Message>();
			switch (status) {
			case ClientMessage.ALL:
				messages = getMessagesList(lang, frm, limit, searchTerm);
				break;

			case ClientMessage.UNTRANSLATED:
				messages = getUntranslatedMessages(lang, frm, limit, searchTerm);
				break;

			case ClientMessage.MYTRANSLATIONS:
				messages = getMyTranslations(lang, email, frm, limit,
						searchTerm);
				break;

			case ClientMessage.UNCONFIRMED:
				messages = getUnApprovedMessages(lang, frm, limit, searchTerm);
				break;

			default:
				break;
			}

			PaginationList<ClientMessage> clientMessages = new PaginationList<ClientMessage>();
			clientMessages.setTotalCount(messages.getTotalCount());

			for (Message message : messages) {

				Set<LocalMessage> localMessages = new HashSet<LocalMessage>();

				for (LocalMessage localMessage : message.getLocalMessages()) {
					if (localMessage.getLang().getLanguageCode().equals(lang)) {
						localMessages.add(localMessage);
					}
				}
				message.setLocalMessages(localMessages);

				ClientMessage clientMessage = new ClientMessage();
				clientMessage.setId(message.getId());
				clientMessage.setValue(message.getValue());
				clientMessage.setComment(message.getComment());
				ArrayList<ClientLocalMessage> clientLocalMessages = new ArrayList<ClientLocalMessage>();
				for (LocalMessage localMessage : localMessages) {
					ClientLocalMessage clientLocalMessage = new ClientLocalMessage();
					clientLocalMessage.setId(localMessage.getId());
					clientLocalMessage.setValue(localMessage.getValue());
					clientLocalMessage.setApproved(localMessage.isApproved());
					clientLocalMessage.setCreateBy(localMessage.getCreatedBy()
							.getFirstName());
					clientLocalMessage.setVotes(localMessage.getUps());
					clientLocalMessages.add(clientLocalMessage);
				}
				clientMessage.setLocalMessages(clientLocalMessages);
				clientMessages.add(clientMessage);
			}
			return clientMessages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private PaginationList<Message> getUnApprovedMessages(String lang, int frm,
			int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.openSession();

			Query approvedMessagesQuery = session
					.getNamedQuery("getUnApprovedMessages")
					.setParameter("lang", lang).setInteger("limt", limit)
					.setInteger("fm", frm)
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfUnApprovedMessages")
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			Iterator iter = approvedMessagesQuery.list().iterator();
			while (iter.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iter.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private PaginationList<Message> getMyTranslations(String lang,
			String email, int frm, int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.openSession();
			Client client = getUserManager().getClient(email);
			if (client == null) {
				return null;
			}
			Query myTranslationsQuery = session
					.getNamedQuery("getMyTranslations")
					.setParameter("lang", lang).setInteger("fm", frm)
					.setInteger("limt", limit)
					.setParameter("clientId", client.getID())
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfMyTranslations")
					.setParameter("lang", lang)
					.setParameter("clientId", client.getID())
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List queryList = myTranslationsQuery.list();
			Iterator i = queryList.iterator();
			while (i.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) i.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private PaginationList<Message> getUntranslatedMessages(String lang,
			int frm, int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.openSession();
			Query messageIdsQuery = session
					.getNamedQuery("getUntranslatedMessages")
					.setParameter("lang", lang).setInteger("limt", limit)
					.setInteger("fm", frm)
					.setParameter("searchTerm", "%" + searchTerm + "%");

			int count = ((BigInteger) session
					.getNamedQuery("getCountOfUntranslatedMessages")
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List list = messageIdsQuery.list();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iterator.next();
				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);
				message.setLocalMessages(new HashSet<LocalMessage>());
				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private PaginationList<Message> getMessagesList(String lang, int frm,
			int limit, String searchTerm) {
		PaginationList<Message> messages = new PaginationList<Message>();

		Session session = null;
		try {
			session = HibernateUtil.openSession();
			Query query = session.getNamedQuery("getMessagesByLimit")
					.setInteger("fm", frm).setInteger("limt", limit)
					.setParameter("lang", lang)
					.setParameter("searchTerm", "%" + searchTerm + "%");
			int count = ((BigInteger) session
					.getNamedQuery("getCountOfMessages")
					.setParameter("searchTerm", "%" + searchTerm + "%")
					.uniqueResult()).intValue();

			messages.setTotalCount(count);

			List list2 = query.list();
			Iterator iterator1 = list2.iterator();
			while (iterator1.hasNext()) {
				Message message = new Message();
				Object[] next = (Object[]) iterator1.next();

				message.setId(((BigInteger) next[0]).longValue());
				message.setValue((String) next[1]);
				message.setComment((String) next[3]);

				Query localMessagesQuery = session
						.getNamedQuery("getLocalMessagesByMessageId")
						.setParameter("messageId", message.getId())
						.setParameter("lang", lang);
				List<LocalMessage> localMessages = localMessagesQuery.list();

				message.setLocalMessages(new HashSet<LocalMessage>(
						localMessages));

				messages.add(message);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean addVote(long localMessageId, String userEmail) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();

			Client client = getUserManager().getClient(userEmail);
			if (client == null) {
				return false;
			}
			Query query = session.getNamedQuery("getLocalMessageById")
					.setParameter("id", localMessageId);
			LocalMessage localMessage = (LocalMessage) query.uniqueResult();
			if (localMessage == null) {
				return false;
			}

			long clientId = client.getID();
			Query voteQuery = session.getNamedQuery("getVoteByClientId")
					.setParameter("clientId", clientId)
					.setParameter("localMessageId", localMessage.getId());
			Vote vote = (Vote) voteQuery.uniqueResult();

			if (vote == null) {
				vote = new Vote();
				vote.setLocalMessage(localMessage);
				vote.setClient(client);
				localMessage.setUps(localMessage.getUps() + 1);

				org.hibernate.Transaction voteTransaction = session
						.beginTransaction();
				session.saveOrUpdate(vote);
				voteTransaction.commit();

				org.hibernate.Transaction transaction = session
						.beginTransaction();
				session.saveOrUpdate(localMessage);
				transaction.commit();
			}

			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ClientLocalMessage addTranslation(String userEmail, long id,
			String lang, String value) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();

			Client client = getUserManager().getClient(userEmail);
			if (client == null) {
				return null;
			}

			Query namedQuery = session.getNamedQuery("getLocalMessageByClient")
					.setParameter("clientId", client.getID())
					.setParameter("messageId", id).setParameter("lang", lang);
			LocalMessage uniqueResult = (LocalMessage) namedQuery
					.uniqueResult();
			if (uniqueResult != null) {

				Query deleteVotesQuery = session.getNamedQuery(
						"deleteVotesByLocalMessage").setParameter("id",
						uniqueResult.getId());
				Query deleteQuery = session.getNamedQuery("deleteLocalMessage")
						.setParameter("id", uniqueResult.getId());

			}

			Query messageQuery = session.getNamedQuery("getMessageById")
					.setParameter("id", id);
			Message message = (Message) messageQuery.uniqueResult();
			Set<LocalMessage> localMessages2 = message.getLocalMessages();
			for (LocalMessage localMessage : localMessages2) {
				if (localMessage.getCreatedBy() != null
						&& localMessage.getCreatedBy().equals(client)
						&& localMessage.getLang().equals(lang)) {
					return null;
				}
			}

			Query languageQuery = session.getNamedQuery("getLanguageById")
					.setParameter("code", lang);
			Language language = (Language) languageQuery.uniqueResult();

			LocalMessage localMessage = new LocalMessage();
			localMessage.setMessage(message);
			localMessage.setLang(language);
			localMessage.setValue(value);
			localMessage.setCreatedDate(new Date(System.currentTimeMillis()));
			localMessage.setCreatedBy(client);

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(localMessage);
			transaction.commit();

			addVote(localMessage.getId(), userEmail);

			org.hibernate.Transaction mesgTransaction = session
					.beginTransaction();
			Set<LocalMessage> localMessages = message.getLocalMessages();
			localMessages.add(localMessage);
			message.setLocalMessages(localMessages);
			session.saveOrUpdate(message);
			mesgTransaction.commit();

			ClientLocalMessage clm = new ClientLocalMessage();
			clm.setApproved(localMessage.isApproved());
			if (localMessage.getCreatedBy() != null) {
				clm.setCreateBy(localMessage.getCreatedBy().getEmailId());
			}
			clm.setId(localMessage.getId());
			clm.setValue(localMessage.getValue());
			clm.setVotes(localMessage.getUps());

			return clm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// public ArrayList<Status> getTranslationStatus() {
	// Session session = null;
	// try {
	// session = HibernateUtil.openSession();
	//
	// ArrayList<Status> list = new ArrayList<Status>();
	//
	// List<String> languages = getLanguages();
	//
	// Query noOfMessagesQuery = session.getNamedQuery("getNoOfMessages");
	// BigInteger noOfMessages = (BigInteger) noOfMessagesQuery
	// .uniqueResult();
	//
	// for (String language : languages) {
	//
	// Query noOfTranslatedMessagesQuery = session.getNamedQuery(
	// "getNoOfMessagesTranslatedByLang").setParameter("lang",
	// language);
	// BigInteger translatedMessages = (BigInteger) noOfTranslatedMessagesQuery
	// .uniqueResult();
	//
	// Query noOfApprovedMessagesQuery = session.getNamedQuery(
	// "getNoOfMessagesApprovedByLang").setParameter("lang",
	// language);
	// BigInteger approvedMessages = (BigInteger) noOfApprovedMessagesQuery
	// .uniqueResult();
	//
	// Status status = new Status();
	// status.setTotal(noOfMessages.intValue());
	// status.setLang(language);
	// status.setTranslated(translatedMessages.intValue());
	// status.setApproved(approvedMessages.intValue());
	//
	// list.add(status);
	// }
	// return list;
	// } catch (Exception e) {
	// return null;
	// } finally {
	// if (session != null) {
	// session.close();
	// }
	// }
	//
	// }

	public List<Message> getAllMessages() {
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getMessages");
			List<Message> list = query.list();

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean setApprove(long id, boolean isApproved) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();

			Query query = session.getNamedQuery("getLocalMessageById")
					.setParameter("id", id);
			LocalMessage localMessage = (LocalMessage) query.uniqueResult();

			if (localMessage == null) {
				return false;
			}

			Query messagesQuery = session
					.getNamedQuery("getLocalMessagesByMessageId")
					.setParameter("messageId",
							localMessage.getMessage().getId())
					.setParameter("lang",
							localMessage.getLang().getLanguageCode());
			List<LocalMessage> list = messagesQuery.list();

			for (LocalMessage locMessage : list) {
				if (locMessage.isApproved()) {
					locMessage.setApproved(false);
					org.hibernate.Transaction transaction = session
							.beginTransaction();
					session.saveOrUpdate(locMessage);
					transaction.commit();
				}
			}

			localMessage.setApproved(isApproved);

			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(localMessage);
			transaction.commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean deleteMessage(Message message) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();
			org.hibernate.Transaction transaction = session.beginTransaction();
			session.delete(message);
			transaction.commit();

			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private long updatePrimaryCurrency(long companyId, ClientCurrency currency)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Currency existingCurrency = company.getCurrency(currency
				.getFormalName());
		if (existingCurrency == null) {
			existingCurrency = new Currency();
			existingCurrency = new ServerConvertUtil().toServerObject(
					existingCurrency, currency, session);
			existingCurrency.setCompany(company);
			session.save(existingCurrency);
		}
		company.getPreferences().setPrimaryCurrency(existingCurrency);
		Query query = session
				.getNamedQuery("update.primay.currency.in.company")
				.setParameter("companyId", companyId)
				.setParameter("currencyId", existingCurrency.getID());
		query.executeUpdate();
		return existingCurrency.getID();
	}

	/**
	 * Return all local messages of given language for the given client. All
	 * Values must be single quote escaped for Javascript
	 * 
	 * ex: that's => that\'s
	 * 
	 * @param clientId
	 * @param langCode
	 * @return
	 */
	public HashMap<String, String> getKeyAndValues(long clientId,
			String langCode) {
		// Replace("'", "\\'")
		Session session = null;
		try {
			session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getKeyAndValues")
					.setParameter("clientId", clientId)
					.setParameter("lang", langCode);
			List list = query.list();

			HashMap<String, String> result = new HashMap<String, String>();

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] next = (Object[]) iterator.next();
				String key = (String) next[0];
				String value = (String) next[1];
				if (result.containsKey(key)) {
					continue;
				}
				// String replace = value.replace("'", "\\'");
				result.put(key, value);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ClientLanguage> getLanguages() {
		Session session = null;
		try {
			session = HibernateUtil.openSession();

			Query query = session.getNamedQuery("getLanguages");
			List<Language> list = query.list();

			ArrayList<ClientLanguage> result = new ArrayList<ClientLanguage>();

			for (Language language : list) {
				ClientLanguage clientLanguage = new ClientLanguage(
						language.getLanguageTooltip(),
						language.getLangugeName(), language.getLanguageCode());
				result.add(clientLanguage);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean canApprove(String userEmail, String lang) {
		Session session = null;
		try {
			session = HibernateUtil.openSession();
			Client client = getUserManager().getClient(userEmail);
			for (Language language : client.getLanguages()) {
				if (language.getLanguageCode().equals(lang)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public boolean savePortletPageConfig(
			ClientPortletPageConfiguration pageConfiguration) {
		if (pageConfiguration == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PortletPageConfiguration serverObj = null;
			String pageName = pageConfiguration.getPageName();
			User user = AccounterThreadLocal.get();
			long userId = user.getID();
			Query query = session.getNamedQuery("getPortletPageConfiguration")
					.setParameter("pageName", pageName)
					.setParameter("userId", userId);
			serverObj = (PortletPageConfiguration) query.uniqueResult();
			if (serverObj == null) {
				serverObj = new PortletPageConfiguration();
				serverObj.setUser(user);
			}
			if (serverObj.getID() != 0) {
				pageConfiguration.setId(serverObj.getID());
			}
			serverObj = new ServerConvertUtil().toServerObject(serverObj,
					(IAccounterCore) pageConfiguration, session);
			session.saveOrUpdate(serverObj);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally {
		}
	}

	public ClientPortletPageConfiguration getPortletPageConfiguration(
			String pageName) {
		if (pageName == null) {
			return null;
		}
		Session session = HibernateUtil.getCurrentSession();
		try {
			long userId = AccounterThreadLocal.get().getID();
			long companyId = AccounterThreadLocal.get().getCompany().getId();
			Query query = session.getNamedQuery("getPortletPageConfiguration")
					.setParameter("pageName", pageName)
					.setParameter("userId", userId)
			/* .setParameter("companyId", companyId) */;
			PortletPageConfiguration pageConfiguration = (PortletPageConfiguration) query
					.uniqueResult();
			ClientPortletPageConfiguration clientPortletPageConfiguration;
			if (pageConfiguration != null) {
				clientPortletPageConfiguration = new ClientConvertUtil()
						.toClientObject(pageConfiguration,
								ClientPortletPageConfiguration.class);
				return clientPortletPageConfiguration;
			} else {
				ClientCompany company = new ClientConvertUtil().toClientObject(
						getCompany(companyId), ClientCompany.class);
				return company.getPortletPageConfiguration(pageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}
	}

	/**
	 * getting the recent Transactions
	 * 
	 * @param companyId
	 * @param limit
	 * @return List<RecentTransactionsList>
	 */
	public List<RecentTransactionsList> getRecentTransactionsList(
			long companyId, int limit) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			List l = session.getNamedQuery("getRecentTransactionList")
					.setParameter("companyId", companyId)
					.setParameter("limit", limit).list();
			Object[] object = null;
			Iterator iterator = l.iterator();
			List<RecentTransactionsList> activities = new ArrayList<RecentTransactionsList>();
			while (iterator.hasNext()) {
				RecentTransactionsList recentTransactionsList = new RecentTransactionsList();
				object = (Object[]) iterator.next();
				recentTransactionsList.setID(((BigInteger) object[0])
						.longValue());
				recentTransactionsList.setType((Integer) (object[1]));
				recentTransactionsList.setAmount((Double) (object[2]));
				recentTransactionsList.setName(object[3] != null ? String
						.valueOf(object[3]) : null);
				recentTransactionsList
						.setTransactionDate(object[4] == null ? null
								: new ClientFinanceDate(
										((BigInteger) object[4]).longValue()));
				recentTransactionsList.setCurrecyId(object[5] == null ? null
						: ((BigInteger) object[5]).longValue());
				recentTransactionsList.setEstimateType((Integer) object[6]);
				activities.add(recentTransactionsList);
			}
			return activities;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public boolean deleteTransactionFromDb(Long companyId, IAccounterCore obj)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction transaction = session.beginTransaction();

		IAccounterServerCore serverObject = null;
		serverObject = new ServerConvertUtil().toServerObject(serverObject,
				obj, session);

		if (serverObject == null) {
			throw new AccounterException(
					AccounterException.ERROR_ILLEGAL_ARGUMENT);
		}

		if (serverObject instanceof Transaction) {
			Transaction trans = (Transaction) serverObject;
			trans.onDelete(session);
		}

		Query query = session.getNamedQuery("getTaxreturnByTransactionid")
				.setParameter("transaction", serverObject);
		List<TAXReturnEntry> list = query.list();

		for (TAXReturnEntry taxReturnEntry : list) {
			taxReturnEntry.setTransaction(null);
			session.save(taxReturnEntry);
		}

		Class<?> clientClass = ObjectConvertUtil.getEqivalentClientClass(obj
				.getObjectType().getClientClassSimpleName());
		Class<?> serverClass = ObjectConvertUtil
				.getServerEqivalentClass(clientClass);

		if (canDelete(serverClass.getSimpleName(), obj.getID(), companyId)) {
			session.delete(serverObject);
		} else {
			throw new AccounterException(AccounterException.ERROR_OBJECT_IN_USE);
		}

		transaction.commit();

		return true;
	}

	public boolean createOrSkipTransactions(List<ClientReminder> records,
			boolean isCreate, long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		if (!isCreate) {
			// If user wanted to skip transactions.
			for (ClientReminder obj : records) {
				Reminder reminder = null;
				reminder = new ServerConvertUtil().toServerObject(reminder,
						obj, session);
				RecurringTransaction recurringTransaction = reminder
						.getRecurringTransaction();
				recurringTransaction.getReminders().remove(reminder);
				session.saveOrUpdate(recurringTransaction);
			}
		} else {
			for (ClientReminder obj : records) {
				Reminder reminder = null;
				reminder = new ServerConvertUtil().toServerObject(reminder,
						obj, session);

				Transaction transaction = null;
				try {
					transaction = createDuplicateTransaction(reminder
							.getRecurringTransaction());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					throw new AccounterException(e.getMessage());
				}
				FinanceDate transactionDate = reminder.getTransactionDate();
				if (transactionDate != null) {
					transaction.setDate(transactionDate);
					if (transaction instanceof Invoice) {
						((Invoice) transaction).setDueDate(transactionDate);
					} else if (transaction instanceof EnterBill) {
						((EnterBill) transaction).setDueDate(transactionDate);
					} else if (transaction instanceof CashPurchase) {
						((CashPurchase) transaction)
								.setDeliveryDate(transactionDate);
					} else if (transaction instanceof Estimate) {
						((Estimate) transaction)
								.setExpirationDate(transactionDate);
						((Estimate) transaction)
								.setDeliveryDate(transactionDate);
					}
				}
				session.saveOrUpdate(transaction);
				RecurringTransaction recurringTransaction = reminder
						.getRecurringTransaction();
				recurringTransaction.getReminders().remove(reminder);
				session.saveOrUpdate(recurringTransaction);
			}
		}
		deleteRecurringTask(session, companyId);
		hibernateTransaction.commit();
		return true;
	}

	public void deleteRecurringTask(Session session, long companyId) {
		List<Reminder> list = session.getNamedQuery("getReminders")
				.setLong("companyId", companyId).list();

		if (list == null || list.isEmpty()) {
			MessageOrTask task = (MessageOrTask) session
					.getNamedQuery("getRecurringReminderTask")
					.setParameter("companyId", companyId).uniqueResult();
			session.delete(task);
		}
	}

	public void doCreateIssuePaymentEffect(Long companyId,
			ClientIssuePayment obj) {

		List<ClientTransactionIssuePayment> transactionIssuePayment = obj
				.getTransactionIssuePayment();
		long accountId = obj.getAccount();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : transactionIssuePayment) {
			Company company = getCompany(companyId);
			String nextCheckNumber = NumberUtils.getNextCheckNumber(companyId,
					accountId);
			if (UIUtils.toLong(obj.getCheckNumber()) > UIUtils
					.toLong(nextCheckNumber)) {
				nextCheckNumber = obj.getCheckNumber();
			}

			Transaction transaction = null;
			int recordType = clientTransactionIssuePayment.getRecordType();
			switch (recordType) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getWriteCheck(), company);
				WriteCheck writeCheck = (WriteCheck) transaction;
				writeCheck.setCheckNumber(nextCheckNumber);
				transaction = writeCheck;
				break;

			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCashPurchase(),
						company);
				CashPurchase cashPurchase = (CashPurchase) transaction;
				cashPurchase.setCheckNumber(nextCheckNumber);
				transaction = cashPurchase;
				break;

			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCustomerRefund(),
						company);
				CustomerRefund customerRefund = (CustomerRefund) transaction;
				customerRefund.setCheckNumber(nextCheckNumber);
				transaction = customerRefund;
				break;

			case ClientTransaction.TYPE_PAY_TAX:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getPaySalesTax(), company);
				PayTAX payTax = (PayTAX) transaction;
				payTax.setCheckNumber(nextCheckNumber);
				transaction = payTax;
				break;

			case ClientTransaction.TYPE_PAY_BILL:

				transaction = getTransactionById(
						clientTransactionIssuePayment.getPayBill(), company);
				PayBill payBill = (PayBill) transaction;
				payBill.setCheckNumber(nextCheckNumber);
				transaction = payBill;
				break;

			case ClientTransaction.TYPE_VENDOR_PAYMENT:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getPayBill(), company);
				VendorPayment vendorPayment = (VendorPayment) transaction;
				vendorPayment.setCheckNumber(nextCheckNumber);
				transaction = vendorPayment;
				break;

			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCreditCardCharge(),
						company);
				CreditCardCharge creditCardCharge = (CreditCardCharge) transaction;
				creditCardCharge.setCheckNumber(nextCheckNumber);
				transaction = creditCardCharge;
				break;

			case ClientTransaction.TYPE_RECEIVE_TAX:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getReceiveVAT(), company);
				ReceiveVAT receiveVat = (ReceiveVAT) transaction;
				receiveVat.setCheckNumber(nextCheckNumber);
				transaction = receiveVat;
				break;

			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				transaction = getTransactionById(
						clientTransactionIssuePayment.getCustomerPrepayment(),
						company);
				CustomerPrePayment customerPrePayment = (CustomerPrePayment) transaction;
				customerPrePayment.setCheckNumber(nextCheckNumber);
				transaction = customerPrePayment;
				break;

			}
			Session session = HibernateUtil.getCurrentSession();

			org.hibernate.Transaction beginTransaction = session
					.beginTransaction();

			transaction.setStatus(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED);
			session.save(transaction);

			Query query = session.getNamedQuery("getAccount.by.id")
					.setLong("id", accountId).setEntity("company", company);
			Account account = (Account) query.uniqueResult();
			account.setLastCheckNum(nextCheckNumber);
			session.save(account);

			beginTransaction.commit();
		}

	}

	public Transaction getTransactionById(long transactionId, Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getTransaction.by.id")
				.setParameter("transactionId", transactionId)
				.setEntity("company", company);
		Transaction trans = (Transaction) query.uniqueResult();
		return trans;
	}

	List<ClientAdvertisement> getAdvertisements() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			Query query = session.getNamedQuery("getAdvertisements");
			List<Advertisement> list = query.list();
			List<ClientAdvertisement> adds = new ArrayList<ClientAdvertisement>();
			for (Advertisement advertisement : list) {
				ClientAdvertisement clientAdvertisement = new ClientConvertUtil()
						.toClientObject(advertisement,
								ClientAdvertisement.class);
				adds.add(clientAdvertisement);
			}
			return adds;
		} catch (Exception e) {
		}
		return null;

	}

	public List<ClientTDSTransactionItem> getTDSTransactionItemsList(
			int formType, long companyId) {

		ArrayList<ClientTDSTransactionItem> arrayList = new ArrayList<ClientTDSTransactionItem>();

		Session session = HibernateUtil.getCurrentSession();
		Query query;
		if (formType != TDSChalanDetail.Form27EQ) {
			query = session.getNamedQuery("getTDSPayBillTransactionsList")
					.setParameter("companyId", companyId);
		} else {
			query = session.getNamedQuery(
					"getTDSReceivePaymentTransactionsList").setParameter(
					"companyId", companyId);
		}
		List list = query.list();
		Iterator iterator = list.iterator();

		// ArrayList<ClientTDSChalanDetail> chalanList = new
		// ArrayList<ClientTDSChalanDetail>();
		// ArrayList<TDSChalanDetail> chalansGot = new
		// ArrayList<TDSChalanDetail>(
		// session.getNamedQuery("list.TdsChalanDetails")
		// .setEntity("company", getCompany(companyId)).list());
		//
		// for (TDSChalanDetail chalan : chalansGot) {
		// ClientTDSChalanDetail clientObject = null;
		// try {
		// clientObject = new ClientConvertUtil().toClientObject(chalan,
		// ClientTDSChalanDetail.class);
		// } catch (AccounterException e) {
		// e.printStackTrace();
		// }
		// chalanList.add(clientObject);
		// }

		while (iterator.hasNext()) {

			Object[] next = (Object[]) iterator.next();

			Long payeeId = (Long) next[0];
			Double tdsTotal = (Double) next[1];
			Double total = (Double) next[2];
			Long date = (Long) next[3];
			Long trID = (Long) next[4];

			ClientTDSTransactionItem clientTDSTransactionItem = new ClientTDSTransactionItem();
			clientTDSTransactionItem.setVendor(payeeId);
			clientTDSTransactionItem.setTdsAmount(tdsTotal);
			clientTDSTransactionItem.setTotalAmount(total);
			clientTDSTransactionItem.setTransactionDate(date);
			clientTDSTransactionItem.setTransaction(trID);
			clientTDSTransactionItem.setSurchargeAmount(0);
			clientTDSTransactionItem.setEduCess(0);
			clientTDSTransactionItem.setTotalTax(tdsTotal);

			// boolean present = false;
			// if (chalanList.size() > 0) {
			//
			// for (ClientTDSChalanDetail chalan : chalanList) {
			// if (present == true) {
			// break;
			// }
			// for (ClientTDSTransactionItem item : chalan
			// .getTransactionItems()) {
			// if (clientTDSTransactionItem.getTransaction() != item
			// .getTransaction()) {
			// present = false;
			// } else {
			// present = true;
			// break;
			// }
			//
			// }
			// }
			// }
			//
			// if (present == false) {
			arrayList.add(clientTDSTransactionItem);
			// }

		}
		return arrayList;
	}

	public PaginationList<ClientTDSChalanDetail> getTDSChalanDetailsList(
			Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		PaginationList<ClientTDSChalanDetail> chalanList = new PaginationList<ClientTDSChalanDetail>();

		try {

			ArrayList<TDSChalanDetail> chalansGot = new ArrayList<TDSChalanDetail>(
					session.getNamedQuery("list.TdsChalanDetails")
							.setEntity("company", getCompany(companyId)).list());

			for (TDSChalanDetail chalan : chalansGot) {
				ClientTDSChalanDetail clientObject = new ClientConvertUtil()
						.toClientObject(chalan, ClientTDSChalanDetail.class);

				chalanList.add(clientObject);
			}

			return chalanList;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));

		}
	}

	public ArrayList<ClientTDSChalanDetail> getTDSChallansForAckNo(
			String ackNo, long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<ClientTDSChalanDetail> result = new ArrayList<ClientTDSChalanDetail>();

		List<TDSChalanDetail> challansGot = session
				.getNamedQuery("getTDSChallansForAckNo")
				.setString("ackNo", ackNo).setLong("companyId", companyId)
				.list();

		for (TDSChalanDetail chalan : challansGot) {
			ClientTDSChalanDetail clientObject = new ClientConvertUtil()
					.toClientObject(chalan, ClientTDSChalanDetail.class);

			result.add(clientObject);
		}

		return result;
	}

	public ClientTDSDeductorMasters getTDSDeductorMasterDetails(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		try {

			TDSDeductorMasters deductor = (TDSDeductorMasters) session
					.getNamedQuery("getTdsDeductor")
					.setLong("companyId", companyId).uniqueResult();

			ClientTDSDeductorMasters clientObject = null;
			if (deductor != null) {
				clientObject = new ClientConvertUtil().toClientObject(deductor,
						ClientTDSDeductorMasters.class);

			}
			return clientObject;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public List<ClientETDSFillingItem> getEtdsList(int formNo, int quater,
			int startYear, int endYear, Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List<ClientETDSFillingItem> etdsList = new ArrayList<ClientETDSFillingItem>();

		boolean isForm27EQ = (formNo == TDSChalanDetail.Form27EQ);
		try {

			Query query = session.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();
			for (TDSChalanDetail chalan : chalansGot) {
				int i = 1;

				for (TDSTransactionItem item : chalan.getTdsTransactionItems()) {
					ClientETDSFillingItem eTDSObj = new ClientETDSFillingItem();
					eTDSObj.setSerialNo(i);

					double total = 0;
					total = chalan.getIncomeTaxAmount()
							+ chalan.getSurchangePaidAmount()
							+ chalan.getEducationCessAmount()
							+ chalan.getInterestPaidAmount()
							+ chalan.getOtherAmount()
							+ chalan.getPenaltyPaidAmount();

					eTDSObj.setBankBSRCode(chalan.getBankBsrCode());
					eTDSObj.setChalanSerialNumber(chalan
							.getChalanSerialNumber());
					eTDSObj.setSectionForPayment(chalan.getPaymentSection());
					eTDSObj.setTotalTDSfordeductees(total);
					eTDSObj.setDateTaxDeposited(chalan.getDateTaxPaid());
					eTDSObj.setDeducteeID(item.getVendor().getID());
					eTDSObj.setPanOfDeductee(item.getVendor().getTaxId());
					eTDSObj.setDateOFpayment(item.getTransactionDate()
							.getDate());
					eTDSObj.setAmountPaid(item.getTotalAmount());
					eTDSObj.setTds(item.getTdsAmount());
					eTDSObj.setSurcharge(item.getSurchargeAmount());
					eTDSObj.setEducationCess(item.getEduCess());
					eTDSObj.setTotalTaxDEducted(item.getTotalTax());
					eTDSObj.setTotalTaxDeposited(item.getTotalTax());
					eTDSObj.setDateofDeduction(item.getTransactionDate()
							.getDate());
					Transaction transaction = item.getTransaction();
					if (transaction instanceof PayBill) {
						eTDSObj.setTaxRate(((PayBill) transaction)
								.getTdsTaxItem().getTaxRate());
					}
					if (chalan.isBookEntry())
						eTDSObj.setBookEntry("YES");
					else
						eTDSObj.setBookEntry("NO");

					if (!chalan.isVoid()) {
						etdsList.add(eTDSObj);
					}
					i++;
				}
			}
			return etdsList;
		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean updateAckNoForChallans(int formNo, int quater,
			int startYear, int endYear, String ackNo, long date, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();

			for (TDSChalanDetail challan : chalansGot) {
				challan.setEtdsfillingAcknowledgementNo(ackNo);
				challan.setAcknowledgementDate(new FinanceDate(date));
				challan.setFiled(true);

				session.saveOrUpdate(challan);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return false;
		}
		return true;
	}

	public List<ClientTDSChalanDetail> getChalanList(int formNo, int quater,
			int startYear, int endYear, Long companyId) throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		ArrayList<ClientTDSChalanDetail> chalanList = new ArrayList<ClientTDSChalanDetail>();
		try {

			Query query = session.getNamedQuery("getTdsChalanDetails")
					.setEntity("company", getCompany(companyId))
					.setParameter("formNum", formNo)
					.setParameter("quarter", quater)
					.setParameter("startYear", startYear + 1)
					.setParameter("endYear", endYear + 1);

			ArrayList<TDSChalanDetail> chalansGot = (ArrayList<TDSChalanDetail>) query
					.list();
			int i = 1;
			for (TDSChalanDetail chalan : chalansGot) {

				ClientTDSChalanDetail clientObject = new ClientConvertUtil()
						.toClientObject(chalan, ClientTDSChalanDetail.class);
				chalanList.add(clientObject);
			}
			return chalanList;
		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ClientTDSResponsiblePerson getResponsiblePersonDetails(long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		try {

			TDSResponsiblePerson person = (TDSResponsiblePerson) session
					.getNamedQuery("getTdsResposiblePersonDetails")
					.setLong("companyId", companyId).uniqueResult();

			ClientTDSResponsiblePerson clientObject = null;

			if (person != null) {
				clientObject = new ClientConvertUtil().toClientObject(person,
						ClientTDSResponsiblePerson.class);
			}
			return clientObject;

		} catch (Exception e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean savePortletConfiguration(
			ClientPortletConfiguration configuration) {
		if (configuration == null) {
			return false;
		}
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PortletConfiguration serverObj = new PortletConfiguration();
			serverObj = new ServerConvertUtil().toServerObject(serverObj,
					(IAccounterCore) configuration, session);
			session.saveOrUpdate(serverObj);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		} finally {
		}
	}

	public String getIRASFileInformationByDate(Long companyId, long startDate,
			long endDate, boolean isXml) {
		Session session = HibernateUtil.getCurrentSession();

		IRASInformation gstInformation = new IRASInformation();
		Company company = getCompany(companyId);

		IRASCompanyInfo companyInfo = new IRASCompanyInfo();
		companyInfo.setCompanyName(company.getTradingName());
		Map<String, String> companyFields = company.getPreferences()
				.getCompanyFields();
		String companyUEN = companyFields.get("CompanyUEN");
		String gstNo = companyFields.get("GST No");
		companyInfo.setCompanyUEN(companyUEN);
		companyInfo.setGSTNo(gstNo);
		companyInfo.setPeriodStart(new FinanceDate(startDate));

		companyInfo.setPeriodEnd(new FinanceDate(endDate));
		companyInfo.setIAFCreationDate(new FinanceDate());
		companyInfo.setProductVersion("Accounter");
		companyInfo.setIAFVersion("IAFv1.0.0");

		gstInformation.setCompanyInfo(companyInfo);

		List<IRASPurchaseLineInfo> purchaseLineInfo = new ArrayList<IRASPurchaseLineInfo>();

		Query query = session.getNamedQuery("getPurchaseLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		List list = query.list();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			IRASPurchaseLineInfo gstPurchaseLineInfo = new IRASPurchaseLineInfo();
			Object[] next = (Object[]) iterator.next();

			String vendorName = (String) next[0];

			Query namedQuery = session
					.getNamedQuery("getVendor.by.name")
					.setParameter("name", vendorName,
							EncryptedStringType.INSTANCE)
					.setEntity("company", company);
			Vendor vendor = (Vendor) namedQuery.uniqueResult();
			String vendorUEN = vendor.getPayeeFields().get("CompanyUEN");
			gstPurchaseLineInfo.setSupplierName(vendorName);
			gstPurchaseLineInfo.setSupplierUEN(vendorUEN);
			gstPurchaseLineInfo.setInvoiceDate(new FinanceDate((Long) next[1]));
			gstPurchaseLineInfo.setInvoiceNo((String) next[2]);
			gstPurchaseLineInfo.setPermitNo("");
			gstPurchaseLineInfo.setProductDescription((String) next[3]);

			double price = (Double) next[4];

			gstPurchaseLineInfo.setTaxCode((String) next[5]);

			double taxAmount = (Double) next[6];

			String currencyName = (String) next[7];
			double currencyFactor = (Double) next[8];

			gstPurchaseLineInfo.setPurchaseValueSGD(price * currencyFactor);
			gstPurchaseLineInfo.setGSTValueSGD(taxAmount * currencyFactor);
			gstPurchaseLineInfo.setLineNo((Long) next[9] + 1);

			if (currencyName.equals("SGD")) {
				gstPurchaseLineInfo.setFCYCode("XXX");
			} else {
				gstPurchaseLineInfo.setFCYCode(currencyName);
				gstPurchaseLineInfo.setPurchaseFCY(price);
				gstPurchaseLineInfo.setGSTFCY(taxAmount);
			}

			purchaseLineInfo.add(gstPurchaseLineInfo);

		}
		gstInformation.setPurchaseLines(purchaseLineInfo);

		List<IRASSupplyLineInfo> supplyLineInfo = new ArrayList<IRASSupplyLineInfo>();

		Query supplyQuery = session.getNamedQuery("getSupplyLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		Iterator iterator1 = supplyQuery.list().iterator();

		while (iterator1.hasNext()) {
			IRASSupplyLineInfo gstSupplyLineInfo = new IRASSupplyLineInfo();
			Object[] next = (Object[]) iterator1.next();

			String customerName = (String) next[0];

			Query namedQuery = session
					.getNamedQuery("getCustomer.by.name")
					.setParameter("name", customerName,
							EncryptedStringType.INSTANCE)
					.setEntity("company", company);
			Customer customer = (Customer) namedQuery.uniqueResult();
			String customerUEN = customer.getPayeeFields().get("CustomerUEN");

			gstSupplyLineInfo.setCustomerName(customerName);
			gstSupplyLineInfo.setCustomerUEN(customerUEN);
			gstSupplyLineInfo.setInvoiceDate(new FinanceDate((Long) next[1]));
			gstSupplyLineInfo.setInvoiceNo((String) next[2]);
			gstSupplyLineInfo.setProductDescription((String) next[3]);

			double price = (Double) next[4];

			gstSupplyLineInfo.setTaxCode((String) next[5]);

			double taxAmount = (Double) next[6];

			String currencyName = (String) next[7];
			double currencyFactor = (Double) next[8];

			gstSupplyLineInfo.setSupplyValueSGD(price * currencyFactor);
			gstSupplyLineInfo.setGSTValueSGD(taxAmount * currencyFactor);

			gstSupplyLineInfo.setCountry((String) next[9]);
			gstSupplyLineInfo.setLineNo((Long) next[10] + 1);

			if (currencyName.equals("SGD")) {
				gstSupplyLineInfo.setFCYCode("XXX");
			} else {
				gstSupplyLineInfo.setFCYCode(currencyName);
				gstSupplyLineInfo.setSupplyFCY(price);
				gstSupplyLineInfo.setGSTFCY(taxAmount);
			}

			supplyLineInfo.add(gstSupplyLineInfo);

		}
		gstInformation.setSupplyLines(supplyLineInfo);

		List<IRASGeneralLedgerLineInfo> glLineInfo = new ArrayList<IRASGeneralLedgerLineInfo>();

		Query glQuery = session.getNamedQuery("getGLLineItems")
				.setLong("startDate", startDate).setLong("endDate", endDate)
				.setParameter("companyId", companyId);
		Iterator iterator2 = glQuery.list().iterator();

		while (iterator2.hasNext()) {
			IRASGeneralLedgerLineInfo gstGLLineInfo = new IRASGeneralLedgerLineInfo();
			Object[] next = (Object[]) iterator2.next();

			gstGLLineInfo.setTransactionDate(new FinanceDate((Long) next[0]));
			gstGLLineInfo.setAccountID((String) next[1]);
			gstGLLineInfo.setAccountName((String) next[2]);
			gstGLLineInfo.setTransactionDescription((String) next[3]);
			gstGLLineInfo.setName((String) next[4]);
			long transactionId = (Long) next[5];
			gstGLLineInfo.setTransactionID(String.valueOf(transactionId));

			gstGLLineInfo.setCredit((Double) next[6]);
			gstGLLineInfo.setDebit((Double) next[7]);
			gstGLLineInfo.setBalance((Double) next[8]);

			Query namedQuery = session.getNamedQuery("getTransaction.by.id")
					.setLong("transactionId", transactionId)
					.setEntity("company", company);
			Transaction transaction = (Transaction) namedQuery.uniqueResult();

			if (transaction != null) {
				String documentId = getSourceDocumentId(transaction);
				String sourceType = getSourceType(transaction);

				gstGLLineInfo.setSourceDocumentID(documentId);
				gstGLLineInfo.setSourceType(sourceType);
			}

			glLineInfo.add(gstGLLineInfo);

		}
		gstInformation.setGeneralLedgerLines(glLineInfo);

		if (isXml) {
			return createXmlFile(gstInformation);
		} else {
			return createTxtFile(gstInformation);
		}

	}

	protected String createTxtFile(IRASInformation result) {
		FileOutputStream stream;
		try {
			File file = new File(ServerConfiguration.getTmpDir(),
					SecureUtils.createID() + ".txt");
			stream = new FileOutputStream(file);

			DataOutputStream outputStream = new DataOutputStream(stream);

			result.toTxt(outputStream);

			return file.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String createXmlFile(IRASInformation result) {
		FileOutputStream stream;
		try {
			File file = new File(ServerConfiguration.getTmpDir(),
					SecureUtils.createID() + ".xml");

			stream = new FileOutputStream(file);

			result.toXML(stream);

			return file.getName();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	String PURCHASES = "Purchases";
	String CASH_RECEIPT = "Cash Receipt";
	String AR = "AR";
	String AP = "AP";
	String INVENTORY = "Inventory";
	String SALES = "Sales";
	String CASH_DISBURSEMENT = "Cash Disbursement";
	String GENERAL_JOURNAL = "General Journal";

	private String getSourceType(Transaction transaction) {
		switch (transaction.getType()) {
		case Transaction.TYPE_CASH_PURCHASE:
		case Transaction.TYPE_CASH_EXPENSE:
		case Transaction.TYPE_PAY_BILL:
			return PURCHASES;

		case Transaction.TYPE_CASH_SALES:
			return CASH_RECEIPT;

		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
		case Transaction.TYPE_CUSTOMER_REFUNDS:
		case Transaction.TYPE_INVOICE:
		case Transaction.TYPE_RECEIVE_TAX:
			return AR;

		case Transaction.TYPE_ENTER_BILL:
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
		case Transaction.TYPE_EXPENSE:
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
		case Transaction.TYPE_PAY_TAX:
			return AP;

		case Transaction.TYPE_JOURNAL_ENTRY:
		case Transaction.TYPE_TRANSFER_FUND:
		case Transaction.TYPE_ADJUST_SALES_TAX:
		case Transaction.TYPE_TAX_RETURN:
		case Transaction.TYPE_WRITE_CHECK:
			return GENERAL_JOURNAL;

		case Transaction.TYPE_RECEIVE_PAYMENT:
			return SALES;

		default:
			return null;
		}
	}

	private String getSourceDocumentId(Transaction transaction) {

		switch (transaction.getType()) {
		case Transaction.TYPE_CASH_PURCHASE:
		case Transaction.TYPE_CASH_SALES:
		case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case Transaction.TYPE_ENTER_BILL:
		case Transaction.TYPE_JOURNAL_ENTRY:
		case Transaction.TYPE_TRANSFER_FUND:
		case Transaction.TYPE_PAY_BILL:
		case Transaction.TYPE_VENDOR_CREDIT_MEMO:
		case Transaction.TYPE_ADJUST_SALES_TAX:
		case Transaction.TYPE_TAX_RETURN:
		case Transaction.TYPE_RECEIVE_TAX:
		case Transaction.TYPE_CREDIT_CARD_CHARGE:
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
		case Transaction.TYPE_CASH_EXPENSE:
		case Transaction.TYPE_PAY_TAX:
		case Transaction.TYPE_INVOICE:
			return transaction.getNumber();

		case Transaction.TYPE_CUSTOMER_PRE_PAYMENT:
			if (transaction.getPaymentMethod().equals("Check")) {
				CustomerPrePayment customerPrePayment = (CustomerPrePayment) transaction;
				return customerPrePayment.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_CUSTOMER_REFUNDS:
			if (transaction.getPaymentMethod().equals("Check")) {
				CustomerRefund customerRefund = (CustomerRefund) transaction;
				return customerRefund.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_RECEIVE_PAYMENT:
			if (transaction.getPaymentMethod().equals("Check")) {
				ReceivePayment receivePayment = (ReceivePayment) transaction;
				return receivePayment.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

		case Transaction.TYPE_WRITE_CHECK:
			if (transaction.getPaymentMethod().equals("Check")) {
				WriteCheck writeCheck = (WriteCheck) transaction;
				return writeCheck.getCheckNumber();
			} else {
				return transaction.getNumber();
			}

			// case Transaction.TYPE_PAY_EXPENSE:
			// if (transaction.getPaymentMethod().equals("Check")) {
			// PayExpense payExpense = (PayExpense) transaction;
			// return payExpense.getCheckNumber();
			// } else {
			// return transaction.getNumber();
			// }

		}
		return "";
	}

	public ArrayList<TDSChalanDetail> getChalanList(FinanceDate startDate,
			FinanceDate endDate, String acknowledgementNo, long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		try {

			Query query = session
					.getNamedQuery("getTdsChalanDetailsByAcknowledgementNo")
					.setEntity("company", getCompany(companyId))
					.setParameter("startDate", startDate.getDate())
					.setParameter("endDate", endDate.getDate())
					.setParameter("acknowledgementNo", acknowledgementNo);

			return (ArrayList<TDSChalanDetail>) query.list();
		} catch (Exception e) {
			try {
				throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
			} catch (DAOException e1) {
				e1.printStackTrace();
			}
		}
		return new ArrayList<TDSChalanDetail>();
	}

	/**
	 * If accountId = 0, returns all bank statements if accountId != 0 , returns
	 * bank statements of that particular accountId
	 * 
	 * @param companyId
	 * @return
	 */
	public PaginationList<ClientStatement> getBankStatements(long accountId,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();

		PaginationList<ClientStatement> statements = new PaginationList<ClientStatement>();
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Statement> listStatements = new ArrayList<Statement>(
					session.getNamedQuery("list.bankStatement")
							.setParameter("companyId", companyId).list());
			for (Statement statement : listStatements) {
				ClientStatement clientObject = new ClientConvertUtil()
						.toClientObject(statement, ClientStatement.class);

				if (accountId == 0) {// for displaying in BankStatementsView,
										// add all statement objects
					statements.add(clientObject);
				} else if (accountId != 0) {
					if (accountId == clientObject.getAccount()) {// for a
																	// particular
						// accountId in
						// statementObject
						statements.add(clientObject);
					}
				}

			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return statements;

	}

	public void importData(long companyId, String userEmail, String filePath,
			int importerType, Map<String, String> importMap)
			throws AccounterException {
		try {
			Importer<? extends IAccounterCore> importer = getImporterByType(
					importerType, importMap);

			String[] headers = null;
			boolean isHeader = true;
			File file = new File(filePath);
			DataInputStream in = new DataInputStream(new FileInputStream(
					file.getAbsolutePath()));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			OperationContext context = new OperationContext(companyId,
					(IAccounterCore) null, userEmail);
			while ((strLine = br.readLine()) != null) {
				Map<String, String> columnNameValueMap = new HashMap<String, String>();
				String[] values = strLine.split(",");
				if (isHeader) {
					headers = values;
					isHeader = false;
				} else {
					if (values.length == headers.length) {
						for (int i = 0; i < values.length; i++) {
							String value = values[i].trim()
									.replaceAll("\"", "");
							columnNameValueMap.put(headers[i], value);
						}
						importer.loadData(columnNameValueMap);
						IAccounterCore data = importer.getData();
						context.setData(data);
						create(context);
					}
				}
			}
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(e.getMessage());
		}
	}

	private Importer<? extends IAccounterCore> getImporterByType(
			int importerType, Map<String, String> importMap) {
		Importer<? extends IAccounterCore> importerByType = getImporterByType(importerType);
		importerByType.updateFields(importMap);
		return importerByType;
	}

	private Importer<? extends IAccounterCore> getImporterByType(
			int importerType) {
		switch (importerType) {
		case ImporterType.INVOICE:
			return new InvoiceImporter();
		case ImporterType.CUSTOMER:
			return new CustomerImporter();
		case ImporterType.VENDOR:
			return new VendorImporter();
		case ImporterType.ITEM:
			return new ItemImporter();
		}
		return null;
	}

	public List<ImportField> getFieldsOfImporter(int importerType) {
		Importer<? extends IAccounterCore> importerByType = getImporterByType(importerType);
		return importerByType.getFields();
	}

}
