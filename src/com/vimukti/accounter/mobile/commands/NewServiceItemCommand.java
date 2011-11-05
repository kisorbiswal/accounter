package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.web.client.core.ClientItem;

public class NewServiceItemCommand extends AbstractItemCreateCommand {

	public NewServiceItemCommand() {
		super(ClientItem.TYPE_SERVICE);
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
