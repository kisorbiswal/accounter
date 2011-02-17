package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientAccountTransaction implements IAccounterCore {

	String stringID;
	String transaction;
	String account;
	double amount;

	public ClientAccountTransaction() {
	}

	public ClientAccountTransaction(String account,
			ClientTransaction transaction, Double amount) {
		this.account = account;
		this.transaction = transaction.getStringID();
		this.amount = amount;

	}

	@Override
	public String toString() {
		if (transaction != null) {
			return "   " + transaction.toString() + "    " + amount;
		} else
			return "";
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @return the transaction
	 */
	public String getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String getClientClassSimpleName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ACCOUNTTRANSACTION;
	}

	@Override
	public String getStringID() {
		return this.stringID;

	}

	@Override
	public void setStringID(String stringID) {

		this.stringID = stringID;

	}

}
