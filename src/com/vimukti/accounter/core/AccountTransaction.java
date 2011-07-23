package com.vimukti.accounter.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * 
 * A back-up class to maintain all the records of updating the account. This has
 * the references of {@link Account}, {@link Transaction}, amount that was
 * effected, transaction date etc.
 * 
 * @author Chandan
 * 
 */
@SuppressWarnings("serial")
public class AccountTransaction implements IAccounterServerCore,
		IsSerializable, Serializable, ICreatableObject {

	long id;
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
	public long id;

	transient boolean isImported;
	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;
	FinanceDate tempTransactionDate;

	public AccountTransaction() {
		this.stringID = SecureUtils.createID();
	}

	@SuppressWarnings("deprecation")
	public AccountTransaction(Account account, Transaction transaction,
			double amount, boolean closingFYEntry, boolean cashBasisEntry) {

		this.stringID = SecureUtils.createID();
		this.account = account;
		this.transaction = transaction;
		this.amount = amount;
		this.closingFYEntry = closingFYEntry;
		this.cashBasisEntry = cashBasisEntry;

		tempTransactionDate = transaction.clonedTransactionDate != null ? transaction.clonedTransactionDate
				: transaction.transactionDate;

		FinanceLogger.log(
				"AccountTransaction Entry has been created for effect "
						+ " of {0} on Account {1} with amount : {2} ", Utility
						.getTransactionName(transaction.getType()), account
						.getName(), String.valueOf(amount));
		// CompanyPreferences preferences =
		// Company.getCompany().getPreferences();
		// if (this.transaction.transactionDate.getYear() == preferences
		// .getStartOfFiscalYear().getYear()) {

		FinanceLogger.log(
				"{0} Monthly wise amounts has been update with amount: {1} ",
				account.getName(), String.valueOf(amount));
		int key = (tempTransactionDate.getYear() + 1900) * 100
				+ tempTransactionDate.getMonth();
		if (this.account.monthViceAmounts.containsKey(key)) {
			this.account.monthViceAmounts.put(key,
					(Double) this.account.monthViceAmounts
							.get(key)
							+ amount);
		} else {
			this.account.monthViceAmounts.put(key, amount);
		}
		// }
	}

	public boolean equals(AccountTransaction at) {

		if (this.id == at.id && DecimalUtil.isEquals(this.amount, at.amount)
				&& this.transaction.equals(at.transaction)
				&& this.account.equals(at.account))
			return true;
		return false;

	}

	@Override
	public String toString() {
		if (transaction != null) {
			return account.name + "   " + transaction.toString() + "    "
					+ amount;
		} else
			return "";
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
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

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setCreatedDate(FinanceDate createdDate) {
		this.createdDate = createdDate;
	}

	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	public void setLastModifiedDate(FinanceDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public FinanceDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setImported(boolean isImported) {

	}

	@Override
	public void setID(long id){
		this.id=id;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	// public void setAmount(double amount) {
	// this.amount = amount;
	// }

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}
}
