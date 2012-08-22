package com.vimukti.accounterbb.result;

import java.util.Vector;


public class CommandList {
	private Vector commandNames = new Vector();

	public void setCommandNames(Vector commandNames) {
		this.commandNames = commandNames;
	}

	public Vector getCommandNames() {
		return commandNames;
	}
}
