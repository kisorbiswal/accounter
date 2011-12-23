package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.web.client.core.ClientItem;

public class NewNonInventoryItemCommand extends AbstractItemCreateCommand {
	public NewNonInventoryItemCommand() {
		super(ClientItem.TYPE_NON_INVENTORY_PART);
	}

	public NewNonInventoryItemCommand(int type) {
		super(type);
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new NumberRequirement(WEIGHT, getMessages().pleaseEnter(
				getMessages().weight()), getMessages().weight(), true, true));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getItem().getID() == 0 ? getMessages().creating(
				getMessages().nonInventoryItem())
				: "Updating Non Inventory Item";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellProducts()) {
			addFirstMessage(context, "You do not have permission to do this.");
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}

}
