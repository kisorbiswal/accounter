package com.vimukti.accounter.mobile;

public class UserCommand {
	private String inputs;
	private String commandName;
	private String displayName;
	private long id;

	public UserCommand(String commandName) {
		this(commandName, commandName, "");
	}

	public UserCommand(String commandName, String displayName, String inputs) {
		this.setDisplayName(displayName);
		this.commandName = commandName;
		this.setInputs(inputs);
	}

	public UserCommand(String commandName, String inputs) {
		this(commandName, commandName, inputs);
	}

	public UserCommand(String commandName, long id) {
		this.commandName = commandName;
		this.id = id;
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

	public String getInputs() {
		return inputs;
	}

	public void setInputs(String inputs) {
		this.inputs = inputs;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
