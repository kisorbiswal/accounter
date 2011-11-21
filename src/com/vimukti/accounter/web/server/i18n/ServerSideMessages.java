package com.vimukti.accounter.web.server.i18n;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;

public class ServerSideMessages {

	static class Handler implements InvocationHandler {
		@SuppressWarnings("unused")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			String msg = getMessage(method.getName());
			int index = 0;

			for (Class<?> type : method.getParameterTypes()) {
				String value=String.valueOf(args[index++]);
				msg = msg.replaceFirst("\\{.+?\\}", value);
			}
			return msg;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) throws IOException {
		if (clazz == null) {
			throw new IllegalArgumentException("Class cannot be null.");
		}

		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class<?>[] { clazz }, new Handler());
	}

	/**
	 * Load the message for the given key
	 * 
	 * @param name
	 * @return
	 */
	private static String getMessage(String key) {
		User user = AccounterThreadLocal.get();
		Locale locale = ServerLocal.get();
		Session session = HibernateUtil.getCurrentSession();
		try {
			String msg = (String) session
					.getNamedQuery("getLocalMessageForKey")
					.setString("language", locale.getISO3Language())
					.setLong("client", user.getClient().getID())
					.setString("key", key).uniqueResult();
			return msg;
		} catch (Exception e) {

		}
		return "";
	}

}
