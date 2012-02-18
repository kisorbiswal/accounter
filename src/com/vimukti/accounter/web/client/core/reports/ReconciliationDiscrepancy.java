package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class ReconciliationDiscrepancy extends BaseReport implements
		Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long transactionId;

	private ClientFinanceDate transactionDate;

	private ClientFinanceDate enteredDate;

	private String transactionNumber;

	private String name;

	private double reconciliedAmount;

	private String accountName;

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientFinanceDate getEnteredDate() {
		return enteredDate;
	}

	public void setEnteredDate(ClientFinanceDate enteredDate) {
		this.enteredDate = enteredDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getReconciliedAmount() {
		return reconciliedAmount;
	}

	public void setReconciliedAmount(double reconciliedAmount) {
		this.reconciliedAmount = reconciliedAmount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
