/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockTransfer implements
		Serializable, IsSerializable {

	private ClientWarehouse fromWarehouse;
	private ClientWarehouse toWarehouse;

	private long id;
	
	private Set<ClientStockTransferItem> stockTransferItems;

	

	public long getId() {
		return id;
	}
	
	/**
	 * @return the fromWarehouse
	 */
	public ClientWarehouse getFromWarehouse() {
		return fromWarehouse;
	}

	/**
	 * @param fromWarehouse
	 *            the fromWarehouse to set
	 */
	public void setFromWarehouse(ClientWarehouse fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	/**
	 * @return the toWarehouse
	 */
	public ClientWarehouse getToWarehouse() {
		return toWarehouse;
	}

	/**
	 * @param toWarehouse
	 *            the toWarehouse to set
	 */
	public void setToWarehouse(ClientWarehouse toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	/**
	 * @return the stockTransferItems
	 */
	public Set<ClientStockTransferItem> getStockTransferItems() {
		return stockTransferItems;
	}

	/**
	 * @param stockTransferItems
	 *            the stockTransferItems to set
	 */
	public void setStockTransferItems(
			Set<ClientStockTransferItem> stockTransferItems) {
		this.stockTransferItems = stockTransferItems;
	}

}
