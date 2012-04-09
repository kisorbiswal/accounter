package com.vimukti.accounter.web.client.core;

public class ClientEmployeeCategory implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_CATEGORY;
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

}
