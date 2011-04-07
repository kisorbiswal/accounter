package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;

public class User implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262438863405809492L;

	String firstName;

	String lastName;

	String emailId;

	String userRole;

	private boolean isActive;

	private UserPermissions permissions;

	private boolean canDoUserManagement;

	private String company;

	private String displayName;

	private boolean isAdmin;
	
	private int loginCount;

	int version;

	long id;
	/**
	 * The full name of the user.
	 */
	String fullName;
	String email;
	String passwordSha1Hash;

	/**
	 * The user name is different from the full name. This is the name by which
	 * user wants to login.
	 */
	String userName;
	/**
	 * This is field used for AccounterLive. This is the URL which is dedicated
	 * to the user at the time of registration.
	 */
	String domainURL;
	String stringID;

	private long lastLogin;
	FinanceDate createdDate;
	/**
	 * This User object is referenced to the Administer of this User. If the
	 * present reference itself is the Admin, then this field will remains as
	 * null.
	 */
	User admin;

	/**
	 * By this int value we can come to know the maximum number of users that
	 * can be assigned under the admin
	 */
	int maxUserCount;

	/**
	 * This is a AccounterLive variable. If the user is subscribed for
	 * newsletters etc, we have to add him to the mailingList. So that he will
	 * be mailed automatically whenever there is a new update or new feature in
	 * the software.
	 */
	boolean mailingList;

	/**
	 * Preferences that a user want to set.
	 */
	UserPreferences userPreferences = new UserPreferences();

	Address address = new Address();

	Contact contact = new Contact();

	transient boolean isImported;

	public User() {
		UserPreferences userPreferences = new UserPreferences();

		this.setUserPreferences(userPreferences);

	}

	public User(ClientUser clientUser) {
		// User user = new User();
		this.setStringID(clientUser.getStringID());
		this.setFirstName(clientUser.getFirstName());
		this.setLastName(clientUser.getLastName());
		this.setFullName(clientUser.getFullName());
		this.setEmailId(clientUser.getEmailId());
		this.setActive(clientUser.isActive());
		this.setCanDoUserManagement(clientUser.isCanDoUserManagement());
		this.setUserRole(clientUser.getUserRole());
		this.setAdmin(clientUser.isAdmin());
		UserPermissions userPermissions = new UserPermissions();
		userPermissions.setTypeOfBankReconcilation(clientUser.getPermissions()
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoicesAndExpenses(clientUser
				.getPermissions().getTypeOfInvoicesAndExpenses());
		userPermissions.setTypeOfSystemSettings(clientUser.getPermissions()
				.getTypeOfSystemSettings());
		userPermissions.setTypeOfViewReports(clientUser.getPermissions()
				.getTypeOfViewReports());
		userPermissions.setTypeOfPublishReports(clientUser.getPermissions()
				.getTypeOfPublishReports());
		userPermissions.setTypeOfLockDates(clientUser.getPermissions()
				.getTypeOfLockDates());
		this.setPermissions(userPermissions);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDomainURL() {
		return domainURL;
	}

	public void setDomainURL(String domainURL) {
		this.domainURL = domainURL;
	}

	public boolean isMailingList() {
		return mailingList;
	}

	public void setMailingList(boolean mailingList) {
		this.mailingList = mailingList;
	}

	/**
	 * @return the instanceVersion
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the passwordSha1Hash
	 */
	public String getPasswordSha1Hash() {
		return passwordSha1Hash;
	}

	/**
	 * @return the lastLogin
	 */
	public long getLastLogin() {
		return lastLogin;
	}

	/**
	 * @return the createdDate
	 */
	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the admin
	 */
	public User getAdmin() {
		return admin;
	}

	/**
	 * @return the maxUserCount
	 */
	public int getMaxUserCount() {
		return maxUserCount;
	}

	/**
	 * @return the userPreferences
	 */
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	/**
	 * @param userPreferences
	 *            the userPreferences to set
	 */
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	public void setFullName(String fname) {
		this.fullName = fname;

	}

	public void setEmail(String email) {
		this.email = email;

	}

	public void setPasswordSha1Hash(String string) {
		this.passwordSha1Hash = string;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	public void setPermissions(UserPermissions permissions) {
		this.permissions = permissions;
	}

	public UserPermissions getPermissions() {
		return permissions;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getName() {
		if (getFirstName() == null && getLastName() == null)
			return "";
		else if (getFirstName() == null)
			return getLastName();
		else if (getLastName() == null)
			return getFirstName();
		return getFirstName() + " " + getLastName();

	}

	public ClientUser getClientUser() {
		ClientUser user = new ClientUser();
		user.setFirstName(this.getFirstName());
		user.setLastName(this.getLastName());
		user.setEmailId(this.getEmailId());
		user.setCanDoUserManagement(this.isCanDoUserManagement());
		user.setUserRole(this.getUserRole());
		ClientUserPermissions userPermissions = new ClientUserPermissions();
		userPermissions.setTypeOfBankReconcilation(this.getPermissions()
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoicesAndExpenses(this.getPermissions()
				.getTypeOfInvoicesAndExpenses());
		userPermissions.setTypeOfSystemSettings(this.getPermissions()
				.getTypeOfSystemSettings());
		userPermissions.setTypeOfViewReports(this.getPermissions()
				.getTypeOfViewReports());
		userPermissions.setTypeOfPublishReports(this.getPermissions()
				.getTypeOfPublishReports());
		userPermissions.setTypeOfLockDates(this.getPermissions()
				.getTypeOfLockDates());
		user.setPermissions(userPermissions);
		return user;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public int getLoginCount() {
		return loginCount;
	}
}
