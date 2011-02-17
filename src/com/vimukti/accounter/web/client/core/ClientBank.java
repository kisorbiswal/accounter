package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientBank implements IAccounterCore {

	String stringID;

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
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientBank";
	}

}
