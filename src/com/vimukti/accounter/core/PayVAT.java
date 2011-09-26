package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * It corresponds to paying the VAT to VATAgency. It includes payFrom account,
 * VATAgency to which we are paying, and the total amount we have to pay. It
 * also includes list of TransactionPayVAT which shows all the vat amounts in
 * vat code order.
 * 
 * @author Chandan
 * 
 */
public class PayVAT extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303963552649468306L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;

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

	List<TransactionPayVAT> transactionPayVAT;

	//

	/**
	 * @return the payFrom
	 */
	public Account getPayFrom() {
		return payFrom;
	}

	/**
	 * @param payFrom
	 *            the payFrom to set
	 */
	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
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
	 * @return the transactionPayVAT
	 */
	public List<TransactionPayVAT> getTransactionPayVAT() {
		return transactionPayVAT;
	}

	/**
	 * @param transactionPayVAT
	 *            the transactionPayVAT to set
	 */
	public void setTransactionPayVAT(List<TransactionPayVAT> transactionPayVAT) {
		this.transactionPayVAT = transactionPayVAT;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		for (TransactionPayVAT t : transactionPayVAT) {
			t.setPayVAT(this);
		}
		if (this.id == 0) {
			super.onSave(session);

			if (!(this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		if (isBecameVoid()) {

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			if (this.transactionPayVAT != null) {
				for (TransactionPayVAT ti : this.transactionPayVAT) {
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
	public Account getEffectingAccount() {

		return this.payFrom;
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

		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {

		return AccounterServerConstants.TYPE_PAY_VAT;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		if (this.isVoid) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

}
