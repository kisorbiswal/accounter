package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientBank implements IAccounterCore {

	long id;

	String name;

	int version;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * null;
	 * 
	 * @return the id
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {

		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.BANK;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientBank";
	}

	public Object clone() {
		return null;

	}

}
