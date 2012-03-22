package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

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

public class ReceiveVAT extends Transaction implements IAccounterServerCore {

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

	private String checkNumber;

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

	@Override
	public Account getEffectingAccount() {

		return null;
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

		return AccounterServerConstants.TYPE_RECEIVE_VAT;
	}

	@Override
	public void onLoad(Session s, Serializable id) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		// super.onSave(session);
		this.isOnSaveProccessed = true;
		for (TransactionReceiveVAT t : transactionReceiveVAT) {
			t.setReceiveVAT(this);
		}
		if (this.getID() == 0) {
			super.onSave(session);

			if (!(this.paymentMethod
					.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK))
					&& !(this.paymentMethod
							.equals(AccounterServerConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
				this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			}

		}

		double paidAmount = total;
		if (getCurrency().getID() != getCompany().getPrimaryCurrency().getID()) {
			paidAmount = (paidAmount / currencyFactor);
		}
		this.depositIn.updateCurrentBalance(this, -paidAmount, currencyFactor);
		this.depositIn.onUpdate(session);
		session.update(this.depositIn);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}

		super.onUpdate(session);
		if (isBecameVoid()) {

			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
			if (this.transactionReceiveVAT != null) {
				for (TransactionReceiveVAT ti : this.transactionReceiveVAT) {
					if (ti instanceof Lifecycle) {
						Lifecycle lifeCycle = (Lifecycle) ti;
						lifeCycle.onUpdate(session);
					}
				}
			}
			double paidAmount = total;
			if (getCurrency().getID() != getCompany().getPrimaryCurrency()
					.getID()) {
				paidAmount = (paidAmount / currencyFactor);
			}
			this.depositIn.updateCurrentBalance(this, paidAmount,
					currencyFactor);
			this.depositIn.onUpdate(session);
			session.update(this.depositIn);
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		ReceiveVAT receiveVAT = (ReceiveVAT) clientObject;
		if (receiveVAT.isVoid()) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}

		if (!UserUtils.canDoThis(ReceiveVAT.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return super.canEdit(clientObject);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		// TODO Auto-generated method stub

	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		// TODO Auto-generated method stub

	}
}
