package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public abstract class TaxItemRequirement extends ListRequirement<ClientTAXItem> {

	public TaxItemRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientTAXItem> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientTAXItem value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientTAXItem value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().taxItem()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().taxItem());
	}

}
