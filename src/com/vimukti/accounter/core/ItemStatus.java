/**
 * 
 */
package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * It maintains the status of an item.
 * 
 * @author Srikanth.J
 * 
 */
public class ItemStatus implements IAccounterServerCore {

	private static final long serialVersionUID = -3127954338713524771L;

	private long id;

	private Item item;
	private Quantity quantity;

	public Warehouse warehouse;

	private int version;

	public ItemStatus() {
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	public Item getItem() {
		return item;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public long getID() {
		return id;
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

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}
}
