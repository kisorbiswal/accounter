package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientCustomer;

public abstract class CustomerRequirement extends
		ListRequirement<ClientCustomer> {

	public CustomerRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCustomer> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getDisplayValue(ClientCustomer value) {
		return value != null ? value.getDisplayName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return "Create Customer";
	}

	@Override
	protected String getSelectString() {
		return "Slect a Customer";
	}

	@Override
	protected Record createRecord(ClientCustomer value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getBalance());
		return record;
	}

}
