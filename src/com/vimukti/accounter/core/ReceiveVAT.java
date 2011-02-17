package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * 
 * It corresponds to Receive the VAT from VATAgency. It includes depostIn
 * account, VATAgency from which we are receiving, and the total amount we have
 * to receive. It also includes list of TransactionReceiveVAT which shows all
 * the vat amounts in vat code order.
 * 
 * @author Naresh G
 * 
 */

public class ReceiveVAT extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303963552649468307L;

	/*
	 * DepositIn {@link Account},
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * Bills Due On or Before.
	 */
	FinanceDate returnsDueOnOrBefore;

	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	double endingBalance;

	boolean isEdited = false;

	List<TransactionReceiveVAT> transactionReceiveVAT;

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the returnsDueOnOrBefore
	 */
	public FinanceDate getReturnsDueOnOrBefore() {
		return returnsDueOnOrBefore;
	}

	/**
	 * @param returnsDueOnOrBefore
	 *            the returnsDueOnOrBefore to set
	 */
	public void setReturnsDueOnOrBefore(FinanceDate returnsDueOnOrBefore) {
		this.returnsDueOnOrBefore = returnsDueOnOrBefore;
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
	 * @return the endingBalance
	 */
	public double getEndingBalance() {
		return endingBalance;
	}

	/**
	 * @param endingBalance
	 *            the endingBalance to set
	 */
	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	/**
	 * @return the isEdited
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionReceiveVAT
	 */
	public List<TransactionReceiveVAT> getTransactionReceiveVAT() {
		return transactionReceiveVAT;
	}

	/**
	 * @param transactionReceiveVAT
	 *            the transactionReceiveVAT to set
	 */
	public void setTransactionReceiveVAT(
			List<TransactionReceiveVAT> transactionReceiveVAT) {
		this.transactionReceiveVAT = transactionReceiveVAT;
	}

	/**
	 * @return the isImported
	 */
	public boolean isImported() {
		return isImported;
	}

	/**
	 * @param isImported
	 *            the isImported to set
	 */
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	@Override
	public Account getEffectingAccount() {

		return this.depositIn;
	}

	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	@Override
	public Payee getPayee() {

		return null;
	}

	@Override
	public int getTransactionCategory() {

		return 0;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {

		return false;
	}

	@Override
	public String toString() {

		return AccounterConstants.TYPE_RECEIVE_VAT;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		for (TransactionReceiveVAT t : transactionReceiveVAT) {
			t.setReceiveVAT(this);
		}
		if (this.id == 0) {
			super.onSave(session);

			if (!(this.paymentMethod
					.equals(AccounterConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		if (isBecameVoid()) {

			FinanceLogger.log("ReceiveVat with Number :" + this.number
					+ "is going to void");

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			if (this.transactionReceiveVAT != null) {
				for (TransactionReceiveVAT ti : this.transactionReceiveVAT) {
					if (ti instanceof Lifecycle) {
						Lifecycle lifeCycle = (Lifecycle) ti;
						lifeCycle.onUpdate(session);
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		if (this.isVoid) {
			throw new InvalidOperationException("Receive VAT can't be voided");
		}
		return super.canEdit(clientObject);
	}
}
