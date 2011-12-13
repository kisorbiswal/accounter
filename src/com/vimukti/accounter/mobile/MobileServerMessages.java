package com.vimukti.accounter.mobile;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public class MobileServerMessages {
	public static String getMessage(String key) {
		String message = ServerSideMessages.getMessage(key);
		int a = key.indexOf('{');
		int b = key.indexOf('}');
		if (a != -1) {
			String innerKey = key.substring(a + 1, b);
			message = message.replaceFirst("\\{.+?\\}",
					getInnerMessage(innerKey));
		}
		return message;
	}

	private static String getInnerMessage(String innerKey) {
		String global = null;
		if (innerKey.equals("Customer")) {
			global = Global.get().Customer();
		} else if (innerKey.equals("customer")) {
			global = Global.get().customer();
		} else if (innerKey.equals("Vendor")) {
			global = Global.get().Vendor();
		} else if (innerKey.equals("vendor")) {
			global = Global.get().vendor();
		} else if (innerKey.equals("Location")) {
			global = Global.get().Location();
		} else if (innerKey.equals("customers")) {
			global = Global.get().customers();
		} else if (innerKey.equals("vendors")) {
			global = Global.get().vendors();
		} else if (innerKey.equals("Customers")) {
			global = Global.get().Customers();
		} else if (innerKey.equals("Vendors")) {
			global = Global.get().Vendors();
		} else {
			global = getMessage(innerKey);
		}
		return global;
	}
}
