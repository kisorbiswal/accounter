package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class Phone implements IAccounterServerCore {

	public static final int BUSINESS_PHONE_NUMBER = 1;
	public static final int MOBILE_PHONE_NUMBER = 2;
	public static final int HOME_PHONE_NUMBER = 3;
	public static final int ASSISTANT_PHONE_NUMBER = 4;
	public static final int OTHER_PHONE_NUMBER = 5;

	int type = 0;
	String number = "";
	boolean isSelected = false;

	transient boolean isImported;

	public Phone() {

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

	public LinkedHashMap<String, String> getPhoneTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(BUSINESS_PHONE_NUMBER + "", "Business");
		valueMap.put(MOBILE_PHONE_NUMBER + "", "Mobile");
		valueMap.put(HOME_PHONE_NUMBER + "", "Home");
		valueMap.put(ASSISTANT_PHONE_NUMBER + "", "Assistant");
		valueMap.put(OTHER_PHONE_NUMBER + "", "Other");
		return valueMap;

	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id){

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
