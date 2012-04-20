package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientAttendanceManagement implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ClientAttendanceManagementItem> items;

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<ClientAttendanceManagementItem> getItems() {
		return items;
	}

	public void setItems(List<ClientAttendanceManagementItem> items) {
		this.items = items;
	}
}
