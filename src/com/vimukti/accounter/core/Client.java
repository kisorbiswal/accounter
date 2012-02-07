package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.server.translate.Language;

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
	private String fullName;
	private String emailId;
	private String password;
	private boolean isActive;
	private Set<User> users;
	private String phoneNumber;
	private String country;
	private boolean isSubscribedToNewsLetters;
	private boolean isRequirePasswordReset = false;
	private int loginCount;
	private long lastLoginTime;
	private Set<Language> languages;
	private boolean isDeleted;
	private boolean isEmailBounced;
	private ClientSubscription clientSubscription;

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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> companies) {
		this.users = companies;
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
		user.setClient(this);
		return user;
	}

	/**
	 * Returns ServerCompany From ID
	 * 
	 * @param companyID
	 * @return
	 */
	public Company getCompany(long companyID) {
		Iterator<User> iterator = users.iterator();
		while (iterator.hasNext()) {
			Company next = iterator.next().getCompany();
			if (next.getID() == companyID) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns ServerCompany From ID
	 * 
	 * @param companyID
	 * @return
	 */
	public List<Company> getCompanies() {
		Iterator<User> iterator = users.iterator();
		List<Company> companies = new ArrayList<Company>();
		while (iterator.hasNext()) {
			Company next = iterator.next().getCompany();
			companies.add(next);
		}
		return companies;
	}

	public long getID() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isEmailBounced() {
		return isEmailBounced;
	}

	public void setEmailBounced(boolean isEmailBounced) {
		this.isEmailBounced = isEmailBounced;
	}

	public ClientSubscription getClientSubscription() {
		return clientSubscription;
	}

	public void setClientSubscription(ClientSubscription clientSubscription) {
		this.clientSubscription = clientSubscription;
	}
}
