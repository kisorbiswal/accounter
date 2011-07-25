/**
 * 
 */
package com.vimukti.accounter.workspace.tool;

/**
 * Used to Throw an AccounterOperationalException
 * 
 * @author Prasanna Kumar G
 * 
 */
public class AccounterOperationException extends AccounterException {
	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(String message) {
		super(message);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(Throwable t, String message) {
		super(t, message);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(Throwable t) {
		super(t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(int code) {
		super(code);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
