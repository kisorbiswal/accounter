package com.vimukti.accounter.main;

import java.util.Locale;

public class ServerLocal {
	private static ThreadLocal<Locale> local = new ThreadLocal<Locale>();

	public static Locale get() {
		return local.get();
	}

	public static void set(Locale locale) {
		local.set(locale);
	}
}
