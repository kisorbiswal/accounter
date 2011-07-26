/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.core.Item;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientItemStatus implements Serializable, IsSerializable {

	private long id;

	private Item item;

	private ClientQuantity quantity;

	public ClientWarehouse warehouse;

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
	public ClientQuantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(ClientQuantity quantity) {
		this.quantity = quantity;
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
