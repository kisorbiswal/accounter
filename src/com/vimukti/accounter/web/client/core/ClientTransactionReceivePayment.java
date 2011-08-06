package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class ClientTransactionReceivePayment implements IAccounterCore {

	int version;

	long dueDate;

	long invoice;

	double invoiceAmount = 0D;

	long discountDate;

	long discountAccount;

	double cashDiscount = 0D;

	long writeOffAccount;

	double writeOff = 0D;

	double appliedCredits = 0D;

	double payment = 0D;

	long receivePayment;

	List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments;

	boolean isVoid = false;

	long customerRefund;

	public transient boolean isInvoice;

	long journalEntry;

	double amountDue;
	private String number;

	private long id;

	/* The following fields are just for saving credits temporarly */
	Map<Integer, Object> tempCredits = new HashMap<Integer, Object>();
	double remainingCreditBalance;
	boolean isCreditsApplied;

	private double dummyDue;

	public boolean isCreditsApplied() {
		return isCreditsApplied;
	}

	public void setCreditsApplied(boolean isCreditsApplied) {
		this.isCreditsApplied = isCreditsApplied;
	}

	public Map<Integer, Object> getTempCredits() {
		return tempCredits;
	}

	public void setTempCredits(Map<Integer, Object> tempCredits) {
		this.tempCredits = tempCredits;
	}

	public double getRemainingCreditBalance() {
		return remainingCreditBalance;
	}

	public void setRemainingCreditBalance(double remainingCreditBalance) {
		this.remainingCreditBalance = remainingCreditBalance;
	}

	public double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	public long getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(long journalEntry) {
		this.journalEntry = journalEntry;
	}

	/**
	 * @return the id
	 */

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the dueDate
	 */
	public long getDueDate() {
		return dueDate;
	}

	/**
	 * @return the invoice
	 */
	public long getInvoice() {
		return invoice;
	}

	/**
	 * @return the invoiceAmount
	 */
	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * @return the discountDate
	 */
	public long getDiscountDate() {
		return discountDate;
	}

	public long getDiscountAccount() {
		return discountAccount;
	}

	/**
	 * @return the cashDiscount
	 */
	public double getCashDiscount() {
		return cashDiscount;
	}

	public long getWriteOffAccount() {
		return writeOffAccount;
	}

	/**
	 * @return the writeOff
	 */
	public double getWriteOff() {
		return writeOff;
	}

	/**
	 * @return the appliedCredits
	 */

	public double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param invoice
	 *            the invoice to set
	 */
	public void setInvoice(long invoice) {
		this.invoice = invoice;
	}

	/**
	 * @param invoiceAmount
	 *            the invoiceAmount to set
	 */
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	/**
	 * @param date
	 *            the discountDate to set
	 */
	public void setDiscountDate(long date) {
		this.discountDate = date;
	}

	public void setDiscountAccount(long discountAccount) {
		this.discountAccount = discountAccount;
	}

	/**
	 * @param cashDiscount
	 *            the cashDiscount to set
	 */
	public void setCashDiscount(double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	public void setWriteOffAccount(long writeOffAccount) {
		this.writeOffAccount = writeOffAccount;
	}

	/**
	 * @param writeOff
	 *            the writeOff to set
	 */
	public void setWriteOff(double writeOff) {
		this.writeOff = writeOff;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */

	public void setAppliedCredits(double appliedCredits) {
		this.appliedCredits = appliedCredits;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}

	/**
	 * @return the transaction
	 */
	public long getReceivePayment() {
		return receivePayment;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setReceivePayment(long receivePaymentId) {
		this.receivePayment = receivePaymentId;
	}

	public List<ClientTransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the customerRefund
	 */
	public long getCustomerRefund() {
		return customerRefund;
	}

	public void setDiscountAccount(ClientAccount cashDiscountAccount) {

		this.discountAccount = cashDiscountAccount.getID();
	}

	public void setCustomerRefund(long customerRefund) {
		this.customerRefund = customerRefund;
	}

	public void setTransaction(long receivePayment) {
		this.receivePayment = receivePayment;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
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

		return "ClientTransactionReceivePayment";
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setDummyDue(double amountDue) {
		this.dummyDue = amountDue;
	}

	public double getDummyDue() {
		return dummyDue;
	}

	public ClientTransactionReceivePayment clone() {
		ClientTransactionReceivePayment clientTransactionReceivePaymentClone = (ClientTransactionReceivePayment) this
				.clone();
		List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
		for (ClientTransactionCreditsAndPayments creditsAndPayments : this.transactionCreditsAndPayments) {
			transactionCreditsAndPayments.add(creditsAndPayments.clone());
		}
		clientTransactionReceivePaymentClone.transactionCreditsAndPayments = transactionCreditsAndPayments;
		Map<Integer, Object> tempCredit = new HashMap<Integer, Object>();
		for (Entry<Integer, Object> entrySet : tempCredits.entrySet()) {
			tempCredit.put(entrySet.getKey(), (entrySet.getValue()))

		}

		return clientTransactionReceivePaymentClone;
	}
}
