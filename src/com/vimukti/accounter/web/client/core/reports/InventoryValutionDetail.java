package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InventoryValutionDetail extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int transType;

	private long transactionId;

	private long transactionDate;

	private String payeeName;

	private long transactionNo;

	private long quantity;

	private double cost;

	private long onHand;

	private double assetValue;

	private String itemName;

	private long payeeId;

	private long itemId;

	public int getTransType() {
		return transType;
	}

	public void setTransType(int transType) {
		this.transType = transType;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public long getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(long transactionNo) {
		this.transactionNo = transactionNo;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public long getOnHand() {
		return onHand;
	}

	public void setOnHand(long onHand) {
		this.onHand = onHand;
	}

	public double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

}
