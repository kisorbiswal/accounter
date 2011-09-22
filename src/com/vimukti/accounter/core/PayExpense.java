package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayExpense extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8470751684678534464L;

	/**
	 * Paid From {@link Account}
	 */
	Account paidFrom;

	/**
	 * Reference or Cheque Number for which, the PayExpense Transaction has
	 * been, associated
	 */
	String refernceOrChequeNumber;

	/**
	 * List of {@link TransactionPayExpense}'s
	 */
	List<TransactionPayExpense> transactionPayExpenses;

	//

	public Account getPaidFrom() {
		return paidFrom;
	}

	public void setPaidFrom(Account paidFrom) {
		this.paidFrom = paidFrom;
	}

	public String getRefernceOrChequeNumber() {
		return refernceOrChequeNumber;
	}

	public void setRefernceOrChequeNumber(String refernceOrChequeNumber) {
		this.refernceOrChequeNumber = refernceOrChequeNumber;
	}

	/**
	 * @return the transactionPayExpenses
	 */
	public List<TransactionPayExpense> getTransactionPayExpenses() {
		return transactionPayExpenses;
	}

	/**
	 * @param transactionPayExpenses
	 *            the transactionPayExpenses to set
	 */
	public void setTransactionPayExpenses(
			List<TransactionPayExpense> transactionPayExpenses) {
		this.transactionPayExpenses = transactionPayExpenses;
	}

	@Override
	public Account getEffectingAccount() {
		return this.paidFrom;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		Account accountPayable = getCompany().getAccountsPayableAccount();
		accountPayable.updateCurrentBalance(this, -this.total);
		session.update(accountPayable);
		accountPayable.onUpdate(session);

		return false;
	}

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {

		return null;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

}
