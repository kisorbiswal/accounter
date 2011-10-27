package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientItemGroup;

public abstract class ItemGroupRequirement extends
		ListRequirement<ClientItemGroup> {

	public ItemGroupRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientItemGroup> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().itemGroup());
	}

	@Override
	protected Record createRecord(ClientItemGroup value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientItemGroup value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().itemGroup()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().itemGroup());
	}

}
