package com.vimukti.accounter.core;

import java.util.List;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
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

	Account depositFrom;

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

			this.cashBackAccount.updateCurrentBalance(this,
					this.cashBackAmount, currencyFactor);
			this.cashBackAccount.onUpdate(session);

		}

		depositIn.updateCurrentBalance(this, -this.total, this.currencyFactor);
		session.save(depositIn);
		depositFrom.updateCurrentBalance(this, this.total, this.currencyFactor);
		session.save(depositFrom);

		return false;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		checkAccountNull(depositFrom);
		checkAccountNull(depositIn);
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
		return null;
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

	public Account getDepositFrom() {
		return depositFrom;
	}

	public void setDepositFrom(Account depositFrom) {
		this.depositFrom = depositFrom;
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

		if (this.isVoid && !makeDeposit.isVoid) {
			this.doVoidEffect(session);

		} else {
			if (!this.depositIn.equals(makeDeposit.depositIn)
					|| !DecimalUtil.isEquals(this.total, makeDeposit.total)) {
				Account depositInAccount = (Account) session.get(Account.class,
						makeDeposit.depositIn.id);
				depositInAccount.updateCurrentBalance(this, makeDeposit.total,
						makeDeposit.currencyFactor);
				depositInAccount.onUpdate(session);
				session.saveOrUpdate(depositInAccount);

				this.depositIn.updateCurrentBalance(this, -this.total,
						this.currencyFactor);
				this.depositIn.onUpdate(session);
				session.saveOrUpdate(this.depositIn);
			}
			if (!this.depositFrom.equals(makeDeposit.depositFrom)
					|| !DecimalUtil.isEquals(this.total, makeDeposit.total)) {
				Account depositFromAccount = (Account) session.get(
						Account.class, makeDeposit.depositFrom.id);
				depositFromAccount.updateCurrentBalance(this,
						-makeDeposit.total, makeDeposit.currencyFactor);
				depositFromAccount.onUpdate(session);
				session.saveOrUpdate(depositFromAccount);

				this.depositFrom.updateCurrentBalance(this, this.total,
						this.currencyFactor);
				this.depositFrom.onUpdate(session);
				session.saveOrUpdate(this.depositFrom);
			}
		}
		super.onEdit(makeDeposit);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid) {
			doVoidEffect(session);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session) {
		depositIn.updateCurrentBalance(this, this.total, this.currencyFactor);
		session.save(depositIn);
		depositFrom
				.updateCurrentBalance(this, -this.total, this.currencyFactor);
		session.save(depositFrom);
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

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		if (cashBackAccount != null) {
			map.put(cashBackAccount, cashBackAmount);
		}
		for (TransactionMakeDeposit deposit : transactionMakeDeposit) {
			map.put(deposit.getEffectingAccount(), deposit.getAmount());
		}
		return map;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.makeDeposit()).gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap();
		w.put(messages.currency(), this.currencyFactor);
		w.put(messages.amount(), this.total).gap();
		w.put(messages.paymentMethod(), this.paymentMethod);
		w.put(messages.memo(), this.memo).gap();
	}
}
