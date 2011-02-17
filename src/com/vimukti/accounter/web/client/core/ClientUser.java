package com.vimukti.accounter.web.client.core;


@SuppressWarnings("serial")
public class ClientUser implements IAccounterCore {

	int version;
	String stringID;

	String fullName;
	String email;
	String passwordSha1Hash;

	long lastLogin;
	long createdDate;
	String admin;

	int maxUserCount;

	ClientCompany company;

	ClientUserPreferences userPreferences = new ClientUserPreferences();
	// long userPreferencesId;

	ClientAddress address = new ClientAddress();

	ClientContact contact = new ClientContact();

	public ClientUser() {
		ClientUserPreferences userPreferences = new ClientUserPreferences();

		this.setUserPreferences(userPreferences);

	}

	public ClientCompany getCompany() {
		return company;
	}

	public void setCompany(ClientCompany company) {
		this.company = company;
	}

	/**
	 * @return the instanceVersion
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param instanceVersion
	 *            the instanceVersion to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the passwordSha1Hash
	 */
	public String getPasswordSha1Hash() {
		return passwordSha1Hash;
	}

	/**
	 * @param passwordSha1Hash
	 *            the passwordSha1Hash to set
	 */
	public void setPasswordSha1Hash(String passwordSha1Hash) {
		this.passwordSha1Hash = passwordSha1Hash;
	}

	/**
	 * @return the lastLogin
	 */
	public ClientFinanceDate getLastLogin() {
		return new ClientFinanceDate(lastLogin);
	}

	/**
	 * @return the createdDate
	 */
	public ClientFinanceDate getCreatedDate() {
		return new ClientFinanceDate(createdDate);
	}

	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(String adminId) {
		this.admin = adminId;
	}

	/**
	 * @return the maxUserCount
	 */
	public int getMaxUserCount() {
		return maxUserCount;
	}

	/**
	 * @param maxUserCount
	 *            the maxUserCount to set
	 */
	public void setMaxUserCount(int maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	// /**
	// * @return the companies
	// */
	// public Set<ClientCompany> getCompanies() {
	// return companies;
	// }
	//
	// /**
	// * @param companies
	// * the companies to set
	// */
	// public void setCompanies(Set<ClientCompany> companies) {
	// this.companies = companies;
	// }

	/**
	 * @return the userPreferences
	 */

	public ClientCompany getDefaultCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	// public long getUserPreferencesId() {
	// return userPreferencesId;
	// }
	public ClientUserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(ClientUserPreferences clientUserPreferencesId) {
		this.userPreferences = clientUserPreferencesId;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress addressId) {
		this.address = addressId;
	}

	public ClientContact getContact() {
		return contact;
	}

	public void setContact(ClientContact contactId) {
		this.contact = contactId;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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

		return "ClientUser";
	}

}
