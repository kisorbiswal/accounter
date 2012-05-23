package com.vimukti.accounter.utils;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.EncryptedStringType;
import org.hibernate.proxy.HibernateProxy;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.setup.server.DatabaseManager;

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
		if (ServerConfiguration.isDesktopApp()) {
			try {
				DatabaseManager.getInstance().loadDbConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Configuration config = new Configuration();
		config.getTypeResolver().registerTypeOverride(
				EncryptedStringType.INSTANCE);
		config.configure();
		return config.buildSessionFactory();
	}

	public static Session getCurrentSession() {
		return threadLocalSession.get();
	}

	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException(
					"Entity passed for initialization is null");
		}

		Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity)
					.getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

}
