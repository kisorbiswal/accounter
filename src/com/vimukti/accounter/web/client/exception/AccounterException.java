/**
 * 
 */
package com.vimukti.accounter.web.client.exception;

/**
 * @author Prasanna Kumar G
 * 
 */
@SuppressWarnings("serial")
public class AccounterException extends Exception {

	protected int errorCode;
	private long id;

	/**
	 * Creates new Instance
	 */
	public AccounterException(String message) {
		super(message);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(Throwable t, String message) {
		super(message, t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterException(Throwable t) {
		super(t);
	}

	/**
	 * @return
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param id
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public long getID() {
		return this.id;
	}
}
