package com.vimukti.accounter.utils;

public abstract class ConnectionProvider implements
		org.hibernate.connection.ConnectionProvider {

	private static ThreadLocal<String> dbName = new ThreadLocal<String>();

	public static void setDBName(String name) {
		dbName.set(name);
	}

	public static String getDBName() {
		String str = dbName.get();
		return str;
	}

}