package com.vimukti.accounter.core;

import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * @author Suresh Garikapati
 * 
 *         Transfer Fund
 * 
 *         Effect on Transfer From Account:
 * 
 *         If the finance Account isIncrease true then Increase it's current and
 *         total balance by transfer amount otherwise Decrease
 * 
 *         If the finance Account isIncrease true, then the amount in the Entry
 *         of JournalEntry must be in Credit side with the entry type Financial
 *         Account else the amount must be on Debit side.
 * 
 *         Effect on Transfer To Account:
 * 
 *         If the finance Account isIncrease true then Decrease it's current and
 *         total balance by transfer amount otherwise Increase
 * 
 *         If the finance Account isIncrease true, then the amount in the Entry
 *         of JournalEntry must be in Debit side with the entry type Financial
 *         Account else the amount must be on Credit side.
 * 
 * 
 */

public class TransferFund extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3319972479304421994L;
	/**
	 * The account from which we want to transfer the amount
	 */
	@ReffereredObject
	Account transferFrom;

	/**
	 * The account to which the amount is transfered.
	 */
	@ReffereredObject
	Account transferTo;

	@ReffereredObject
	JournalEntry journalEntry;

	//

	public TransferFund() {
		setType(Transaction.TYPE_TRANSFER_FUND);
	}

	/**
	 * @return the transferFrom
	 */
	public Account getTransferFrom() {
		return transferFrom;
	}

	/**
	 * @param transferFrom
	 *            the transferFrom to set
	 */
	public void setTransferFrom(Account transferFrom) {
		this.transferFrom = transferFrom;
	}

	/**
	 * @return the transferTo
	 */
	public Account getTransferTo() {
		return transferTo;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		super.onSave(session);
		if (this.getID() == 0l && !isDraftOrTemplate()) {
			Account account = this.transferFrom;
			account.updateCurrentBalance(this, this.total, currencyFactor);
			session.update(account);
			account.onUpdate(session);

			// JournalEntry journalEntry = new JournalEntry(this);

		}
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		// if (isBecameVoid()) {
		// this.transferFrom.updateCurrentBalance(this, -this.total);
		// session.update(this.transferFrom);
		// this.transferFrom.onUpdate(session);
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
		return this.transferTo;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_TRANSFER_FUND;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setTransferTo(Account trandferTo) {
		this.transferTo = trandferTo;
	}

	/**
	 * @return the journalEntry
	 */
	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	/**
	 * @param journalEntry
	 *            the journalEntry to set
	 */
	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_BANKING;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.getPayee();
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		TransferFund transferFund = (TransferFund) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		if (isDraftOrTemplate()) {
			super.onEdit(transferFund);
			return;
		}

		/**
		 * if present transaction is deleted or voided & previous transaction is
		 * not voided then only it will enter the loop
		 */

		if (this.isVoid() && !transferFund.isVoid()) {

			transferFund.effectAccount(session, transferFund.transferFrom,
					-transferFund.total);

		} else {

			if ((this.transferFrom.getID() == transferFund.transferFrom.getID())
					&& (this.transferTo.getID() == transferFund.transferTo
							.getID())
					&& !DecimalUtil.isEquals(this.total, transferFund.total)) {

				transferFund.total -= this.total;

				this.effectAccount(session, this.transferTo, transferFund.total);

				this.effectAccount(session, this.transferFrom,
						-transferFund.total);

			} else {

				if (this.transferFrom.getID() != transferFund.transferFrom
						.getID()) {

					transferFund.effectAccount(session,
							transferFund.transferFrom, -transferFund.total);

					this.effectAccount(session, this.transferFrom, this.total);
				}

				if (this.transferTo.getID() != transferFund.transferTo.getID()) {

					transferFund.effectAccount(session,
							transferFund.transferTo, transferFund.total);

					this.effectAccount(session, this.transferTo, -this.total);
				}
			}
		}

		super.onEdit(transferFund);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && !isDraft()) {
			this.effectAccount(session, this.transferFrom, -this.total);
		}
		return super.onDelete(session);
	}

	private void effectAccount(Session session, Account transferFrom,
			double amount) {

		Account account = (Account) session.get(Account.class,
				transferFrom.getID());

		account.updateCurrentBalance(this, amount, currencyFactor);
		session.update(account);
		account.onUpdate(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		TransferFund transferFund = (TransferFund) clientObject;

		if (!UserUtils.canDoThis(TransferFund.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		/**
		 * If Transfer Fund is already void or deleted we can't edit it
		 */
		if (this.isVoid() && !transferFund.isVoid()) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
			// "Transfer Fund is already voided or deleted we can't Edit");
		}

		return super.canEdit(clientObject);
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		map.put(transferFrom, total);
		return map;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePayee(boolean onCreate) {
		// TODO Auto-generated method stub

	}

}
