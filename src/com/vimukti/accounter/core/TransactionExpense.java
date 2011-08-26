package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class TransactionExpense implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4683365805457055214L;
	public static final int TYPE_ITEM = 1;
	public static final int TYPE_ACCOUNT = 4;

	long id;

	int type;

	/**
	 * To indicate that the TransactionExpense is the Item type
	 */
	Item item;

	/**
	 * To indicate that the TransactionExpense is the Account type
	 */
	Account account;

	String description;

	/**
	 * The quantity of the TransactionExpense
	 */
	double quantity;

	/**
	 * The unit price of the TransactionExpense. This is multiplied by quantity
	 * to give amount.
	 */
	double unitPrice;

	/**
	 * Obtained by the product of quantity and unitprice
	 */
	double amount;

	/**
	 * This is the reference to Expense which holds all the TransactionExpenses
	 */
	Expense expense;

	transient private boolean isOnSaveProccessed;
	private int version;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the expense
	 */
	public Expense getExpense() {
		return expense;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @param lineTotal
	 *            the lineTotal to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param expense
	 *            the expense to set
	 */
	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable id) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (this.expense.getStatus() == Expense.STATUS_APPROVED
				&& this.expense.isAuthorised()) {

			Account effectingAccount = this.getEffectingAccount();
			if (effectingAccount != null) {
				effectingAccount.updateCurrentBalance(this.expense, amount);

				session.update(effectingAccount);

			}

		}
		// ChangeTracker.put(this);
		return false;
	}

	private Account getEffectingAccount() {

		switch (this.type) {

		case TransactionExpense.TYPE_ACCOUNT:

			return this.account;

		case TransactionExpense.TYPE_ITEM:

			return this.item.getExpenseAccount();

		}
		return null;
	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

}
