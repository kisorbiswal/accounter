package com.vimukti.accounter.web.server.i18n;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ServerSideMessages {

	public static final boolean TRACK_MESSAGE_STATISTICS = Boolean.TRUE;

	private static Map<String, String> keyMessages = new HashMap<String, String>();

	public static List<String> usageByOrder = new ArrayList<String>();

	public static Map<String, Integer> usageByCount = new HashMap<String, Integer>();

	static class Handler implements InvocationHandler {
		@SuppressWarnings("unused")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

			String msg = getMessage(method.getName());
			int index = 0;

			for (Class<?> type : method.getParameterTypes()) {
				String value = String.valueOf(args[index++]);
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
	public static String getMessage(String key) {
		if (TRACK_MESSAGE_STATISTICS) {
			updateStats(key);
		}

		String messgae = keyMessages.get(key);
		if (messgae != null) {
			return messgae;
		}
		User user = AccounterThreadLocal.get();
		Locale locale = ServerLocal.get();
		Session session = HibernateUtil.getCurrentSession();
		boolean sessionOpened = false;
		if (session == null || !session.isOpen()) {
			session = HibernateUtil.openSession();
			sessionOpened = true;
		}
		try {
			long clientId = 0;
			if (user != null) {
				clientId = user.getClient().getID();
			}
			String language = "";
			try {
				language = locale.getISO3Language();
			} catch (MissingResourceException e) {
				language = "eng";
			}
			List list = session.getNamedQuery("getLocalMessageForKey")
					.setString("language", language)
					.setLong("client", clientId).setString("key", key).list();
			String msg = null;
			if (!list.isEmpty()) {
				msg = (String) list.get(0);
			}
			if (msg == null) {
				new AccounterException("no value found for '" + key + "'")
						.printStackTrace();
				return "";
			}
			// String replace = msg.replace("'", "\\'");
			keyMessages.put(key, msg);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sessionOpened) {
				session.close();
			}
		}
		return "";
	}

	private static void updateStats(String key) {
		if (!usageByOrder.contains(key)) {
			usageByOrder.add(key);
		}
		if (usageByCount.containsKey(key)) {
			usageByCount.put(key, usageByCount.get(key) + 1);
		} else {
			usageByCount.put(key, 1);
		}
	}

}
