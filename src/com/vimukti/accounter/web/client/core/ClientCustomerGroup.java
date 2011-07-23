package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientCustomerGroup implements IAccounterCore {

	long id;

	String name;
	boolean isDefault;

	public ClientCustomerGroup() {

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
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMER_GROUP;
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientCustomerGroup";
	}
}
