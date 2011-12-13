package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomField extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;

	boolean showCustomer;
	boolean showVendor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCustomer() {
		return showCustomer;
	}

	public void setShowCustomer(boolean showCustomer) {
		this.showCustomer = showCustomer;
	}

	public boolean isShowVendor() {
		return showVendor;
	}

	public void setShowVendor(boolean showVendor) {
		this.showVendor = showVendor;
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}
}
