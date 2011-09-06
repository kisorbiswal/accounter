package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccounterClass implements IAccounterServerCore {

	private String className;

	private long id;

	private int version;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getclassName() {
		return className;
	}

	public void setclassName(String trackingClassName) {
		this.className = trackingClassName;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}
}
