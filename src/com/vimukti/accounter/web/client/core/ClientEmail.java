package com.vimukti.accounter.web.client.core;

import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class ClientEmail implements IAccounterCore {

	public static final int TYPE_EMAIL_1 = 1;
	public static final int TYPE_EMAIL_2 = 2;
	public static final int TYPE_EMAIL_3 = 3;

	int type;
	String email = "";
	boolean isSelected = false;

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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	public LinkedHashMap<String, String> getEmailTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_EMAIL_1 + "", "Email1");
		valueMap.put(TYPE_EMAIL_2 + "", "Email2");
		valueMap.put(TYPE_EMAIL_3 + "", "Email3");

		return valueMap;

	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.EMAIL;
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
		return "ClientEmail";
	}

}
