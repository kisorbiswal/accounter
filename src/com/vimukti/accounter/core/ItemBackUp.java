package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.InvalidOperationException;

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

	public long id;

	TransactionItem transactionItem;

	Item item;

	Account incomeAccount;

	Account expenseAccount;

	public ItemBackUp() {

	}

	public ItemBackUp(TransactionItem transactionItem) {
		this();
		this.transactionItem = transactionItem;
		this.item = transactionItem.getItem();
		this.incomeAccount = transactionItem.getItem().getIncomeAccount();
		this.expenseAccount = transactionItem.getItem().getExpenseAccount();
	}

	@Override
	public long getID(){
		return this.id;
	}

	@Override
	public void setImported(boolean isImported) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setID(long id){
		this.id=id;
	}

	public long getId() {
		return id;
	}

	public void setID(long id){
		this.id = id;
	}

	public TransactionItem getTransactionItem() {
		return transactionItem;
	}

	public void setTransactionItem(TransactionItem transactionItem) {
		this.transactionItem = transactionItem;
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
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		this.stringID = this.stringID == null || this.stringID != null
				&& this.stringID.isEmpty() ? SecureUtils.createID()
				: this.stringID;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

}
