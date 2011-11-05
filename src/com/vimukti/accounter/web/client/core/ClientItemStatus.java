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
public class ClientItemStatus implements Serializable, IsSerializable,
		Cloneable {

	private long id;

	private long item;

	private ClientQuantity quantity;

	public long warehouse;

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
	public long getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(long item) {
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
	public long getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(long warehouse) {
		this.warehouse = warehouse;
	}

	public ClientItemStatus clone() {
		ClientItemStatus clientItemStatusClone = (ClientItemStatus) this
				.clone();
		return clientItemStatusClone;
	}

}
