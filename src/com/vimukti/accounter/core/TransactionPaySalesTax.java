package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * For every PaySalesTax entry there will be one or more TransactionPaySalesTax
 * entries. These are populated whenever any PaySalesTax is created. The
 * TaxAgency reference indicates to which TaxAgency we are paying the Tax. The
 * PaySalesTaxEntries reference indicates from which PaySalesTaxEntries this
 * entry is made. And the TAXAdjustment
 * 
 * @author Chandan
 * 
 */
public class TransactionPaySalesTax implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6891512227967571405L;

	long id;

	/**
	 * The TaxItem that is assigned for the TaxAgency for what we are making the
	 * PaysalesTax. There can be many TaxItems. So for every TaxItem a
	 * TransactionPaySalesTax is created.
	 */
	@ReffereredObject
	TAXItem taxItem;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * The amount of Tax which we still have to pay.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are paying presently.
	 */
	double amountToPay;

	/**
	 * This is used to indicate to which paysales tax this Transaction Paysales
	 * Tax pertains to.
	 */
	@ReffereredObject
	Transaction transaction;

	int version;

	/**
	 * This is used to indicate the entry for which this TransactionPaySalesTax
	 * reference is pertaining to.
	 */
	@ReffereredObject
	PaySalesTaxEntries paySalesTaxEntry;

	@ReffereredObject
	TAXAdjustment taxAdjustment;

	@ReffereredObject
	TAXRateCalculation taxRateCalculation;

	@ReffereredObject
	Account liabilityAccount;

	transient private boolean isOnSaveProccessed;

	public TransactionPaySalesTax() {
	}

	public int getVersion() {
		return version;
	}

	public TAXItem getTaxItem() {
		return taxItem;
	}

	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	public double getTaxDue() {
		return taxDue;
	}

	public double getAmountToPay() {
		return amountToPay;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	public Account getLiabilityAccount() {
		return liabilityAccount;
	}

	public void setLiabilityAccount(Account liabilityAccount) {
		this.liabilityAccount = liabilityAccount;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l) {

			this.liabilityAccount = this.taxAgency.getSalesLiabilityAccount();
			this.taxAgency.updateBalance(session, this.transaction, -1
					* this.amountToPay);
			liabilityAccount.updateCurrentBalance(this.transaction,
					-this.amountToPay);
			liabilityAccount.onUpdate(session);
			this.paySalesTaxEntry.updateBalance(this.amountToPay);

			if (this.taxAdjustment != null) {
				this.taxAdjustment.balanceDue -= this.amountToPay;
				session.saveOrUpdate(this.taxAdjustment);
			}

			if (this.taxRateCalculation != null) {
				this.taxRateCalculation.taxDue -= this.amountToPay;
				session.saveOrUpdate(this.taxRateCalculation);
			}
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (isBecameVoid()) {
			// TaxRateCalculation dummyTrc = new TaxRateCalculation();
			// dummyTrc.setLiabilityAccount(this.liabilityAccount);
			// this.taxAgency.updateBalance(session, this.transaction,
			// this.amountToPay, dummyTrc);
			// this.paySalesTaxEntry.updateBalance(-1 * this.amountToPay);
			// ChangeTracker.put(this);
		}
		return false;
	}

	protected boolean isBecameVoid() {
		return this.transaction.isBecameVoid();
	}

	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	public void setTransaction(PaySalesTax transaction) {
		this.transaction = transaction;
	}

	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	public void setAmountToPay(Double amontToPay) {
		this.amountToPay = amontToPay;
	}

	public void setTaxItem(TAXItem taxItem) {
		this.taxItem = taxItem;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
		
	}

}
