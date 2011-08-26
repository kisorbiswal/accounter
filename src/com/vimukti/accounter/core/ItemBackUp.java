package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author vimukti16 <br/>
 * <br/>
 * 
 *         This can store the Back up of each Item and it's internal linked
 *         Accounts which are participated in any Transaction.
 * 
 */
public class ItemBackUp implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	Item item;

	Account incomeAccount;

	Account expenseAccount;

	private int version;

	public ItemBackUp() {

	}

	public ItemBackUp(TransactionItem transactionItem) {
		this();
		this.item = transactionItem.getItem();
		this.incomeAccount = transactionItem.getItem().getIncomeAccount();
		this.expenseAccount = transactionItem.getItem().getExpenseAccount();
	}

	public long getID() {
		return id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Account getIncomeAccount() {
		return incomeAccount;
	}

	public void setIncomeAccount(Account incomeAccount) {
		this.incomeAccount = incomeAccount;
	}

	public Account getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(Account expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// NOTHING TO DO.
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}
