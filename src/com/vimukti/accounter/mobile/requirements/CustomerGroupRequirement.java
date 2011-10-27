package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;

public abstract class CustomerGroupRequirement extends
		ListRequirement<ClientCustomerGroup> {

	public CustomerGroupRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCustomerGroup> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(
				getMessages().customerGroup(Global.get().Customer()));
	}

	@Override
	protected Record createRecord(ClientCustomerGroup value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCustomerGroup value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(
				getMessages().customerGroup(Global.get().Customer())));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(
				getMessages().customerGroup(Global.get().Customer()));
	}

}
