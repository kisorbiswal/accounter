package com.vimukti.accounter.web.client.imports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.Global;

public class ImporterType {
	public static final int CUSTOMER = 1;

	public static final int VENDOR = 2;

	public static final int INVOICE = 3;

	private static Map<Integer, String> supportedImports = new HashMap<Integer, String>();

	static {
		supportedImports.put(CUSTOMER, Global.get().Customer());
	}

	public static Map<String, Integer> getAllSupportedImporters() {

		return null;
	}
}
