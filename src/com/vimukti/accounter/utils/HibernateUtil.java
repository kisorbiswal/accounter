package com.vimukti.accounter.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {

	static ThreadLocal<Session> threadLocalSession = new ThreadLocal<Session>();

	private static SessionFactory sessionFactory;

	public static Session openSession() {
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
		return config.buildSessionFactory();
	}

	public static Session getCurrentSession() {
		return threadLocalSession.get();
	}

}
