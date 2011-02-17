package com.vimukti.accounter.web.client.core;

import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class ClientFax implements IAccounterCore {

	public static final int TYPE_BUSINESS = 1;
	public static final int TYPE_HOME = 2;
	public static final int TYPE_OTHER = 3;

	int type;

	String number = "";

	boolean isSelected = false;

	/**
	 * @return the id
	 */

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public LinkedHashMap<String, String> getFaxTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_BUSINESS + "", "Company");
		valueMap.put(TYPE_HOME + "", "Home");
		valueMap.put(TYPE_OTHER + "", "Other");
		return valueMap;

	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.FAX;
	}

	@Override
	public String getStringID() {
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}
}
