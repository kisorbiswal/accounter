package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class Email implements IAccounterServerCore {

	public static final int TYPE_EMAIL_1 = 1;
	public static final int TYPE_EMAIL_2 = 2;
	public static final int TYPE_EMAIL_3 = 3;

	/**
	 * this will specify the type of the Email it is.
	 */
	int type = 0;
	/**
	 * this will hold the Email id.
	 */
	String email = "";
	/**
	 * This is to specify which Email of the {@link Payee} is being selected .
	 */
	boolean isSelected = false;

	transient boolean isImported;

	public Email() {
		// TODO

	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the isSelected
	 */
	public boolean getIsSelected() {
		return isSelected;
	}

	public LinkedHashMap<String, String> getEmailTypes() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put(TYPE_EMAIL_1 + "", "Email1");
		valueMap.put(TYPE_EMAIL_2 + "", "Email2");
		valueMap.put(TYPE_EMAIL_3 + "", "Email3");

		return valueMap;

	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// this.stringID = stringID;

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
