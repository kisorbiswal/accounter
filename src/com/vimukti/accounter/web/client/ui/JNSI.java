package com.vimukti.accounter.web.client.ui;

public class JNSI {
	public static native boolean getIsAdmin(String jsVar)/*-{

		var isAdmin = eval('$wnd.' + jsVar);

		if (isAdmin != null) {
			return isAdmin;
		} else {
			return false;
		}
	}-*/;

}
