package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String value;

	Set<LocalMessage> localMessages;

	Set<Key> keys;

	boolean isNotUsed;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set<LocalMessage> getLocalMessages() {
		return localMessages;
	}

	public void setLocalMessages(Set<LocalMessage> localMessages) {
		this.localMessages = localMessages;
	}

	public Set<Key> getKeys() {
		return keys;
	}

	public void setKeys(Set<Key> keys) {
		this.keys = keys;
	}

	public boolean isNotUsed() {
		return isNotUsed;
	}

	public void setNotUsed(boolean isNotUsed) {
		this.isNotUsed = isNotUsed;
	}

	@Override
	public String toString() {
		return value;
	}
}
