package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientWriteCheck extends ClientTransaction {

	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_TAX_AGENCY = 4;
	public static final int TYPE_EMPLOYEE = 3;

	public static final String IS_TO_BE_PRINTED = null;

	int payToType;

	String bankAccount;

	double balance;

	String customer;

	String vendor;

	String taxAgency;

	ClientAddress address;

	double amount;

	boolean toBePrinted;

	String salesPerson;

	String inWords;

	String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

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
	 * @return the payToType
	 */
	public int getPayToType() {
		return payToType;
	}

	/**
	 * @param payToType
	 *            the payToType to set
	 */
	public void setPayToType(int payToType) {
		this.payToType = payToType;
	}

	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return this.bankAccount;
	}

	/**
	 * @param bankAccount
	 *            the bankAccount to set
	 */
	public void setBankAccount(String bankAccountId) {
		this.bankAccount = bankAccountId;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customerId) {
		this.customer = customerId;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendorId) {
		this.vendor = vendorId;
	}

	/**
	 * @return the taxAgency
	 */
	public String getTaxAgency() {
		return this.taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */
	public void setTaxAgency(String taxAgencyId) {
		this.taxAgency = taxAgencyId;
	}

	/**
	 * @return the address
	 */
	public ClientAddress getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(ClientAddress addressId) {
		this.address = addressId;
	}

	/**
	 * @return the toBePrinted
	 */
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	/**
	 * @param toBePrinted
	 *            the toBePrinted to set
	 */
	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	/**
	 * @return the inWords
	 */
	public String getInWords() {
		return inWords;
	}

	/**
	 * @param inWords
	 *            the inWords to set
	 */
	public void setInWords(String inWords) {
		this.inWords = inWords;
	}

	/**
	 * @return the salesPerson
	 */
	public String getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @param salesPerson
	 *            the salesPerson to set
	 */
	public void setSalesPerson(String salesPersonId) {
		this.salesPerson = salesPersonId;
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
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public String toString() {
		return AccounterConstants.TYPE_WRITE_CHECK;
	}

	@Override
	public String getDisplayName() {

		return getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

		return "ClientWriteCheck";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.WRITECHECK;
	}

}
