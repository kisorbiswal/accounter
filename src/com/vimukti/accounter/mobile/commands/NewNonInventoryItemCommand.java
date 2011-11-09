package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;

public class NewNonInventoryItemCommand extends AbstractItemCreateCommand {

	public NewNonInventoryItemCommand(int itemType) {
		super(Item.TYPE_NON_INVENTORY_PART);
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new NumberRequirement(WEIGHT, getMessages().pleaseEnter(
				getConstants().weight()), getConstants().weight(), true, true));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Non Inventory Item";
	}

}
