package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.UserCommand;

public abstract class ItemRequirement extends ListRequirement<Item> {

	private boolean isSales;

	public ItemRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Item> listner, boolean isSales) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
		this.isSales = isSales;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().items());
	}

	@Override
	protected String getSetMessage() {
		return "Item has been selected";
	}

	@Override
	protected Record createRecord(Item value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getStandardCost());
		return record;
	}

	@Override
	protected String getDisplayValue(Item value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		if (isSales) {
			list.add(new UserCommand("Create New Service Item", "sell"));
			list.add(new UserCommand("Create New NonInventory Item", "sell"));
			list.add(new UserCommand("Create New Inventory Item", "sell"));
		} else {
			list.add(new UserCommand("Create New Service Item", "buy"));
			list.add(new UserCommand("Create New NonInventory Item", "buy"));
			list.add(new UserCommand("Create New Inventory Item", "buy"));
		}
	}

	@Override
	protected String getSelectString() {
		return "Select the Item";
	}

	@Override
	protected boolean filter(Item e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase());
	}
}
