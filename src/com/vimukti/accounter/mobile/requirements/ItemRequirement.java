package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientItem;

public class ItemRequirement extends ListRequirement<ClientItem> {

	public ItemRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientItem> listner) {
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
	protected Record createRecord(ClientItem value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getStandardCost());
		record.add("", value.isTaxable());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientItem value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().item()));
	}

	@Override
	protected String getSelectString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean filter(ClientItem e, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<ClientItem> getLists(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
