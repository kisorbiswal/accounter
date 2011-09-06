package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class Location implements IAccounterServerCore {

	private static final long serialVersionUID = 1L;
	private String locationName;
	long id;

	@Override
	public long getID() {
		return this.id;
	}

	public void setId(long ID) {
		this.id = ID;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return false;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {
	}

}
