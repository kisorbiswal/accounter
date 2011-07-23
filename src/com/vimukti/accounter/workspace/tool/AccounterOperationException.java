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
public class AccounterOperationException extends Exception {
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
		super(message, t);
	}

	/**
	 * Creates new Instance
	 */
	public AccounterOperationException(Throwable t) {
		super(t);
	}
}
