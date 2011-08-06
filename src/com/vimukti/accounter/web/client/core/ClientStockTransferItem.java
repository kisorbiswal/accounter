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
public class ClientStockTransferItem implements Serializable, IsSerializable,
		Cloneable {

	private ClientItem item;
	private ClientQuantity quantity;
	private String memo;

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
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public ClientStockTransferItem clone() {
		ClientStockTransferItem stockTransferItem = (ClientStockTransferItem) this
				.clone();
		stockTransferItem.item = this.item.clone();
		stockTransferItem.quantity = this.quantity.clone();
		return stockTransferItem;
	}

}
