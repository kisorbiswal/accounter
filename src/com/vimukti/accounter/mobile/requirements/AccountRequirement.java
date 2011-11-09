package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;

public abstract class AccountRequirement extends ListRequirement<Account> {

	public AccountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Account> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(Account value) {
		Record record = new Record(value);
		record.add("", value.getName());
		record.add("", value.getNumber());
		record.add("", value.getCurrentBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(Account value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(Global.get().Account()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Account());
	}

	@Override
	protected boolean filter(Account e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase());
	}
}
