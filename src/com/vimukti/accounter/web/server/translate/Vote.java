package com.vimukti.accounter.web.server.translate;

import java.io.Serializable;

import com.vimukti.accounter.core.Client;

public class Vote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int id;

	LocalMessage localMessage;

	boolean isUp;

	Client client;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalMessage getLocalMessage() {
		return localMessage;
	}

	public void setLocalMessage(LocalMessage localMessage) {
		this.localMessage = localMessage;
	}

	public boolean isUp() {
		return isUp;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client cliet) {
		this.client = cliet;
	}
}
