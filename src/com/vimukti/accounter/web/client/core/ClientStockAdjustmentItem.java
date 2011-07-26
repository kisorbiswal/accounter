/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockAdjustmentItem implements Serializable, IsSerializable {

	private long id;
	// Price value at the time of transaction
	private double adjustmentPriceValue;
	private ClientQuantity adjustmentQty;
	// comment is specific to this adjustment item (along with reason).
	private String comment;
	private ClientItem item;
	// qtyBeforeTransaction is for further information, like report generation.
	private ClientQuantity qtyBeforeTransaction;
	private ClientAdjustmentReason reason;
	private ClientWarehouse warehouse;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the adjustmentPriceValue
	 */
	public double getAdjustmentPriceValue() {
		return adjustmentPriceValue;
	}

	/**
	 * @param adjustmentPriceValue
	 *            the adjustmentPriceValue to set
	 */
	public void setAdjustmentPriceValue(double adjustmentPriceValue) {
		this.adjustmentPriceValue = adjustmentPriceValue;
	}

	/**
	 * @return the adjustmentQty
	 */
	public ClientQuantity getAdjustmentQty() {
		return adjustmentQty;
	}

	/**
	 * @param adjustmentQty
	 *            the adjustmentQty to set
	 */
	public void setAdjustmentQty(ClientQuantity adjustmentQty) {
		this.adjustmentQty = adjustmentQty;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the item
	 */
	public ClientItem getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(ClientItem item) {
		this.item = item;
	}

	/**
	 * @return the qtyBeforeTransaction
	 */
	public ClientQuantity getQtyBeforeTransaction() {
		return qtyBeforeTransaction;
	}

	/**
	 * @param qtyBeforeTransaction
	 *            the qtyBeforeTransaction to set
	 */
	public void setQtyBeforeTransaction(ClientQuantity qtyBeforeTransaction) {
		this.qtyBeforeTransaction = qtyBeforeTransaction;
	}

	/**
	 * @return the reason
	 */
	public ClientAdjustmentReason getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(ClientAdjustmentReason reason) {
		this.reason = reason;
	}

	/**
	 * @return the warehouse
	 */
	public ClientWarehouse getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(ClientWarehouse warehouse) {
		this.warehouse = warehouse;
	}

}
