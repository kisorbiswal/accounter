package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int id;

	String key;

	String value;

	int version;

	boolean isNotUsed;

	Set<LocalMessage> localMessages;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Set<LocalMessage> getLocalMessages() {
		return localMessages;
	}

	public void setLocalMessages(Set<LocalMessage> localMessages) {
		this.localMessages = localMessages;
	}

	public boolean isNotUsed() {
		return isNotUsed;
	}

	public void setNotUsed(boolean isNotUsed) {
		this.isNotUsed = isNotUsed;
	}
}
