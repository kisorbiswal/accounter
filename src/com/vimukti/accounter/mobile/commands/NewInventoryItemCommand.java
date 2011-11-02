package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.MeasurementRequirement;

public class NewInventoryItemCommand extends NewNonInventoryItemCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new MeasurementRequirement(getConstants().measurement(), getMessages()
				.pleaseSelect(getConstants().measurement()), getConstants()
				.measurementName(), true, true, null) {

			@Override
					protected void setCreateCommand(CommandList list) {
						list.add(getMessages().create(getConstants().inventoryItem()));
					}
			
			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().inventoryItem());
			}

		});
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getConstants().inventoryItem();
	}

	@Override
	public String getId() {
		return null;
	}

}
