package com.vimukti.accounter.core;

import java.util.Iterator;
import java.util.Set;

/**
 * This Client Saved in ServerDatabase
 * 
 * @author nagaraju.p
 * 
 */
public class Client {

	private long id;
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private boolean isActive;
	private Set<ServerCompany> companies;
	private String phoneNumber;
	private String country;
	private boolean isSubscribedToNewsLetters;
	private boolean isRequirePasswordReset = false;
	private int loginCount;
	private long lastLoginTime;

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

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
		this.setPhoneNumber(phoneNumber);

	}

	public void setCountry(String country) {
		this.country = country;

	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCountry() {
		return country;
	}

	public boolean isSubscribedToNewsLetters() {
		return isSubscribedToNewsLetters;
	}

	public void setSubscribedToNewsLetters(boolean isSubscribedToNewsLetters) {
		this.isSubscribedToNewsLetters = isSubscribedToNewsLetters;
	}

	public boolean isRequirePasswordReset() {
		return isRequirePasswordReset;
	}

	public void setRequirePasswordReset(boolean isRequirePasswordReset) {
		this.isRequirePasswordReset = isRequirePasswordReset;
	}

	/**
	 * Converts Client Object to User Object
	 */
	public User toUser() {
		User user = new User();
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setEmail(this.emailId);
		return user;
	}

	/**
	 * Returns ServerCompany From ID
	 * 
	 * @param serverCompanyID
	 * @return
	 */
	public ServerCompany getCompany(long serverCompanyID) {
		Iterator<ServerCompany> iterator = companies.iterator();
		while (iterator.hasNext()) {
			ServerCompany next = iterator.next();
			if (next.getID() == serverCompanyID) {
				return next;
			}
		}
		return null;
	}

	public long getID() {
		return id;
	}

}
