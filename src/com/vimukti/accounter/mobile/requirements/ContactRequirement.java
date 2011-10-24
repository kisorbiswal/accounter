package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientContact;

public abstract class ContactRequirement extends ListRequirement<ClientContact> {

	public ContactRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientContact> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientContact value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientContact value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(getConstants().contact());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().contact());
	}

}
