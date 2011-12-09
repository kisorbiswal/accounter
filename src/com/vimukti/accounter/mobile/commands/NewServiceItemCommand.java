package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.mobile.Context;
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
		return getItem().getID() == 0 ? "Creating Service Item"
				: "Updating Service Item";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellServices()) {
			addFirstMessage(context, getMessages()
					.youDntHavePermissionToDoThis());
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}
}
