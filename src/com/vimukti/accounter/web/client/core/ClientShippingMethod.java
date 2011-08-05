package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientShippingMethod implements IAccounterCore {

	long id;

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
		return AccounterCoreType.SHIPPING_METHOD;
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

		return "ClientShippingMethod";
	}

	public ClientShippingMethod clone() {
		return null;

	}
}
