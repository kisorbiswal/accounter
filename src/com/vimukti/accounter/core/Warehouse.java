/**
 * 
 */
package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Warehouse POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class Warehouse extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 640523202925694992L;

	private Address address;
	private Set<ItemStatus> itemStatuses;

	private String name;
	private String warehouseCode;
	private Contact contact;
	private boolean isDefaultWarehouse;

	private transient boolean isOnSaveProccessed;

	public Warehouse(String warehouseCode, String name, Address address,
			boolean isDefault) {
		this.name = name;
		this.warehouseCode = warehouseCode;
		this.isDefaultWarehouse = isDefault;
		this.address = address;
	}

	public Warehouse() {

	}

	public Address getAddress() {
		return address;
	}

	public Set<ItemStatus> getItemStatuses() {
		return itemStatuses;
	}

	public String getName() {
		return name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setItemStatuses(Set<ItemStatus> itemStatuses) {
		this.itemStatuses = itemStatuses;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return false;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(s);
		isOnSaveProccessed = true;

		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		super.onUpdate(s);
		return false;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setObjectType(AccounterCoreType.WAREHOUSE);
		ChangeTracker.put(accounterCore);

		return false;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onLoad(Session s, Serializable id) {
	}

	public boolean isDefaultWarehouse() {
		return isDefaultWarehouse;
	}

	public void setDefaultWarehouse(boolean isDefaultWarehouse) {
		this.isDefaultWarehouse = isDefaultWarehouse;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public void updateItemStatus(Item item, Quantity quantity, boolean substract) {
		ItemStatus itemStatus = getItemStatus(item);
		if (itemStatus != null) {
			Quantity tempQ = itemStatus.getQuantity();
			if (substract) {
				itemStatus.getQuantity().setValue(
						tempQ.getValue() - quantity.getValue());
			} else {
				itemStatus.getQuantity().setValue(
						tempQ.getValue() + quantity.getValue());
			}
		}
	}

	public ItemStatus getItemStatus(Item item) {
		for (ItemStatus itemStatus : itemStatuses) {
			if (itemStatus.getItem().equals(item)) {
				return itemStatus;
			}
		}
		return null;
	}
}
