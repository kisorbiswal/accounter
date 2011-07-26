package com.vimukti.accounter.web.client.ui.core;

import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ItemStatus;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class ClientWarehouse implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 32420869218969185L;
	private String stringID;
	private Address address;
	private Set<ItemStatus> itemStatuses;

	private String name;
	private Contact contact;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	public ClientWarehouse() {
	}

	public Address getAddress() {
		return address;
	}

	public Set<ItemStatus> getItemStatuses() {
		return itemStatuses;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setItemStatuses(Set<ItemStatus> itemStatuses) {
		this.itemStatuses = itemStatuses;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
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
