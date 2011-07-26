package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.classic.Lifecycle;

/**
 * Transfers items from one warehouse to another.
 * 
 * @author Srikanth J
 * 
 */
public class StockTransfer extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711393907542349978L;

	private Warehouse fromWarehouse;
	private Warehouse toWarehouse;

	private Set<StockTransferItem> stockTransferItems;

	public StockTransfer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Warehouse getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(Warehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public Warehouse getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(Warehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public void setStockTransferItems(Set<StockTransferItem> stockTransferItems) {
		this.stockTransferItems = stockTransferItems;
	}

	public Set<StockTransferItem> getStockTransferItems() {
		return stockTransferItems;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

}
