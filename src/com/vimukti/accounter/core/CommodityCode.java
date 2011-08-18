package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class CommodityCode implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long id;
	/**
	 * commodity code name.
	 */
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}
}
