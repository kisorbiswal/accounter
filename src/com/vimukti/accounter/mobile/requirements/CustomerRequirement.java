package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;

public abstract class CustomerRequirement extends ListRequirement<Customer> {

	public CustomerRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Customer> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getDisplayValue(Customer value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(Global.get().Customer()));
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(Global.get().Customer());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Customer());
	}

	@Override
	protected String getSetMessage() {
		Customer value = getValue();
		return getMessages().selectedAs(value.getName(),
				Global.get().Customer());
	}

	@Override
	protected boolean filter(Customer e, String name) {
		return e.getName().toLowerCase().startsWith(name)
				|| e.getNumber().equals(name);
	}

	@Override
	protected Record createRecord(Customer value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getBalance());
		return record;
	}

}
