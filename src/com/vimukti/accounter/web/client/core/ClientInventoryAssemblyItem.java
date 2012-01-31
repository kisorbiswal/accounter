package com.vimukti.accounter.web.client.core;

public class ClientInventoryAssemblyItem implements IAccounterCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3588314606540831574L;

	private long id;

	private int version;

	private ClientItem inventoryItem;

	private ClientQuantity quantity;

	private Double unitPrice;

	private String discription;

	private long warehouse;

	private double linetotal;

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
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ClientItem getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(ClientItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public ClientQuantity getQuantity() {
		return quantity;
	}

	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setDescription(String discription) {
		this.discription = discription;
	}

	public String getDiscription() {
		return discription;
	}

	public void setWareHouse(long warehouse) {
		this.warehouse = warehouse;
	}

	public long getWarehouse() {
		return warehouse;
	}

	public void setLineTotal(double linetotal) {
		this.linetotal = linetotal;
	}

	public double getLineTotal() {
		return linetotal;
	}

	public boolean isEmpty() {
		if ((this.inventoryItem == null && getID() == 0)
				&& this.unitPrice == null
				&& (this.linetotal == 0)
				&& (this.quantity == null || this.quantity.getValue() == 0 || this.quantity
						.getValue() == 1)) {
			return true;
		}
		return false;
	}

}
