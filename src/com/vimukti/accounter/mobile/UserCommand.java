package com.vimukti.accounter.mobile;

public class UserCommand {
	private String displayName;
	private String commandName;

	public UserCommand(String displayName) {
		this(displayName, displayName);
	}

	public UserCommand(String displayName, String commandName) {
		this.displayName = displayName;
		this.commandName = commandName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
