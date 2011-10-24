package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientPayee;

public abstract class PayeeRequirement extends ListRequirement<ClientPayee> {

	public PayeeRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientPayee> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientPayee value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientPayee value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(getConstants().payee());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().payee());
	}
}
