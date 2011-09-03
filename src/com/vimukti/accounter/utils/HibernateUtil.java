package com.vimukti.accounter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {

	private static final Logger LOG = Logger.getLogger(HibernateUtil.class);

	// private static SessionFactory sessionFactory;

	// SessionFactory = new
	// Configuration().configure("/CollaberServer/hibernate.cfg.xml");
	static ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();

	private static SessionFactory sessionFactory;

	// public static SessionFactory getSessionFactory() {
	// return sessionFactory;
	// }
	//
	// public static void shutdown() {
	// getSessionFactory().close();
	// }
	public static Session openSession(String dbName) {
		return openSession(dbName, false);
	}

	public static Session openSession(String dbName, boolean createDB) {
		// LOG.info("Openned Session");
		ConnectionProvider.setDBName(dbName);
		boolean executeSql = false;
		if (createDB && sessionFactory != null) {
			executeSql = true;
		}

		Session session = getSessionFactory(dbName, createDB).openSession();
		if (createDB && executeSql) {
			ScriptRunner runner = new ScriptRunner(session.connection(), true,
					true);
			runner.setLogWriter(null);
			try {
				runner.runScript(new BufferedReader(new FileReader(new File(
						"config/tables.sql"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		threadLocalSession.set(session);
		return session;
	}

	private static SessionFactory getSessionFactory(String dbName,
			boolean createDB) {
		if (createDB) {
			if (sessionFactory != null) {
				return sessionFactory;
				// sessionFactory.close();
			}
			sessionFactory = buildSessionFactory(true);
			return sessionFactory;
		} else {
			if (sessionFactory == null) {
				try {
					sessionFactory = buildSessionFactory(false);
				} catch (Throwable e) {
					e.printStackTrace();
					throw new ExceptionInInitializerError(e);
				}
			}
			return sessionFactory;
		}
	}

	private static SessionFactory buildSessionFactory(boolean createDB) {
		Configuration config = new Configuration();
		config.configure();
		if (createDB) {
			config.setProperty("hibernate.hbm2ddl.auto", "update");
		} else {
			config.setProperty("hibernate.hbm2ddl.auto", "none");
		}
		config.setProperty("hibernate.connection.provider_class",
				getConnectionProvider());
		return config.buildSessionFactory();
	}

	private static String getConnectionProvider() {
		return "com.vimukti.accounter.utils.LiveConnectionProvider";
	}

	public static Session getCurrentSession() {
		return threadLocalSession.get();
	}

	public static void setSession(Session session) {
		threadLocalSession.set(session);
	}

	public static void closeCurrentSession() {
		// LOG.info("Close Session");

		if (getCurrentSession() != null && getCurrentSession().isOpen()) {
			getCurrentSession().close();
			threadLocalSession.remove();
		}
	}

	public static void rebuildSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
		Runtime.getRuntime().gc();
		System.out.println("Memory--------------"
				+ Runtime.getRuntime().totalMemory());
	}

	public static String getCurrentDatabse() {
		return ConnectionProvider.getDBName();
	}

}
