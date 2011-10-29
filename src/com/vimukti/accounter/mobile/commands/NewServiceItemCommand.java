package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.mobile.Context;

public class NewServiceItemCommand extends AbstractItemCreateCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Service Item";
	}

	@Override
	protected String initObject(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
