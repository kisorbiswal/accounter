package com.vimukti.accounter.web.client.core;

public class ClientContact implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean isPrimary;
	String name = "";
	String title = "";
	String businessPhone = "";
	String email = "";

	private int version;

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
	public long getID() {
		return 0;
	}

	@Override
	public void setID(long id) {
		// this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientContact";
	}

	public ClientContact clone() {
		ClientContact contact = (ClientContact) this.clone();
		return contact;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof ClientContact) {
			ClientContact clientContact = (ClientContact) obj;
			if (!clientContact.getEmail().equals(getEmail())) {
				return false;
			}
			if (!clientContact.getName().equals(getName())) {
				return false;
			}
			if (!clientContact.getTitle().equals(getTitle())) {
				return false;
			}
			if (!clientContact.getBusinessPhone().equals(getBusinessPhone())) {
				return false;
			}
			if (clientContact.isPrimary() != isPrimary()) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}
}
