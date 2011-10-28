package com.vimukti.accounter.mobile.commands;

public class NewServiceItemCommand extends AbstractItemCreateCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Service Item";
	}

}
