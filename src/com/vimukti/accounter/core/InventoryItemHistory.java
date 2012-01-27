package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class InventoryItemHistory implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Item item;
	private double cost;
	private int version;
	private Transaction transaction;

	public InventoryItemHistory() {
	}

	public InventoryItemHistory(Item item, double cost, Transaction transaction) {
		this.item = item;
		this.cost = cost;
		this.transaction = transaction;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
