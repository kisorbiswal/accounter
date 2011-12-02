package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;

public class NewNonInventoryItemCommand extends AbstractItemCreateCommand {
	public NewNonInventoryItemCommand() {
		super(Item.TYPE_INVENTORY_PART);
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
		return getItem().getID() == 0 ? "Creating Non Inventory Item"
				: "Updating Non Inventory Item";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isSellProducts()) {
			addFirstMessage(context, "You dnt have permission to do this.");
			return "cancel";
		}
		return super.initObject(context, isUpdate);
	}

}
