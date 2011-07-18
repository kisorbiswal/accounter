package com.vimukti.accounter.web.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientMember implements IsSerializable, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064481956017534416L;
	public String userID;
	public String role;
	public String spaceID;
	public String emailID;
	public String companyDisplayName;
	public String fullname;
	public String companyName;

	public String getUserID() {
		return userID;
	}

	public void setUserID(final String userID) {
		this.userID = userID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

}
