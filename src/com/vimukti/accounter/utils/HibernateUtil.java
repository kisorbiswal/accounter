package com.vimukti.accounter.utils;

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
	public static Session openSession() {
		ConnectionProvider.setDBName("Accounter");
		Session session = getSessionFactory().openSession();
		threadLocalSession.set(session);
		return session;
	}

	private static SessionFactory getSessionFactory() {
		if (sessionFactory != null) {
			return sessionFactory;
		}
		sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

	private static SessionFactory buildSessionFactory() {
		Configuration config = new Configuration();
		config.configure();
		config.setProperty("hibernate.hbm2ddl.auto", "none");
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
