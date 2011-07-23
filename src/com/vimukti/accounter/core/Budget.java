package com.vimukti.accounter.core;


public class Budget {

	long id;

	/**
	 * The month for which we are planning to make budget.
	 */
	int month;

	/**
	 * The amount by which we create the budget for the specific period.
	 */
	double amount;

	/**
	 * The account in which we are making the budget amount to be deposited.
	 */
	Account account;

	public long id;

	transient boolean isImported;
	String createdBy;
	String lastModifier;
	String createdDate;
	String lastModifiedDate;

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
	public void setID(long id){
		this.id = id;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

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
	public void setAccount(Account account) {
		this.account = account;
	}

}
