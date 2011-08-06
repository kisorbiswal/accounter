package com.vimukti.accounter.web.client.core;

public class ClientEmployee implements IAccounterCore {

	/**
	 * EMPLOYEE DETAILS VARIABLES
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String email;

	private boolean isActive;

	private String fullName;
	private String firstName;
	private String lastName;
	private String displayName;

	private String userRole;

	private ClientUserPermissions permissions;

	/**
	 * EMPLOYEE DETAILS METHODS
	 */
	@Override
	public String getClientClassSimpleName() {
		return "ClientEmployee";
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
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
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole
	 *            the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return the permissions
	 */
	public ClientUserPermissions getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(ClientUserPermissions permissions) {
		this.permissions = permissions;
	}

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

	@Override
	public String getName() {
		return this.fullName;
	}

	public ClientEmployee clone() {
		ClientEmployee employee = (ClientEmployee) this.clone();
		employee.permissions = this.permissions.clone();
		return employee;

	}

}
