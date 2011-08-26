package com.vimukti.accounter.core;

import java.util.LinkedHashMap;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Email implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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

	private int version;

	public Email() {

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
