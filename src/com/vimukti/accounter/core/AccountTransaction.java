package com.vimukti.accounter.core;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * A back-up class to maintain all the records of updating the account. This has
 * the references of {@link Account}, {@link Transaction}, amount that was
 * effected, transaction date etc.
 * 
 * @author Chandan
 * 
 */
public class AccountTransaction extends CreatableObject implements
		IAccounterServerCore, IsSerializable, Serializable {

	Logger log = Logger.getLogger(AccountTransaction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This reference is maintained to know from what transaction the
	 * AccountTransaction reference is created and stored.
	 */
	Transaction transaction;
	/**
	 * On this account the Account Transaction table is updated. For every
	 * different account, not depending on the transaction an entry is recorded
	 * here with the given amount.
	 */
	Account account;
	/**
	 * By this amount the account changes the Account Transaction through the
	 * specified transaction.
	 */
	double amount;
	/**
	 * This is used to indicate the end of fiscal year. If true, it indicates
	 * that the fiscal year is being closed and we have to add the closing
	 * fiscal year entry.
	 */
	boolean closingFYEntry = false;
	/**
	 * This is maintained to avoid unnecessary confusion with the Accrual
	 * entries and Cashbasis journal entries.
	 */
	boolean cashBasisEntry = false;

	public AccountTransaction() {
	}

	public AccountTransaction(Account account, Transaction transaction,
			double amount, boolean closingFYEntry, boolean cashBasisEntry) {
		setCompany(transaction.getCompany());
		this.account = account;
		this.transaction = transaction;
		this.amount = amount;
		this.closingFYEntry = closingFYEntry;
		this.cashBasisEntry = cashBasisEntry;

		// CompanyPreferences preferences =
		// Company.getCompany().getPreferences();
		// if (this.transaction.transactionDate.getYear() == preferences
		// .getStartOfFiscalYear().getYear()) {

		int key = (transaction.getDate().getYear() + 1900) * 100
				+ transaction.getDate().getMonth();
		if (this.account.monthViceAmounts.containsKey(key)) {
			this.account.monthViceAmounts.put(key,
					this.account.monthViceAmounts.get(key) + amount);
		} else {
			this.account.monthViceAmounts.put(key, amount);
		}
		// }
	}

	@Override
	public String toString() {
		if (transaction != null) {
			return getAccount().getName() + "   " + transaction.toString()
					+ "    " + amount;
		} else
			return "";
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	// public void setTransaction(Transaction transaction) {
	// this.transaction = transaction;
	// }
	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	// public void setAccount(Account account) {
	// this.account = account;
	// }
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param newAmount
	 *            the amount to set
	 */
	// public void setAmount(double amount) {
	// this.amount = amount;
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.accounttransaction()).gap();

		w.put(messages.amount(), this.amount);

		if (this.transaction != null)
			w.put(messages.transactionAmount(), this.transaction.getNetAmount());

		if (this.account != null)
			w.put(messages.account(), this.account.getName());

		w.put(messages.closeFiscalYear(), closingFYEntry);

		w.put(messages.cashBasisAccounting(), this.cashBasisEntry);

	}
}
