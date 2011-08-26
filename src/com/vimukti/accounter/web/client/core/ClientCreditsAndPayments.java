package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

public class ClientCreditsAndPayments implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String memo;

	double creditAmount = 0D;

	double balance = 0D;

	ClientTransaction transaction;
	private int version;
	long payee;

	Set<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments = new HashSet<ClientTransactionCreditsAndPayments>();

	private double amtTouse;

	/* For stroing the balance temporarly */
	private double remaoningBalance;
	boolean recordChanged = false;

	public double getRemaoningBalance() {
		return remaoningBalance;
	}

	public void setRemaoningBalance(double remaoningBalance) {
		this.remaoningBalance = remaoningBalance;
	}

	public ClientTransaction getTransaction() {
		return transaction;
	}

	public void setRecordChanged(boolean isChanged) {
		this.recordChanged = isChanged;
	}

	public boolean isRecordChanged() {
		return recordChanged;
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
	}

	public void setAmtTouse(double amtTouse) {
		this.amtTouse = amtTouse;
	}

	public double getAmtTouse() {
		return this.amtTouse;
	}

	public long getPayee() {
		return payee;
	}

	public void setPayee(long payee) {
		this.payee = payee;
	}

	public Set<ClientTransactionCreditsAndPayments> getTransactionCreditsAndPayments() {
		return transactionCreditsAndPayments;
	}

	public void setTransactionCreditsAndPayments(
			Set<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments) {
		this.transactionCreditsAndPayments = transactionCreditsAndPayments;
	}

	public ClientCreditsAndPayments() {
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CREDITS_AND_PAYMENTS;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {

		return null;
	}

	private double actualAmt;

	public double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(double actualAmt) {
		this.actualAmt = actualAmt;
	}

	@Override
	public String getClientClassSimpleName() {
		// its not using any where
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

	public ClientCreditsAndPayments clone() {
		ClientCreditsAndPayments creditsAndPayments = (ClientCreditsAndPayments) this
				.clone();
		creditsAndPayments.transaction = this.transaction.clone();
		Set<ClientTransactionCreditsAndPayments> creditsAndPaymentsSet = new HashSet<ClientTransactionCreditsAndPayments>();
		for (ClientTransactionCreditsAndPayments clientTransactionCreditsAndPayments : this.transactionCreditsAndPayments) {
			creditsAndPaymentsSet.add(clientTransactionCreditsAndPayments
					.clone());
		}
		creditsAndPayments.transactionCreditsAndPayments = creditsAndPaymentsSet;
		return creditsAndPayments;

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
