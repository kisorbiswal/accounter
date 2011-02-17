/**
 * 
 */
package com.vimukti.accounter.ext;

/**
 * @author Fernandez
 * 
 */
@SuppressWarnings("serial")
public class CommandException extends Exception {

	private Throwable cause;
	private String message;

	public CommandException() {
		// TODO Auto-generated constructor stub
	}

	public CommandException(String message) {
		this.message = message;
	}

	public CommandException(String message, Throwable exception) {
		this.cause = exception;
	}

	@Override
	public String getMessage() {

		if (cause == null || message == null) {

			return super.getMessage();

		} else
			return message;
	}

}
