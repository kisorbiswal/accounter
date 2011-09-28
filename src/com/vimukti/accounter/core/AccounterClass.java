package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccounterClass extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	private String className;

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
	public String getName() {
		return className;
	}

	@Override
	public void setName(String name) {
		this.className = name;
	}
}
