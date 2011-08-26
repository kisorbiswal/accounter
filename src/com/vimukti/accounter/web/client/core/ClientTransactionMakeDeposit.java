package com.vimukti.accounter.web.client.core;

public class ClientTransactionMakeDeposit implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_FINANCIAL_ACCOUNT = 3;
	public static final int TYPE_VENDOR = 2;
	public static final int TYPE_CUSTOMER = 1;

	int version;
	long id;
	long date;
	int type;
	long account;
	long vendor;
	long customer;
	String reference;
	String paymentMethod;
	double amount;
	boolean isNewEntry = false;

	long cashAccount;

	ClientMakeDeposit makeDeposit;

	double payments = 0D;

	double balanceDue = 0D;

	// Transaction depositedTransaction;
	long depositedTransaction;

	String number;

	boolean isVoid;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setNewEntry(boolean isNewEntry) {
		this.isNewEntry = isNewEntry;
	}

	/**
	 * @return the account
	 */
	public long getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(long account) {
		this.account = account;
	}

	/**
	 * @return the vendor
	 */
	public long getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the customer
	 */
	public long getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @return the amount
	 */

	/**
	 * @return the isNewEntry
	 */
	public boolean getIsNewEntry() {
		return isNewEntry;
	}

	/**
	 * @param isNewEntry
	 *            the isNewEntry to set
	 */
	public void setIsNewEntry(boolean isNewEntry) {
		this.isNewEntry = isNewEntry;
	}

	/**
	 * @return the cashAccount
	 */
	public long getCashAccount() {
		return cashAccount;
	}

	/**
	 * @param cashAccountId
	 *            the cashAccount to set
	 */
	public void setCashAccount(long cashAccountId) {
		this.cashAccount = cashAccountId;
	}

	public ClientMakeDeposit getMakeDeposit() {
		return makeDeposit;
	}

	public void setMakeDeposit(ClientMakeDeposit makeDepositId) {
		this.makeDeposit = makeDepositId;
	}

	public double getPayments() {
		return payments;
	}

	public void setPayments(double payments) {
		this.payments = payments;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the depositedTransaction
	 */
	public long getDepositedTransaction() {
		return depositedTransaction;
	}

	/**
	 * @param depositedTransactionId
	 *            the depositedTransaction to set
	 */
	public void setDepositedTransaction(long depositedTransactionId) {
		this.depositedTransaction = depositedTransactionId;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransactionMakeDeposit";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSACTION_MAKEDEPOSIT;
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(ClientTransaction.TYPE_MAKE_DEPOSIT);
	}

	public ClientTransactionMakeDeposit clone() {
		ClientTransactionMakeDeposit clientTransactionMakeDepositClone = (ClientTransactionMakeDeposit) this
				.clone();
		clientTransactionMakeDepositClone.makeDeposit = this.makeDeposit
				.clone();
		return clientTransactionMakeDepositClone;
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
