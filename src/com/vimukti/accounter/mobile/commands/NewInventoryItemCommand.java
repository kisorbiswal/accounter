package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.MeasurementRequirement;

public class NewInventoryItemCommand extends NewNonInventoryItemCommand {

	public NewInventoryItemCommand() {
		super(Item.TYPE_INVENTORY_PART);
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new MeasurementRequirement(getMessages().measurement(),
				getMessages().pleaseSelect(getMessages().measurement()),
				getMessages().measurementName(), true, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getMessages().inventoryItem()));
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().inventoryItem());
			}

		});
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().inventoryItem();
	}

	@Override
	public String getId() {
		return null;
	}

}
