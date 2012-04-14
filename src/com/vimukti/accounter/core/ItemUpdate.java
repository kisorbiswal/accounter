package com.vimukti.accounter.core;

public class ItemUpdate extends CreatableObject {

	private Item item;
	private Quantity quantity;
	private double unitPrice;
	private Warehouse warehouse;

	public ItemUpdate() {
		// TODO Auto-generated constructor stub
	}

	public ItemUpdate(Item item, Quantity qty, double unitPrice,
			Warehouse warehouse) {
		this.item = item;
		this.quantity = qty;
		this.unitPrice = unitPrice;
		this.warehouse = warehouse;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public void add(ItemUpdate itemUpdate) {
		this.quantity.add(itemUpdate.getQuantity());
	}

}
