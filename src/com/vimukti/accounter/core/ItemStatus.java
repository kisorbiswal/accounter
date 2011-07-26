/**
 * 
 */
package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * It maintains the status of an item.
 * 
 * @author Srikanth.J
 * 
 */
public class ItemStatus implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3127954338713524771L;
	private long id;
	
	private Item item;
	private Quantity quantity;


	public Warehouse warehouse;

	public ItemStatus() {
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
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

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
