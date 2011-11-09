package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class ItemRequirement extends ListRequirement<Item> {

	public ItemRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Item> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
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
		list.add(getMessages().create(getConstants().item()));
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
