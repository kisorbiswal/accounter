package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientVendorGroup implements IAccounterCore {

	int version;
	String stringID;
	String name;
	boolean isDefault;

	public ClientVendorGroup() {

	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the vendorGroupName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param vendorGroupName
	 *            the vendorGroupName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.VENDOR_GROUP;
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

		return "ClientVendorGroup";
	}

}
