package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Stock adjustment POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class StockAdjustment extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Warehouse wareHouse;

	private Set<StockAdjustmentItem> stockAdjustmentItems;

	@Override
	public String toString() {
		return null;
	}

	/**
	 * Calculates the total adjustment price based on the individual items
	 * adjustment prices.
	 * 
	 * @return sum of individual adjumentPrices.
	 */
	public double totalAdjustmentPrice() {
		double totalPrice = 0;
		for (StockAdjustmentItem item : stockAdjustmentItems) {
			totalPrice += item.getAdjustmentPriceValue();
		}
		return totalPrice;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	public Warehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	public Set<StockAdjustmentItem> getStockAdjustmentItems() {
		return stockAdjustmentItems;
	}

	public void setStockAdjustmentItems(
			Set<StockAdjustmentItem> stockAdjustmentItems) {
		this.stockAdjustmentItems = stockAdjustmentItems;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		for (StockAdjustmentItem item : stockAdjustmentItems) {
			item.setWarehouse(wareHouse);
		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		for (StockAdjustmentItem item : stockAdjustmentItems) {
			item.setWarehouse(wareHouse);
		}
		return super.onUpdate(session);
	}

	@Override
	public String getName() {
		return wareHouse.getName();
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getObjType() {
		return IAccounterCore.STOCK_ADJUSTMENT;
	}
}
