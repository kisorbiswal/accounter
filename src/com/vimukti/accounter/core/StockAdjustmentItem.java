package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class StockAdjustmentItem implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	// Price value at the time of transaction
	private double adjustmentPriceValue;
	private Quantity adjustmentQty;
	// comment is specific to this adjustment item (along with reason).
	private String comment;
	private Item item;
	// qtyBeforeTransaction is for further information, like report generation.
	private Quantity qtyBeforeTransaction;

	// private AdjustmentReason reason;
	private Warehouse wareHouse;

	public StockAdjustmentItem() {

	}

	/**
	 * Value at the time of transaction.
	 * 
	 * @return
	 */
	public double getAdjustmentPriceValue() {
		return adjustmentPriceValue;
	}

	public Quantity getAdjustmentQty() {
		return adjustmentQty;
	}

	public String getComment() {
		return comment;
	}

	public Item getItem() {
		return item;
	}

	public Quantity getQtyBeforeTransaction() {
		return qtyBeforeTransaction;
	}

	// public AdjustmentReason getReason() {
	// return reason;
	// }
	//
	// public Warehouse getWarehouse() {
	// return warehouse;
	// }

	/**
	 * Value at the time of adjustment
	 * 
	 * @param adjustmentPriceValue
	 */
	public void setAdjustmentPriceValue(double adjustmentPriceValue) {
		this.adjustmentPriceValue = adjustmentPriceValue;
	}

	public void setAdjustmentQty(Quantity adjustmentQty) {
		this.adjustmentQty = adjustmentQty;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setQtyBeforeTransaction(Quantity qtyBeforeTransaction) {
		this.qtyBeforeTransaction = qtyBeforeTransaction;
	}

	// public void setReason(AdjustmentReason reason) {
	// this.reason = reason;
	// }
	//
	// public void setWarehouse(Warehouse warehouse) {
	// this.warehouse = warehouse;
	// }

	public long getId() {
		return id;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

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
	public boolean onSave(Session s) throws CallbackException {
		double value = (getAdjustmentQty().getValue() * getAdjustmentQty()
				.getUnit().getFactor())
				/ getItem().getMeasurement().getDefaultUnit().getFactor();
		wareHouse.updateItemStatus(getItem(), value, false);
		s.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		double value = (getAdjustmentQty().getValue() * getAdjustmentQty()
				.getUnit().getFactor())
				/ getItem().getMeasurement().getDefaultUnit().getFactor();
		wareHouse.updateItemStatus(getItem(), value, true);
		s.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// TODO Auto-generated method stub

	}

	public Warehouse getWarehouse() {
		return wareHouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.wareHouse = warehouse;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
