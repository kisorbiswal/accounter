package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
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
	protected void setCreateCommand(CommandList list) {
		list.add("Create Customer");
	}

	@Override
	protected String getEmptyString() {
		return "There are no customers";
	}

	@Override
	protected String getSelectString() {
		return "Slect a Customer";
	}

	@Override
	protected String getSetMessage() {
		ClientCustomer value = getValue();
		return value.getDisplayName() + " has been Selected.";
	}

	@Override
	protected boolean filter(ClientCustomer e, String name) {
		return e.getName().toLowerCase().startsWith(name)
				|| e.getNumber().equals(name);
	}

	@Override
	protected Record createRecord(ClientCustomer value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getBalance());
		return record;
	}

}
