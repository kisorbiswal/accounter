package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientBudget implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	private long id;

	private String budgetName;

	private int version;

	List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();

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

	public List<ClientBudgetItem> getBudgetItem() {
		return budgetItems;
	}

	public void setBudgetItem(List<ClientBudgetItem> budgetitems) {
		this.budgetItems = budgetitems;
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
