package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

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
	protected List<ClientCustomer> getLists(Context context, String name) {
		final String str = name.toLowerCase();
		return Utility.filteredList(new ListFilter<ClientCustomer>() {

			@Override
			public boolean filter(ClientCustomer e) {
				return e.getName().toLowerCase().startsWith(str)
						|| e.getNumber().equals(str);
			}
		}, getLists(context));
	}

	@Override
	protected Record createRecord(ClientCustomer value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getBalance());
		return record;
	}

}
