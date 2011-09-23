package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author vimukti16 This is the class where we kept all UK VAT related Box
 *         information.
 * 
 * 
 */

public class VATReturnBox extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8208080531370075833L;
	String name;
	String vatBox;
	String totalBox;

	int vatReturnType;

	/**
	 * 
	 * //rajesh.b@skyessmail.com
	 */
	public VATReturnBox() {
		// TODO Auto-generated constructor stub
	}

	public VATReturnBox(Company company) {
		setCompany(company);
	}

	/**
	 * @return the vatReturnType
	 */
	public int getVatReturnType() {
		return vatReturnType;
	}

	/**
	 * @param vatReturnType
	 *            the vatReturnType to set
	 */
	public void setVatReturnType(int vatReturnType) {
		this.vatReturnType = vatReturnType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the vatBox
	 */
	public String getVatBox() {
		return vatBox;
	}

	/**
	 * @param vatBox
	 *            the vatBox to set
	 */
	public void setVatBox(String vatBox) {
		this.vatBox = vatBox;
	}

	/**
	 * @return the totalBox
	 */
	public String getTotalBox() {
		return totalBox;
	}

	/**
	 * @param totalBox
	 *            the totalBox to set
	 */
	public void setTotalBox(String totalBox) {
		this.totalBox = totalBox;
	}

	transient private boolean isOnSaveProccessed;

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}
}
