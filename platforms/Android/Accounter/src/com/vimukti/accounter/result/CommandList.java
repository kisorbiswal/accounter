package com.vimukti.accounter.result;

import java.util.ArrayList;
import java.util.List;

public class CommandList {
	private List<Command> commandNames = new ArrayList<Command>();

	public void setCommandNames(List<Command> commandNames) {
		this.commandNames = commandNames;
	}

	public List<Command> getCommandNames() {
		return commandNames;
	}
}
