package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public abstract class ClientPayee implements IAccounterCore {

	public static final int TYPE_NONE = 0;

	public static final int TYPE_CUSTOMER = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_TAX_AGENCY = 4;

	public static final int TYPE_EMPLOYEE = 3;

	String name;

	long payeeSince;

	String fileAs;

	String stringID;

	int type;

	int version;

	long date;

	double balance;

	Set<ClientAddress> address = new HashSet<ClientAddress>();
	Set<ClientPhone> phoneNumbers = new HashSet<ClientPhone>();
	Set<ClientFax> faxNumbers = new HashSet<ClientFax>();
	Set<ClientEmail> emails = new HashSet<ClientEmail>();
	Set<ClientContact> contacts = new HashSet<ClientContact>();
	String webPageAddress;
	boolean isActive = Boolean.TRUE;
	String memo;
	String paymentMethod;
	private String phoneNo;
	private String faxNo;

	protected double openingBalance = 0D;

	// UK variables
	// boolean isEUVATExemptPayee;
	String VATRegistrationNumber;
	String TAXCode;
	boolean isOpeningBalanceEditable = Boolean.TRUE;
	private String email;
	boolean isDefault;

	public boolean isOpeningBalanceEditable() {
		return isOpeningBalanceEditable;
	}

	public void setOpeningBalanceEditable(boolean isOpeningBalanceEditable) {
		this.isOpeningBalanceEditable = isOpeningBalanceEditable;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the vATRegistrationNumber
	 */
	public String getVATRegistrationNumber() {
		return VATRegistrationNumber;
	}

	/**
	 * @param registrationNumber
	 *            the vATRegistrationNumber to set
	 */
	public void setVATRegistrationNumber(String registrationNumber) {
		VATRegistrationNumber = registrationNumber;
	}

	/**
	 * @return the vATCode
	 */
	public String getTAXCode() {
		return TAXCode;
	}

	/**
	 * @param code
	 *            the vATCode to set
	 */
	public void setTAXCode(String code) {
		TAXCode = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileAs() {
		return fileAs;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	/**
	 * @return the openingBalance
	 */
	public double getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public double getBalance() {
		return balance;
	}

	/**
	 * @return the id
	 */

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	public boolean isCustomer() {
		return this != null && this instanceof ClientCustomer;
	}

	public boolean isVendor() {
		return this != null && this instanceof ClientVendor;
	}

//	public boolean isTaxAgency() {
//		return this != null && this instanceof ClientTaxAgency;
//	}

	public boolean isEmployee() {
		return this != null && this instanceof ClientSalesPerson;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<ClientAddress> getAddress() {
		return address;
	}

	public void setAddress(Set<ClientAddress> address) {
		this.address = address;
	}

	public Set<ClientPhone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<ClientPhone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Set<ClientFax> getFaxNumbers() {
		return faxNumbers;
	}

	public void setFaxNumbers(Set<ClientFax> faxNumbers) {
		this.faxNumbers = faxNumbers;
	}

	public Set<ClientEmail> getEmails() {
		return emails;
	}

	public void setEmails(Set<ClientEmail> emails) {
		this.emails = emails;
	}

	public Set<ClientContact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ClientContact> contacts) {
		this.contacts = contacts;
	}

	public String getWebPageAddress() {
		return webPageAddress;
	}

	public void setWebPageAddress(String webPageAddress) {
		this.webPageAddress = webPageAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public long getPayeeSince() {
		return this.payeeSince;
	}

	public void setPayeeSince(long payeeSince) {
		this.payeeSince = payeeSince;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}
