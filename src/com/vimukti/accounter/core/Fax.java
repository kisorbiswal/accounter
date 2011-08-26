package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Fax implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_HOME = 2;
	public static final int TYPE_OTHER = 3;

	int type = 0;
	String number = "";
	boolean isSelected = false;

	private int version;

	public Fax() {
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	public LinkedHashMap<String, String> getFaxTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_BUSINESS + "", "Business");
		valueMap.put(TYPE_HOME + "", "Home");
		valueMap.put(TYPE_OTHER + "", "Other");
		return valueMap;

	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
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
