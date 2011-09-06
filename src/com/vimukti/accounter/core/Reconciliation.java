/**
 * 
 */
package com.vimukti.accounter.core;

import java.util.Set;

/**
 * @author Prasanna Kumar G
 * 
 */
public class Reconciliation extends CreatableObject {

	/** StartDate of the Reconciliation */
	private FinanceDate startDate;

	/** EndDate of the Reconciliation */
	private FinanceDate endDate;

	/** OpeningBalance of the Reconciliation */
	private double openingBalance;

	/** ClosingBalance of the Reconciliation */
	private double closingBalance;

	/** Date of the Reconciliation */
	private FinanceDate reconcilationDate;

	/** Transactions that are involved in this Reconciliation */
	private Set<Transaction> transactions;

	/**
	 * @return the startDate
	 */
	public FinanceDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public FinanceDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(FinanceDate endDate) {
		this.endDate = endDate;
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

	/**
	 * @return the closingBalance
	 */
	public double getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance
	 *            the closingBalance to set
	 */
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}

	/**
	 * @return the reconcilationDate
	 */
	public FinanceDate getReconcilationDate() {
		return reconcilationDate;
	}

	/**
	 * @param reconcilationDate
	 *            the reconcilationDate to set
	 */
	public void setReconcilationDate(FinanceDate reconcilationDate) {
		this.reconcilationDate = reconcilationDate;
	}

	/**
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions
	 *            the transactions to set
	 */
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
}
