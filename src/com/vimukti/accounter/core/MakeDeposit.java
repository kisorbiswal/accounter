package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati <br>
 * <br>
 *         This Transaction is Particularly to Deposit the amounts which are Un
 *         Deposited in our Accounting System.
 * 
 */

public class MakeDeposit extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4987826863985791988L;

	/**
	 * DepositIn {@link Account}
	 */
	@ReffereredObject
	Account depositIn;

	/**
	 * Cash Back {@link Account}, for this MakeDeposit
	 */
	@ReffereredObject
	Account cashBackAccount;

	/**
	 * Description for Cash Back Memo
	 */
	String cashBackMemo;

	/**
	 * 
	 */
	double cashBackAmount;

	//

	List<TransactionMakeDeposit> transactionMakeDeposit;

	public MakeDeposit() {
		setType(Transaction.TYPE_MAKE_DEPOSIT);
	}

	/**
	 * @return the depositIn
	 */
	public Account getDepositIn() {
		return depositIn;
	}

	/**
	 * @return the cashBackAccount
	 */
	public Account getCashBackAccount() {
		return cashBackAccount;
	}

	/**
	 * @return the cashBackMemo
	 */
	public String getCashBackMemo() {
		return cashBackMemo;
	}

	/**
	 * @return the cashBackAmount
	 */
	public double getCashBackAmount() {
		return cashBackAmount;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (this.cashBackAccount != null) {

			this.cashBackAccount
					.updateCurrentBalance(this, this.cashBackAmount);
			this.cashBackAccount.onUpdate(session);

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);
		// if (isBecameVoid()) {
		// if (cashBackAccount != null) {
		// this.cashBackAccount.updateCurrentBalance(this, -1
		// * this.cashBackAmount);
		// this.cashBackAccount.onUpdate(session);
		// }
		// }
		// for (TransactionMakeDeposit transactionMakeDeposit :
		// this.transactionMakeDeposit) {
		// transactionMakeDeposit.setIsVoid(Boolean.TRUE);
		// session.update(transactionMakeDeposit);
		// if (transactionMakeDeposit instanceof Lifecycle) {
		// Lifecycle lifeCycle = (Lifecycle) transactionMakeDeposit;
		// lifeCycle.onUpdate(session);
		// }
		// }
		return false;
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
	public Account getEffectingAccount() {
		return this.depositIn;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the transactionMakeDeposit
	 */
	public List<TransactionMakeDeposit> getTransactionMakeDeposit() {
		return transactionMakeDeposit;
	}

	/**
	 * @param transactionMakeDeposit
	 *            the transactionMakeDeposit to set
	 */
	public void setTransactionMakeDeposit(
			List<TransactionMakeDeposit> transactionMakeDeposit) {
		this.transactionMakeDeposit = transactionMakeDeposit;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public void setCashBackAccount(Account cashBankAccount) {
		this.cashBackAccount = cashBankAccount;
	}

	public void setCashBackMemo(String cashBackMemo) {
		this.cashBackMemo = cashBackMemo;
	}

	public void setDepositIn(Account depositIn) {
		this.depositIn = depositIn;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_BANKING;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_MAKE_DEPOSIT;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	public boolean equals(MakeDeposit obj) {
		if (((this.depositIn != null && obj.depositIn != null) ? (this.depositIn
				.equals(obj.depositIn)) : true)
				&& ((this.cashBackAccount != null && obj.cashBackAccount != null) ? (this.cashBackAccount
						.equals(obj.cashBackAccount)) : true)
				&& ((!DecimalUtil.isEquals(this.total, 0) && !DecimalUtil
						.isEquals(obj.total, 0)) ? DecimalUtil.isEquals(
						this.total, obj.total) : true)
				&& ((!DecimalUtil.isEquals(this.cashBackAmount, 0) && !DecimalUtil
						.isEquals(obj.cashBackAmount, 0)) ? DecimalUtil
						.isEquals(this.cashBackAmount, obj.cashBackAmount)
						: true)
				&& (this.transactionMakeDeposit.size() == obj.transactionMakeDeposit
						.size())) {
			for (int i = 0; i < this.transactionMakeDeposit.size(); i++) {
				if (!this.transactionMakeDeposit.get(i).equals(
						obj.transactionMakeDeposit.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		Session session = HibernateUtil.getCurrentSession();
		MakeDeposit makeDeposit = (MakeDeposit) clonedObject;

		if ((this.isVoid() && !makeDeposit.isVoid())
				|| (this.isDeleted() && !makeDeposit.isDeleted())) {
			this.doVoidEffect(session);

		} else {
			Account depositInAccount = (Account) session.get(Account.class,
					makeDeposit.depositIn.id);
			depositInAccount.updateCurrentBalance(this,
					makeDeposit.total);
			depositInAccount.onUpdate(session);

			makeDeposit.doVoidEffect(session);
			cleanTransactionMakeDeposits(makeDeposit);

			this.onSave(session);
		}

		super.onEdit(makeDeposit);
	}

	private void doVoidEffect(Session session) {
		for (TransactionMakeDeposit transactionMakeDeposit : this.transactionMakeDeposit) {
			transactionMakeDeposit.setIsVoid(true);
			transactionMakeDeposit.onUpdate(session);
		}
	}

	private void cleanTransactionMakeDeposits(MakeDeposit makeDeposit) {
		if (makeDeposit.getTransactionMakeDeposit() != null)
			makeDeposit.transactionMakeDeposit.clear();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		boolean flag;
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		flag = super.canEdit(clientObject);
		session.setFlushMode(flushMode);
		return flag;
	}

}
