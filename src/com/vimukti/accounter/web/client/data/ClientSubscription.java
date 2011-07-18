package com.vimukti.accounter.web.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientSubscription implements Serializable, IsSerializable,
		Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long allocatedSize;
	public long maximumSize;
	public int existedUsers;
	public int existingLiteUsers;
	public int maximumUsers;
	public int maximumLiteUsers;

	public ClientSubscription() {
		super();
		// TODO Auto-generated constructor stub
	}

}
