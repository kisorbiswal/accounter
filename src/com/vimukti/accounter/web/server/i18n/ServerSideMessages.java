package com.vimukti.accounter.web.server.i18n;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServerSideMessages {

	static class Handler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, String lang) throws IOException {
		if (clazz == null) {
			throw new IllegalArgumentException("Class cannot be null.");
		}

		return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class<?>[] { clazz }, new Handler());
	}

}
