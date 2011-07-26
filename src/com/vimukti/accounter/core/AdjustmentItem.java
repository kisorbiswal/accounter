package com.vimukti.accounter.core;

public class AdjustmentItem {

	// Price value at the time of transaction
	private double adjustmentPriceValue;
	private Quantity adjustmentQty;
	// comment is specific to this adjustment item (along with reason).
	private String comment;
	private Item item;
	// qtyBeforeTransaction is for further information, like report generation.
	private Quantity qtyBeforeTransaction;
	private AdjustmentReason reason;
	private Warehouse warehouse;

	public AdjustmentItem() {

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

	public AdjustmentReason getReason() {
		return reason;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

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

	public void setReason(AdjustmentReason reason) {
		this.reason = reason;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

}
