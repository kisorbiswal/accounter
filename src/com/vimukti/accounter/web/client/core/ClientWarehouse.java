package com.vimukti.accounter.web.client.core;

import java.util.Set;

public class ClientWarehouse implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 32420869218969185L;
	private String stringID;
	private ClientAddress address;
	private Set<ClientItemStatus> itemStatuses;

	private String name;
	private ClientContact contact;

	private boolean isDefaultWarehouse;
	private String DDINumber;
	private String mobileNumber;
	private String contactNumber;
	private String phoneNumber;

	public boolean isDefaultWarehouse() {
		return isDefaultWarehouse;
	}

	public void setDefaultWarehouse(boolean isDefaultWarehouse) {
		this.isDefaultWarehouse = isDefaultWarehouse;
	}

	public String getDDINumber() {
		return DDINumber;
	}

	public void setDDINumber(String dDINumber) {
		DDINumber = dDINumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ClientWarehouse() {
	}

	public ClientAddress getAddress() {
		return address;
	}

	public Set<ClientItemStatus> getItemStatuses() {
		return itemStatuses;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public void setItemStatuses(Set<ClientItemStatus> itemStatuses) {
		this.itemStatuses = itemStatuses;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	public ClientContact getContact() {
		return contact;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.WAREHOUSE;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientWarehouse";
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

}
