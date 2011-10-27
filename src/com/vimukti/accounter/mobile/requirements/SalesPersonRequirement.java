package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;

public abstract class SalesPersonRequirement extends
		ListRequirement<ClientSalesPerson> {

	public SalesPersonRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientSalesPerson> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().salesPerson());
	}

	@Override
	protected Record createRecord(ClientSalesPerson value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientSalesPerson value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().salesPerson()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().salesPerson());
	}

}
