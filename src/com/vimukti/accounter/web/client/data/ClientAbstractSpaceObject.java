package com.vimukti.accounter.web.client.data;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class ClientAbstractSpaceObject implements ClientObject,
		Serializable, IsSerializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date createdDate;

	public String creatorID;

	public Date lastModifiedDate;

	public String lastModifierID;

	public boolean isRead;

	public ClientAbstractSpaceObject(String user) {
		this.createdDate = new Date();
		this.creatorID = user;
		this.lastModifiedDate = new Date();
		this.lastModifierID = user;
	}

	public abstract String getObjectID();
}
