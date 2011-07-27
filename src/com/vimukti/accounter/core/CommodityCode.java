package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class CommodityCode implements IAccounterServerCore {

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
			throws InvalidOperationException {
		return true;
	}
}
