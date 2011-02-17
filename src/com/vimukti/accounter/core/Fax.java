package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class Fax implements IAccounterServerCore {

	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_HOME = 2;
	public static final int TYPE_OTHER = 3;

	int type = 0;
	String number = "";
	boolean isSelected = false;

	transient boolean isImported;

	public Fax() {
		// TODO
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
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringID(String stringID) {

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
