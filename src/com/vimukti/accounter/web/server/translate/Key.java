package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;

public class Key implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	String key;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
