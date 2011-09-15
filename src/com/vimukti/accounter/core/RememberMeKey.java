/**
 * 
 */
package com.vimukti.accounter.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class RememberMeKey extends CreatableObject {

	private String key;

	private String emailID;

	/**
	 * Creates new Instance
	 */
	public RememberMeKey() {
	}

	/**
	 * Creates new Instance
	 */
	public RememberMeKey(String emailId, String key) {
		this.emailID = emailId;
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the emailID
	 */
	public String getEmailID() {
		return emailID;
	}

	/**
	 * @param emailID
	 *            the emailID to set
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

}
