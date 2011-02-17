package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class CommodityCode implements IAccounterServerCore {

	long id;
	/**
	 * commodity code name.
	 */
	String name;

	public String stringID;

	transient boolean isImported;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}
}
