package com.vimukti.accounter.web.client.core;

/**
 * 
 * @author vimukti16 <br/>
 * <br/>
 * 
 *         This can store the Back up of each Item and it's internal linked
 *         Accounts which are participated in any Transaction.
 * 
 */

public class ClientItemBackUp implements IAccounterCore {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public String stringID;

	String transactionItem;

	String item;

	String incomeAccount;

	String expenseAccount;

	public ClientItemBackUp() {
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public String getTransactionItem() {
		return transactionItem;
	}

	public void setTransactionItem(String transactionItem) {
		this.transactionItem = transactionItem;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ITEMBACKUP;
	}

}
