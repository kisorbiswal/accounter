package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTransactionIssuePayment implements IAccounterCore {

	String stringID;

	long date;

	String writeCheck;

	String customerRefund;

	String payBill;

	String customerPrepayment;

	String creditCardCharge;

	String cashPurchase;

	String payVAT;

	String receiveVAT;

	String paySalesTax;

	String number;

	String name;

	String memo;

	double amount = 0D;

	ClientTransaction transaction;

	int recordType;

	int version;

	String paymentMethod;

	/**
	 * @return the payBill
	 */
	public String getPayBill() {
		return payBill;
	}

	/**
	 * @param payBill
	 *            the payBill to set
	 */
	public void setPayBill(String payBill) {
		this.payBill = payBill;
	}

	/**
	 * @return the creditCardCharge
	 */
	public String getCreditCardCharge() {
		return creditCardCharge;
	}

	/**
	 * @param creditCardCharge
	 *            the creditCardCharge to set
	 */
	public void setCreditCardCharge(String creditCardCharge) {
		this.creditCardCharge = creditCardCharge;
	}

	/**
	 * @return the payVAT
	 */

	public String getPayVAT() {
		return payVAT;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayVAT(String payVAT) {
		this.payVAT = payVAT;
	}

	/**
	 * @return the receiveVAT
	 */
	public String getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setReceiveVAT(String receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the cashPurchase
	 */
	public String getCashPurchase() {
		return cashPurchase;
	}

	/**
	 * @param cashPurchase
	 *            the cashPurchase to set
	 */
	public void setCashPurchase(String cashPurchase) {
		this.cashPurchase = cashPurchase;
	}

	/**
	 * @return the paySalesTax
	 */
	public String getPaySalesTax() {
		return paySalesTax;
	}

	/**
	 * @param paySalesTax
	 *            the paySalesTax to set
	 */
	public void setPaySalesTax(String paySalesTax) {
		this.paySalesTax = paySalesTax;
	}

	public String getPaymentMethod() {
		return paymentMethod;
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

	/**
	 * @return the writeCheck
	 */
	public String getWriteCheck() {
		return writeCheck;
	}

	/**
	 * @param writeCheckId
	 *            the writeCheck to set
	 */
	public void setWriteCheck(String writeCheckId) {
		this.writeCheck = writeCheckId;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the transaction
	 */
	public ClientTransaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionId
	 *            the transaction to set
	 */
	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;

	}

	/**
	 * @return the customerRefund
	 */
	public String getCustomerRefund() {
		return customerRefund;
	}

	/**
	 * @param customerRefundId
	 *            the customerRefund to set
	 */
	public void setCustomerRefund(String customerRefundId) {
		this.customerRefund = customerRefundId;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	public void setRecordType(int objectType) {
		this.recordType = objectType;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

		return "ClientTransactionIssuePayment";
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCustomerPrepayment(String customerPrepayment) {
		this.customerPrepayment = customerPrepayment;
	}

	public String getCustomerPrepayment() {
		return customerPrepayment;
	}

}
