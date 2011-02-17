package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientShippingMethod implements IAccounterCore {

	String stringID;

	String name;
	String description;

	boolean isDefault;

	public ClientShippingMethod() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDisplayName() {

		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return AccounterCoreType.SHIPPING_METHOD;
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

		return "ClientShippingMethod";
	}

}
