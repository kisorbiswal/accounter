package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.core.Item;

public class NewServiceItemCommand extends AbstractItemCreateCommand {

	public NewServiceItemCommand() {
		super(Item.TYPE_SERVICE);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Service Item";
	}
}
