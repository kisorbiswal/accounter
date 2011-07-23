package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientVendorCreditMemo extends ClientTransaction {

	String vendor;

	ClientContact contact;

	String phone;

	String accountsPayable;

	double balanceDue = 0D;

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return this.vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendorId) {
		this.vendor = vendorId;
	}

	/**
	 * @return the contact
	 */
	public ClientContact getContact() {
		return null;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(ClientContact contactId) {
		this.contact = contactId;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAccountsPayable() {
		return accountsPayable;
	}

	public void setAccountsPayable(String accountsPayableId) {
		this.accountsPayable = accountsPayableId;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientVendorCreditMemo";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VENDORCREDITMEMO;
	}
}
