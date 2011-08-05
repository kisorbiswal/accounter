/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author vimukti43
 * 
 */
@SuppressWarnings("serial")
public class ClientVATReturnBox implements IAccounterCore {

	long id;

	String name;
	String vatBox;
	String totalBox;

	int vatReturnType;

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

	/**
	 * 
	 */
	public ClientVATReturnBox() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getClientClassSimpleName
	 * ()
	 */
	@Override
	public String getClientClassSimpleName() {
		return "ClientVATReturnBox";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VATRETURNBOX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;
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

	public ClientVATReturnBox clone() {
		return null;
	}

}
