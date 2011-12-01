package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.Context;

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
		return getItem().getID() == 0 ? "Creating Service Item"
				: "Updating Service Item";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellServices()) {
			addFirstMessage(context, "You dnt have permission to do this.");
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}
}
