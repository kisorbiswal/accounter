package com.vimukti.accounter.workspace.tool;

import java.sql.Timestamp;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class OperationContext {
	/** User who is Doing Operation */
	protected User user;

	/** Operation Data */
	protected IAccounterCore data;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the data
	 */
	public IAccounterCore getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(IAccounterCore data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public Timestamp getDate() {
		return null;
	}

}
