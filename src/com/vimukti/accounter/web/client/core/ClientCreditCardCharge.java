package com.vimukti.accounter.web.client.core;


@SuppressWarnings("serial")
public class ClientCreditCardCharge extends ClientTransaction {
	String vendor;

	ClientContact contact;

	ClientAddress vendorAddress;

	String phone;

	String payFrom;

	String checkNumber;

	long deliveryDate;

	/**
	 * @return the vendorId
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the contactId
	 */
	public ClientContact getContact() {
		return contact;
	}

	/**
	 * @param contactId
	 *            the contactId to set
	 */
	public void setContact(ClientContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the vendorAddressId
	 */
	public ClientAddress getVendorAddress() {
		return vendorAddress;
	}

	/**
	 * @param vendorAddressId
	 *            the vendorAddressId to set
	 */
	public void setVendorAddress(ClientAddress vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	/**
	 * @return the phone
	 */

	/**
	 * @return the payFromId
	 */

	/**
	 * @param payFromId
	 *            the payFromId to set
	 */
	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */

	/**
	 * @return the deliveryDate
	 */
	public long getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            the deliveryDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setPhone(String string) {
		this.phone = string;

	}

	public void setCheckNumber(String text) {
		this.checkNumber = text;

	}

	public void setDeliveryDate(ClientFinanceDate date) {
		this.deliveryDate = date.getTime();

	}

	public String getPayFrom() {
		return this.payFrom;
	}

	public String getPhone() {
		return this.phone;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {

		return "CreditCard Charge";
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

		return "ClientCreditCardCharge";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CREDITCARDCHARGE;
	}
}
