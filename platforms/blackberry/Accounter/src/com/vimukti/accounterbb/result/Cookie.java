package com.vimukti.accounterbb.result;

import net.rim.device.api.util.Persistable;

public class Cookie implements Persistable {

	private String cookie;

	public Cookie() {
		// Locale locale = Locale.getDefault();
		// String language = locale.getLanguage();
		this.cookie = "hi";
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public String toString() {
		return cookie;
	}

}
