package com.vimukti.accounter.web.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientToDetails implements Serializable, IsSerializable, Cloneable {

	private String contactId;
	private String emailId;
	private String name;
	private String company;

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
