package com.vimukti.accounter.ext;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * modified By Malcom Fernandez
 * 
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	public int command;

	/**
	 * arg1 ClientCore Object Id
	 */
	public String arg1;

	/**
	 * arg2 companyId
	 */
	public String arg2;

	public Command next;

	/**
	 * data ClientCore object
	 */
	public Serializable data;

	public Timestamp date;

	public Command(int command, String arg1, String arg2) {
		this.command = command;
		assert arg1 == null || arg1.length() < 256;
		assert arg2 == null || arg2.length() < 256;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.date = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return " Command: " + this.command; //$NON-NLS-1$
	}

}
