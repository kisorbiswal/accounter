package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * A class which hold the tracking of the tax to be paid to the TAXAgency. It
 * holds all the values of the transaction needed such as liability Account,
 * reference to {@link Transaction} and the amount to which the tax owed
 * 
 * @author Chandan
 * 
 */
public class PaySalesTaxEntries implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8301696238073923983L;

	long id;

	Transaction transaction;

	TAXItem taxItem;

	TAXAgency taxAgency;

	double amount;

	double balance;

	int version;

	// int status = 0;
	//
	// boolean isVoid;

	FinanceDate transactionDate;

	TAXRateCalculation taxRateCalculation;

	TAXAdjustment taxAdjustment;

	transient private boolean isOnSaveProccessed;

	public PaySalesTaxEntries() {
	}

	public PaySalesTaxEntries(Transaction transaction, double taxableAmount,
			double rate, TAXItem taxItem) {

		this.transaction = transaction;
		this.taxItem = taxItem;
		this.taxAgency = taxItem.getTaxAgency();
		this.amount = taxableAmount;
		this.balance = (taxableAmount * rate) / 100;
		this.transactionDate = this.transaction.getDate();
		// this.status = transaction.status;

	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the transactionDate
	 */
	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @param taxItem
	 *            the taxItem to set
	 */
	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	/**
	 * @param taxAgency
	 *            the taxAgency to set
	 */

	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	// public Transaction getTransaction() {
	// return transaction;
	// }
	//
	// public void setTransaction(Transaction transaction) {
	// this.transaction = transaction;
	// this.status = transaction.status;
	// this.isVoid = transaction.isVoid;
	// }

	public int getVersion() {
		return version;
	}

	public TAXItem getTaxItem() {
		return taxItem;
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	public double getAmount() {
		return amount;
	}

	public double getBalance() {
		return balance;
	}

	public void updateBalance(double amount) {

		this.balance -= amount;
	}

	public void updateAmountAndBalane(double amount) {
		this.amount += amount;
		this.balance += amount;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @return the taxRateCalculation
	 */
	public TAXRateCalculation getTaxRateCalculation() {
		return taxRateCalculation;
	}

	/**
	 * @param taxRateCalculation
	 *            the taxRateCalculation to set
	 */
	public void setTaxRateCalculation(TAXRateCalculation taxRateCalculation) {
		this.taxRateCalculation = taxRateCalculation;
	}

	public void setTaxAdjustment(TAXAdjustment taxAdjustment) {
		this.taxAdjustment = taxAdjustment;
	}

	public TAXAdjustment getTaxAdjustment() {
		return taxAdjustment;
	}
}
