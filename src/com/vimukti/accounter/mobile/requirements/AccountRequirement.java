package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.utils.CommandUtils;

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
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().number(), value.getNumber());
		record.add(getMessages().balance(),
				value.getTotalBalanceInAccountCurrency());
		return record;
	}

	@Override
	protected String getDisplayValue(Account value) {
		return value != null ? value.getNumber() + "-" + value.getName() + "-"
				+ CommandUtils.getAccountTypeString(value.getType()) : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().Account()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().Account());
	}

	@Override
	protected boolean filter(Account e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase())
				|| e.getNumber().startsWith(
						String.valueOf(getNumberFromString(name)));
	}
}
