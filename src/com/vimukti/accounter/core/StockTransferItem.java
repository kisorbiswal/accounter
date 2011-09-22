package com.vimukti.accounter.core;

public class StockTransferItem {

	private Company company;
	private long id;
	private Item item;
	private Quantity quantity;
	private String memo;

	public StockTransferItem() {
		super();
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

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
