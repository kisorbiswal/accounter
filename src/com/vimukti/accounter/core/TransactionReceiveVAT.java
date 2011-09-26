package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * For every ReceiveVAT entry there will be one or more TransactionReceiveVAT
 * entries. These are populated whenever any VATReturn is created. The VATAgency
 * reference indicates from which VATAgency we are receiving the VAT. And the
 * VATReturn reference indicates from which VATReturn this entry is made
 * 
 * @author Naresh G
 * 
 */

public class TransactionReceiveVAT implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9134646130329872990L;

	long id;

	/**
	 * The TaxAgency that we have selected for what we are making the
	 * PaySalesTax.
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	/**
	 * The amount of Tax which we still have to receive.
	 */
	double taxDue;

	/**
	 * The amount of Tax what we are receiving presently.
	 */
	double amountToReceive;

	@ReffereredObject
	VATReturn vatReturn;

	@ReffereredObject
	ReceiveVAT receiveVAT;

	int version;

	transient private boolean isOnSaveProccessed;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the taxDue
	 */
	public double getTaxDue() {
		return taxDue;
	}

	/**
	 * @param taxDue
	 *            the taxDue to set
	 */
	public void setTaxDue(double taxDue) {
		this.taxDue = taxDue;
	}

	/**
	 * @return the amountToReceive
	 */
	public double getAmountToReceive() {
		return amountToReceive;
	}

	/**
	 * @param amountToReceive
	 *            the amountToReceive to set
	 */
	public void setAmountToReceive(double amountToReceive) {
		this.amountToReceive = amountToReceive;
	}

	/**
	 * @return the vatReturn
	 */
	public VATReturn getVatReturn() {
		return vatReturn;
	}

	/**
	 * @param vatReturn
	 *            the vatReturn to set
	 */
	public void setVatReturn(VATReturn vatReturn) {
		this.vatReturn = vatReturn;
	}

	/**
	 * @return the receiveVAT
	 */
	public ReceiveVAT getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setReceiveVAT(ReceiveVAT receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the isOnSaveProccessed
	 */
	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	/**
	 * @param isOnSaveProccessed
	 *            the isOnSaveProccessed to set
	 */
	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l) {

			this.taxAgency.updateBalance(session, this.vatReturn,
					this.amountToReceive);

			// At the same time we need to update the vatReturn reference in it.
			this.vatReturn.updateBalance(this.amountToReceive);

			// The Accounts payable is also to be decreased as the amount to pay
			// to VATAgency is decreased.
			Account account = vatReturn.getCompany()
					.getVATFiledLiabilityAccount();
			if (account != null) {
				account.updateCurrentBalance(this.receiveVAT,
						this.amountToReceive);
				session.update(account);
				account.onUpdate(session);
			}

		}
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (isBecameVoid()) {

			// We need to update the corresponding VATAgency's balance with this
			// amount to pay.
			this.taxAgency.updateBalance(session, this.vatReturn, -1
					* this.amountToReceive);

			// At the same time we need to update the vatReturn reference in it.
			this.vatReturn.updateBalance(-1 * this.amountToReceive);

			// The Accounts payable is also to be decreased as the amount to pay
			// to VATAgency is decreased.
			Account account = vatReturn.getCompany()
					.getVATFiledLiabilityAccount();
			account.updateCurrentBalance(this.receiveVAT, -1
					* this.amountToReceive);
		}
		return false;
	}

	protected boolean isBecameVoid() {
		return this.receiveVAT.isBecameVoid();
	}

}
