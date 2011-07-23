package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class CommodityCode implements IAccounterServerCore {

	long id;
	/**
	 * commodity code name.
	 */
	String name;

	public long id;

	transient boolean isImported;

	public long getId() {
		return id;
	}

	public void setID(long id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

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
