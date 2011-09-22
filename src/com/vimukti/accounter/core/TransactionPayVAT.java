package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * For every PayVAT entry there will be one or more TransactionPayVAT entries.
 * These are populated whenever any VATReturn is created. The VATAgency
 * reference indicates to which VATAgency we are paying the VAT. And the
 * VATReturn reference indicates from which VATReturn this entry is made
 * 
 * @author Chandan
 * 
 */
public class TransactionPayVAT implements IAccounterServerCore, Lifecycle {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9134646130329872989L;

	long id;

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

	@ReffereredObject
	VATReturn vatReturn;

	@ReffereredObject
	PayVAT payVAT;

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
	 * @return the amountToPay
	 */
	public double getAmountToPay() {
		return amountToPay;
	}

	/**
	 * @param amountToPay
	 *            the amountToPay to set
	 */
	public void setAmountToPay(double amountToPay) {
		this.amountToPay = amountToPay;
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
	 * @return the payVAT
	 */
	public PayVAT getPayVAT() {
		return payVAT;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayVAT(PayVAT payVAT) {
		this.payVAT = payVAT;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {

		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// currently not using anywhere in the project.

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (this.id == 0l) {
			// this.liabilityAccount=this.vatAgency.getLiabilityAccount();

			// We need to update the corresponding VATAgency's balance with this
			// amount to pay.
			this.taxAgency.updateBalance(session, this.vatReturn, -1
					* this.amountToPay);

			FinanceLogger.log("VATReturn with Number:" + this.vatReturn.number
					+ " balance has been updated with amount: -"
					+ this.amountToPay);

			// At the same time we need to update the vatReturn reference in it.
			this.vatReturn.updateBalance(-this.amountToPay);

			// The Accounts payable is also to be decreased as the amount to pay
			// to VATAgency is decreased.
			Account account = vatReturn.getCompany()
					.getVATFiledLiabilityAccount();
			if (account != null) {
				account.updateCurrentBalance(this.payVAT, -(this.amountToPay));
				session.update(account);
				account.onUpdate(session);
			}

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (isBecameVoid()) {
			FinanceLogger.log("TransactionPayVat is going to void");

			// We need to update the corresponding VATAgency's balance with this
			// amount to pay.
			this.taxAgency.updateBalance(session, this.vatReturn,
					this.amountToPay);

			FinanceLogger.log("Vatreturn with Number:" + this.vatReturn.number
					+ " balance has been updated with amount: "
					+ this.amountToPay);

			// At the same time we need to update the vatReturn reference in it.
			this.vatReturn.updateBalance(this.amountToPay);

			// The Accounts payable is also to be decreased as the amount to pay
			// to VATAgency is decreased.
			vatReturn.getCompany().getVATFiledLiabilityAccount()
					.updateCurrentBalance(this.payVAT, this.amountToPay);
		}
		return false;
	}

	protected boolean isBecameVoid() {
		return this.payVAT.isBecameVoid();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

}
