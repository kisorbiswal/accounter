package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A class which hold the tracking of the tax to be paid to the TAXAgency. It
 * holds all the values of the transaction needed such as liability Account,
 * reference to {@link Transaction} and the amount to which the tax owed
 * 
 * @author Chandan
 * 
 */
public class TAXReturnEntry implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8301696238073923983L;

	long id;

	Transaction transaction;

	int transactionType;

	TAXItem taxItem;

	TAXAgency taxAgency;

	int version;

	TAXRateCalculation taxRateCalculation;

	TAXAdjustment taxAdjustment;

	double netAmount;

	double grassAmount;

	transient private boolean isOnSaveProccessed;

	private double taxAmount;

	public TAXReturnEntry() {
	}

	public TAXReturnEntry(Transaction transaction, double taxableAmount,
			double rate, TAXItem taxItem) {

		this.transaction = transaction;
		this.taxItem = taxItem;
		this.taxAgency = taxItem.getTaxAgency();
		this.taxAmount = taxableAmount;
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
			throws AccounterException {
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

	/**
	 * @return the transactionType
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the grassAmount
	 */
	public double getGrassAmount() {
		return grassAmount;
	}

	/**
	 * @param grassAmount
	 *            the grassAmount to set
	 */
	public void setGrassAmount(double grassAmount) {
		this.grassAmount = grassAmount;
	}

	/**
	 * @return the taxAmount
	 */
	public double getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount
	 *            the taxAmount to set
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
}
