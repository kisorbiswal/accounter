package com.vimukti.accounter.web.client.core;

import java.util.Set;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.IAccounterCore;

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
