/**
 * 
 */
package com.vimukti.accounter.web.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterException extends Exception implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	/**
	 * Used to tell that Exception in Database
	 */
	public static final int ERROR_INTERNAL = 5;

	/**
	 * Sent When Arguments given by the Client is Wrong
	 */
	public static final int ERROR_ILLEGAL_ARGUMENT = 6;
	public static final int ERROR_INVALID_USER_SESSION = 8;

	/**
	 * Used to tell that the object was deleted already.
	 */
	public static final int ERROR_NO_SUCH_OBJECT = 9;

	/**
	 * You can't void or edit because it has been deposited from Undeposited
	 * Funds
	 */
	public static final int ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS = 10;

	/**
	 * You can't Edit it
	 */
	public static final int ERROR_CANT_EDIT = 11;

	/**
	 * You can't void it
	 */
	public static final int ERROR_CANT_VOID = 12;

	/**
	 * In the ReceivePayment writeoff or discount is used for this Invoice.
	 */
	public static final int ERROR_RECEIVE_PAYMENT_DISCOUNT_USED = 13;

	public static final int ERROR_OBJECT_IN_USE = 14;
	
	public static final int ERROR_VERSION_MISMATCH = 15;

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
	public AccounterException(int errorCode, String message) {
		super(message);
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
	 * Creates new Instance
	 */
	public AccounterException(int errorCode, Throwable e) {
		super(e);
		this.errorCode = errorCode;
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
