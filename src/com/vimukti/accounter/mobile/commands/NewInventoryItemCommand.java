package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.MeasurementRequirement;
import com.vimukti.accounter.mobile.requirements.WarehouseRequirement;
import com.vimukti.accounter.web.client.core.ClientItem;

public class NewInventoryItemCommand extends NewNonInventoryItemCommand {

	public NewInventoryItemCommand() {
		super(ClientItem.TYPE_INVENTORY_PART);
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new MeasurementRequirement(MEASUREMENT, getMessages()
				.pleaseSelect(getMessages().measurement()), getMessages()
				.measurementName(), true, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(getMessages().create(getMessages().inventoryItem()));
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().inventoryItem());
			}

		});

		list.add(new WarehouseRequirement(WARE_HOUSE, getMessages()
				.pleaseSelect(getMessages().wareHouse()), getMessages()
				.wareHouse(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().inventoryItem());
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

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(MEASUREMENT).setDefaultValue(
				context.getCompany().getDefaultMeasurement());
		get(WARE_HOUSE).setDefaultValue(getCompany().getDefaultWarehouse());
	}

}
