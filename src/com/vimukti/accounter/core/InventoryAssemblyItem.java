package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class InventoryAssemblyItem implements IAccounterServerCore {

	private long id;

	private int version;

	private Item inventoryItem;

	private Quantity quantity;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
