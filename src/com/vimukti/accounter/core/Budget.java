package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Budget implements IAccounterServerCore {

	private static final long serialVersionUID = 1L;

	private long id;

	private String budgetName;

	private BudgetItem budgetItem;

	private int version;

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
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}
}
