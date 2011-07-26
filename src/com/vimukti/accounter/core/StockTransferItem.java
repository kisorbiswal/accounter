package com.vimukti.accounter.core;

public class StockTransferItem {

	private Item item;
	private Quantity quantity;
	private String memo;

	public StockTransferItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Item getItem() {
		return item;
	}

	public String getMemo() {
		return memo;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

}
