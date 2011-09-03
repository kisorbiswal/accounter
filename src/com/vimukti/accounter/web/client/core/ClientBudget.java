package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.core.BudgetItem;

public class ClientBudget implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	private long id;

	private String budgetName;

	private int version;

	private BudgetItem budgetItem;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetname) {
		this.budgetName = budgetname;
	}

	public BudgetItem getBudgetItem() {
		return budgetItem;
	}

	public void setBudgetItem(BudgetItem budgetitem) {
		this.budgetItem = budgetitem;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

}
