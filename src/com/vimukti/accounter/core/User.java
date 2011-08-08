package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;

public class User extends CreatableObject implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262438863405809492L;

	private String firstName;

	private String lastName;

	private String email;

	private String userRole;

	private boolean isActive;

	private UserPermissions permissions;

	private boolean canDoUserManagement;

	private String company;

	private String displayName;

	private boolean isAdmin;

	private boolean isDeleted;

	/**
	 * The full name of the user.
	 */
	private String fullName;

	private String passwordSha1Hash;

	/**
	 * The user name is different from the full name. This is the name by which
	 * user wants to login.
	 */
	// String userName;
	/**
	 * This is field used for AccounterLive. This is the URL which is dedicated
	 * to the user at the time of registration.
	 */

	// private long lastLogin;
	/**
	 * This User object is referenced to the Administer of this User. If the
	 * present reference itself is the Admin, then this field will remains as
	 * null.
	 */
	// User admin;

	/**
	 * By this int value we can come to know the maximum number of users that
	 * can be assigned under the admin
	 */
	// int maxUserCount;

	/**
	 * This is a AccounterLive variable. If the user is subscribed for
	 * newsletters etc, we have to add him to the mailingList. So that he will
	 * be mailed automatically whenever there is a new update or new feature in
	 * the software.
	 */
	// boolean mailingList;

	/**
	 * Preferences that a user want to set.
	 */
	private UserPreferences userPreferences = new UserPreferences();

	// Address address = new Address();

	// Contact contact = new Contact();

	public boolean isLoggedInFromDomain;

	private boolean isMacApp;

	private String phoneNo;

	private String country;

	public User() {
		UserPreferences userPreferences = new UserPreferences();

		this.setUserPreferences(userPreferences);

	}

	public User(ClientUser clientUser) {
		// User user = new User();
		this.id = clientUser.getID();
		this.setFirstName(clientUser.getFirstName());
		this.setLastName(clientUser.getLastName());
		this.setFullName(clientUser.getFullName());
		this.setEmail(clientUser.getEmail());
		this.setActive(clientUser.isActive());
		this.setCanDoUserManagement(clientUser.isCanDoUserManagement());
		this.setUserRole(clientUser.getUserRole());
		this.setAdmin(clientUser.isAdmin());
		UserPermissions userPermissions = new UserPermissions();
		userPermissions.setTypeOfBankReconcilation(clientUser.getPermissions()
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoices(clientUser.getPermissions()
				.getTypeOfInvoices());
		userPermissions.setTypeOfExpences(clientUser.getPermissions()
				.getTypeOfExpences());
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

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return the passwordSha1Hash
	 */
	public String getPasswordSha1Hash() {
		return passwordSha1Hash;
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

	public void setFullName(String fname) {
		this.fullName = fname;

	}

	public void setPasswordSha1Hash(String string) {
		this.passwordSha1Hash = string;
	}

	@Override
	public long getID() {
		return this.id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String emailId) {
		this.email = emailId;
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
		user.setFullName(this.getFullName());
		user.setEmail(this.getEmail());
		user.setCanDoUserManagement(this.isCanDoUserManagement());
		user.setUserRole(this.getUserRole());
		ClientUserPermissions userPermissions = new ClientUserPermissions();
		userPermissions.setTypeOfBankReconcilation(this.getPermissions()
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoices(this.getPermissions()
				.getTypeOfInvoices());
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

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		FinanceLogger
				.log("User with Name {0} has been deleted", this.getName());

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.USER);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// its not using any where

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		// its not using any where
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {

		return false;
	}

	public boolean isMacApp() {
		return isMacApp;
	}

	public void setMacApp(boolean isMacApp) {
		this.isMacApp = isMacApp;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
