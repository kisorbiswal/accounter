package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class JournalEntry extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7677695043049426006L;
	/**
	 * 
	 */
	public static final int TYPE_NORMAL_JOURNAL_ENTRY = 1;
	public static final int TYPE_CASH_BASIS_JOURNAL_ENTRY = 2;

	/**
	 * Transaction Journal Entry Debit Total
	 */
	double debitTotal = 0D;

	/**
	 * Transaction Journal Entry Credit Total
	 */
	double creditTotal = 0D;

	/**
	 * Type of {@link JournalEntry} , Either TYPE_NORMAL_JOURNAL_ENTRY or
	 * TYPE_CASH_BASIS_JOURNAL_ENTRY
	 */
	int journalEntryType = TYPE_NORMAL_JOURNAL_ENTRY;

	double balanceDue = 0d;

	/**
	 * A Journal Entry Has List of Entries List of Entries unlike Transaction
	 * Items,
	 * 
	 * @see TransactionItem
	 */

	Payee involvedPayee;

	Account involvedAccount;

	// @ReffereredObject
	// Transaction transaction;

	public Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	Set<TransactionPayBill> transactionPayBills = new HashSet<TransactionPayBill>();

	//

	public JournalEntry() {
		setType(Transaction.TYPE_JOURNAL_ENTRY);
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return id;
	}

	/**
	 * @return the memo
	 */
	@Override
	public String getMemo() {
		return memo;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public double getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
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

	@Override
	public int getTransactionCategory() {
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_JOURNAL_ENTRY;
	}

	@Override
	public void onLoad(Session session, Serializable arg1) {
		this.isVoidBefore = isVoid;
		super.onLoad(session, arg1);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		this.total = this.debitTotal;

		// Creating Activity
		Activity activity = new Activity(getCompany(),
				AccounterThreadLocal.get(), ActivityType.ADD, this);
		session.save(activity);
		this.setLastActivity(activity);

		return false;
	}

	@Override
	protected void checkNullValues() {
		if (this.creditTotal != this.debitTotal) {
			try {
				throw new AccounterException(
						AccounterException.ERROR_CREDIT_DEBIT_TOTALS_NOT_EQUAL);
			} catch (AccounterException e) {
			}
		}
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		super.onUpdate(session);

		return false;
	}

	protected boolean isBecameVoid() {
		return isVoid && !this.isVoidBefore;
	}

	@Override
	public Payee getInvolvedPayee() {
		return involvedPayee;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {

		this.balanceDue += amount2;
	}

	private void doVoidEffect(Session session, JournalEntry journalEntry) {

		/* If this Journal Entry has any Credits then make them as null. */
		if (this.creditsAndPayments != null)
			this.creditsAndPayments = null;

		/*
		 * If this Journal entry has been paid in any receive payments then roll
		 * back the payments and Update the credits balances.
		 */
		if (this.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				trp.onVoidTransaction(session);
				session.saveOrUpdate(trp);
			}
		}

		/*
		 * If this Journal entry has been paid in any payments then roll back
		 * the payments and and Update the credits balances.
		 */
		for (TransactionPayBill tx : this.transactionPayBills) {
			tx.makeVoid(session);
		}

		this.balanceDue = 0.0;

	}

	@Override
	public void onEdit(Transaction clonedObject) {

		super.onEdit(clonedObject);
		if (isBecameVoid()) {
			doReverseEffect(HibernateUtil.getCurrentSession());
		}

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!isVoid) {
			doReverseEffect(session);
		}
		return super.onDelete(session);
	}

	private void doReverseEffect(Session session) {
		if (this.involvedPayee != null) {
			involvedPayee.clearOpeningBalance();
			session.save(involvedPayee);
		} else if (this.involvedAccount != null) {
			involvedAccount.setOpeningBalance(0.00D);
			session.save(involvedAccount);
		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		if (this.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}
		return true;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = new HashMap<Account, Double>();
		for (TransactionItem e : transactionItems) {
			if (e == null) {
				try {
					throw new AccounterException(
							AccounterException.ERROR_TRANSACTION_ITEM_NULL);
				} catch (Exception e2) {
				}
			}
			map.put(e.getAccount(), e.getLineTotal());
		}
		return map;
	}

	public void setInvolvedPayee(Payee involvedPayee) {
		this.involvedPayee = involvedPayee;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.journalEntry()).gap();
		w.put(messages.journalEntryNo(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap().gap();
		w.put(messages.currency(), this.currencyFactor).gap().gap();

		w.put(messages.memo(), this.memo);

		w.put(messages.details(), this.transactionPayBills);
		w.put(messages.details(), this.transactionReceivePayments);

	}

	public Account getInvolvedAccount() {
		return involvedAccount;
	}

	public void setInvolvedAccount(Account involvedAccount) {
		this.involvedAccount = involvedAccount;
	}

}
