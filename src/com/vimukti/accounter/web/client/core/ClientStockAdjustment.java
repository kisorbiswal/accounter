package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientStockAdjustment implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	int version;

	private long wareHouse;

	private List<ClientStockAdjustmentItem> stockAdjustmentItems;

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
		return AccounterCoreType.STOCK_ADJUSTMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientStockAdjustment";
	}

	public List<ClientStockAdjustmentItem> getStockAdjustmentItems() {
		return stockAdjustmentItems;
	}

	public void setStockAdjustmentItems(
			List<ClientStockAdjustmentItem> stockAdjustmentItems) {
		this.stockAdjustmentItems = stockAdjustmentItems;
	}

	public long getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(long wareHouse) {
		this.wareHouse = wareHouse;
	}

}
