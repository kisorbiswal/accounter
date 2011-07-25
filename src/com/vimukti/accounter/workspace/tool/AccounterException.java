/**
 * 
 */
package com.vimukti.accounter.workspace.tool;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterException extends Exception {

	protected int errorCode;

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
}
