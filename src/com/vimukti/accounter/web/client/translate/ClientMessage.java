package com.vimukti.accounter.web.client.translate;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientMessage implements Serializable {

	public static int UNTRANSLATED = 1;
	public static int ALL = 2;
	public static int MYTRANSLATIONS = 3;
	public static int UNCONFIRMED = 4;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int id;

	String key;

	String value;

	ArrayList<ClientLocalMessage> localMessages;

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

	public ArrayList<ClientLocalMessage> getLocalMessages() {
		return localMessages;
	}

	public void setLocalMessages(ArrayList<ClientLocalMessage> localMessages) {
		this.localMessages = localMessages;
	}
}
