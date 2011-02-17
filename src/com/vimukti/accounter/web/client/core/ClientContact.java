package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientContact implements IAccounterCore {

	boolean isPrimary;
	String name = "";
	String title = "";
	String businessPhone = "";
	String email = "";

	/**
	 * @return the id
	 */

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary
	 *            the isPrimary to set
	 */
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the businessPhone
	 */
	public String getBusinessPhone() {
		return businessPhone;
	}

	/**
	 * @param businessPhone
	 *            the businessPhone to set
	 */
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
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

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.CONTACT;
	}

	@Override
	public String getStringID() {
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return "ClientContact";
	}

}
