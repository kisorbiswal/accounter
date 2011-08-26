/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientStockTransfer implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ClientWarehouse fromWarehouse;
	private ClientWarehouse toWarehouse;

	private long id;

	private Set<ClientStockTransferItem> stockTransferItems;

	private int version;

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

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

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
		return this.id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientStockTransfer";
	}

	public ClientStockTransfer clone() {
		ClientStockTransfer stockTransfer = (ClientStockTransfer) this.clone();
		stockTransfer.fromWarehouse = this.fromWarehouse.clone();
		Set<ClientStockTransferItem> stockTransferItems = new HashSet<ClientStockTransferItem>();
		for (ClientStockTransferItem clientStockTransferItem : this.stockTransferItems) {
			stockTransferItems.add(clientStockTransferItem.clone());
		}
		stockTransfer.stockTransferItems = stockTransferItems;
		stockTransfer.toWarehouse = this.toWarehouse.clone();
		return stockTransfer;

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
