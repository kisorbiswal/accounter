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
public class ClientStockTransfer extends ClientTransaction implements
		Serializable, IsSerializable {

	private ClientWarehouse fromWarehouse;
	private ClientWarehouse toWarehouse;

	private Set<ClientStockTransferItem> stockTransferItems;

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.STOCK_TRANSFER;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientStockTransfer";
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
