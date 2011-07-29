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

	/**
	 * Used to tell that same number is already used for another Transaction or
	 * Core object
	 */
	public static final int ERROR_NUMBER_CONFLICT = 1;
	/**
	 * Used to tell that same name is already used for another core object
	 */
	public static final int ERROR_NAME_CONFLICT = 2;
	/**
	 * Used to tell that this transaction is already done, and can not be done
	 * again
	 */
	public static final int ERROR_TRAN_CONFLICT = 3;

	/**
	 * Sent when user does not have sufficient permission to do this operation
	 */
	public static final int ERROR_PERMISSION_DENIED = 4;
	

	protected int errorCode;

	// private long id;

	/**
	 * Creates new Instance
	 */
	public AccounterException() {
	}

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
	// public void setID(long id) {
	// this.id = id;
	// }

	/**
	 * @return
	 */
	// public long getID() {
	// return this.id;
	// }
}
