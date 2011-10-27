package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientTAXCode;

public abstract class TaxCodeRequirement extends ListRequirement<ClientTAXCode> {

	public TaxCodeRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientTAXCode> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientTAXCode value) {
		Record record = new Record(value);
		record.add("", value.getName() + "-" + value.getSalesTaxRate());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return "TaxCode is selected";
	}

	@Override
	protected String getEmptyString() {
		return "There are no TaxCodes";
	}

	@Override
	protected String getDisplayValue(ClientTAXCode value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().taxCode()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().taxCode());
	}
}
