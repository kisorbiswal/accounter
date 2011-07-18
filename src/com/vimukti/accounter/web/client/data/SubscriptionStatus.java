package com.vimukti.accounter.web.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionStatus implements IsSerializable, Serializable,
		Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long usedSize;

	public long totalSize;
	public int totalUsers;
	public int totalLiteUsers;
	public int createdUsers;

	public SubscriptionStatus() {

	}
}
