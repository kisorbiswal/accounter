package com.vimukti.accounter.core;

import java.util.Set;

/**
 * This Client Saved in ServerDatabase
 * 
 * @author nagaraju.p
 * 
 */
public class Client {
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private boolean isActive;
	private Set<ServerCompany> companies;
	private String phoneNumber;
	private String country;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<ServerCompany> getCompanies() {
		return companies;
	}

	public void setCompanies(Set<ServerCompany> companies) {
		this.companies = companies;
	}

	public void setPhoneNo(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		
	}

	public void setCountry(String country) {
		this.country = country;
		
	}
}
