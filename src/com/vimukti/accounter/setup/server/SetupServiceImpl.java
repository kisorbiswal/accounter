package com.vimukti.accounter.setup.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.License;
import com.vimukti.accounter.core.Property;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.license.LicenseManager;
import com.vimukti.accounter.main.MessageLoader;
import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.setup.client.ISetupService;
import com.vimukti.accounter.setup.client.core.AccountDetails;
import com.vimukti.accounter.setup.client.core.DatabaseConnection;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.RecurringTool;

public class SetupServiceImpl extends RemoteServiceServlet implements
		ISetupService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String testDBConnection(DatabaseConnection dbConnDetails)
			throws Exception {
		return checkDBConnection(dbConnDetails);
	}

	private String checkDBConnection(DatabaseConnection dbConnDetails)
			throws Exception {

		getConnection(dbConnDetails);

		return "Testing of database connection was successful !";

	}

	@Override
	public Boolean saveDBConnection(DatabaseConnection conn) throws Exception {
		Connection connection = getConnection(conn);
		if (connection == null) {
			return false;
		}

		connection = DriverManager.getConnection(DatabaseManager.getInstance()
				.getURLWithoutDB(conn), conn.getUsername(), conn.getPassword());

		try {
			String dropCmd = "DROP DATABASE " + conn.getDbName();
			Statement statement = connection.createStatement();
			statement.execute(dropCmd);

			String createCmd = "CREATE DATABASE " + conn.getDbName();
			statement.execute(createCmd);

			DatabaseManager.getInstance().saveDBConnection(conn);
			completePage(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return true;
	}

	private void completePage(int pageNo) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Property pagePro = (Property) session.get(Property.class,
					Property.SETUP_PAGE);
			if (pagePro == null) {
				pagePro = new Property(Property.SETUP_PAGE, pageNo);
			} else {
				pagePro.setValue(pageNo + "");
			}
			session.saveOrUpdate(pagePro);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
	}

	private Connection getConnection(DatabaseConnection dbConnDetails)
			throws Exception {
		DatabaseManager instance = DatabaseManager.getInstance();
		Connection connection = null;
		try {
			try {
				Class.forName(instance.getDriver(dbConnDetails.getDbType()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new Exception(
						"Connection refused. Check that the hostname and port are correct");
			}

			connection = DriverManager.getConnection(
					instance.getURL(dbConnDetails),
					dbConnDetails.getUsername(), dbConnDetails.getPassword());
			if (connection == null) {
				throw new Exception("Cann't connect to database");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return connection;
	}

	@Override
	public String getServerID() {
		Session session = HibernateUtil.getCurrentSession();
		Property prop = (Property) session.get(Property.class,
				Property.SERVER_ID);
		if (prop != null) {
			return prop.getValue();
		}
		String randomSID = getRandomSID();
		Transaction transaction = session.getTransaction();
		try {
			Property serverIdProp = new Property(Property.SERVER_ID, randomSID);
			session.save(serverIdProp);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
		return randomSID;
	}

	private String getRandomSID() {
		DefaultSIDManager sidManger = new DefaultSIDManager();
		return sidManger.generateSID();
	}

	@Override
	public boolean verifyLicense(String serverID, String licenseString)
			throws Exception {

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			License license = new LicenseManager().doDecode(licenseString);
			String dbServerID = getServerID();
			if (!serverID.equals(license.getServerId())
					|| !serverID.equals(dbServerID) || !license.isValid()) {
				throw new Exception("Invalid License");
			}
			session.save(license);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new Exception(e);
		}
		completePage(2);
		return true;
	}

	@Override
	public boolean saveAccountDetails(AccountDetails accountDetails)
			throws Exception {
		if (!isValidEmail(0, accountDetails.getEmailId())) {
			throw new Exception(Global.get().messages()
					.incorrectEmailOrPassWord());
		}

		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			initServer();

			Property serverIDProp = (Property) session.get(Property.class,
					Property.SERVER_ID);

			if (serverIDProp == null) {
				throw new Exception("License Not yet configured");
			}

			Query query = session.getNamedQuery("getLicenseByServerID")
					.setParameter("serverID", serverIDProp.getValue());
			List<License> list = query.list();
			if (list.isEmpty()) {
				throw new Exception("License Not yet configured");
			}
			License activeLicense = null;
			for (License license : list) {
				if (license.isValid()) {
					activeLicense = license;
					break;
				}
			}
			if (activeLicense == null) {
				throw new Exception("Invalid License.");
			}

			transaction = session.beginTransaction();
			Client client = getClient(accountDetails);
			session.save(client);

			activeLicense.setClient(client);
			session.saveOrUpdate(activeLicense);

			HttpServletRequest request = getThreadLocalRequest();

			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("emailId", client.getEmailId());
			client.setLoginCount(client.getLoginCount() + 1);
			client.setLastLoginTime(System.currentTimeMillis());
			session.saveOrUpdate(client);

			insertFeatures(session);

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			throw new Exception(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		completePage(3);
		return true;
	}

	private void insertFeatures(Session session) {
		Query query = session.getNamedQuery("insertAllFeatures");
		query.executeUpdate();
	}

	private void initServer() throws IOException {
		Encrypter.init();

		FinanceTool.createViews();

		loadAccounterMessages();

		loadSubscriptionFeatures();

		Global.set(new ServerGlobal());

		ScheduledExecutorService recurring = Executors
				.newSingleThreadScheduledExecutor();
		recurring
				.scheduleAtFixedRate(new RecurringTool(), 0, 1, TimeUnit.HOURS);

	}

	private static void loadSubscriptionFeatures() {
		saveSubscription(Subscription.BEFORE_PAID_FETURE);
		saveSubscription(Subscription.FREE_CLIENT);
		saveSubscription(Subscription.PREMIUM_USER);
	}

	private static void saveSubscription(int type) {
		Session session = HibernateUtil.getCurrentSession();
		Subscription instance = Subscription.getInstance(type);
		if (instance == null) {
			Transaction beginTransaction = session.beginTransaction();
			instance = new Subscription();
			instance.setType(type);
			session.save(instance);
			beginTransaction.commit();
		}
	}

	private static void loadAccounterMessages() throws IOException {
		String fileName = AccounterMessages.class.getName();
		fileName = fileName.replace('.', '/');
		fileName = fileName + ".properties";

		String defaultFileName = (AccounterMessages.class.getName().replace(
				'.', '/')) + ".properties";
		ClassLoader classLoader = AccounterMessages.class.getClassLoader();
		InputStream is = null;
		InputStreamReader reader = null;

		String fileName2 = AccounterMessages2.class.getName();
		fileName2 = fileName2.replace('.', '/');
		fileName2 = fileName2 + ".properties";

		String defaultFileName2 = (AccounterMessages2.class.getName().replace(
				'.', '/')) + ".properties";
		ClassLoader classLoader2 = AccounterMessages2.class.getClassLoader();
		InputStream is2 = null;
		InputStreamReader reader2 = null;
		try {
			is2 = classLoader.getResourceAsStream(fileName2);
			if (is2 == null) {
				is2 = classLoader.getResourceAsStream(defaultFileName2);
				if (is2 == null) {
					throw new FileNotFoundException(
							"Could not find any properties files matching the given class AccounterMessages2.");
				}
			}
			new MessageLoader(is2).loadMessages();
			System.out.println("Completed the Inseting of messages 2..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader2 != null) {
				reader2.close();
			}
			if (is2 != null) {
				is2.close();
			}
		}
		try {
			is = classLoader.getResourceAsStream(fileName);
			if (is == null) {
				is = classLoader.getResourceAsStream(defaultFileName);
				if (is == null) {
					throw new FileNotFoundException(
							"Could not find any properties files matching the given class.");
				}
			}
			new MessageLoader(is).loadMessages();
			System.out.println("Completed the Inseting of messages..");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding not found.", e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

	private Client getClient(AccountDetails accountDetails) {
		Client client = new Client();

		client.setFirstName(accountDetails.getFirstName());
		client.setLastName(accountDetails.getLastName());
		client.setFullName(accountDetails.getFirstName() + " "
				+ accountDetails.getLastName());
		client.setActive(true);
		client.setEmailId(accountDetails.getEmailId());
		client.setUsers(new HashSet<User>());
		String passwordWithHash = HexUtil.bytesToHex(Security
				.makeHash(accountDetails.getEmailId()
						+ Client.PASSWORD_HASH_STRING
						+ accountDetails.getPassword()));

		client.setPassword(passwordWithHash);
		client.setPasswordRecoveryKey(EU.encryptPassword(accountDetails
				.getPassword()));

		ClientSubscription cs = new ClientSubscription();
		cs.setSubscription(Subscription.getInstance(Subscription.PREMIUM_USER));
		cs.setPremiumType(ClientSubscription.FIVE_USERS);
		client.setClientSubscription(cs);
		return client;
	}

	private boolean isValidEmail(int inputType, String value) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}
		return value
				.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");
	}

}
