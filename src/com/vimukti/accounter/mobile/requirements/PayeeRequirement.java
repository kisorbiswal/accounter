package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class PayeeRequirement extends ListRequirement<Payee> {

	public PayeeRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Payee> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(Payee value) {
		Record record = new Record(value);
		record.add(value.getName(), value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(Payee value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().payee()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().payee());
	}
}
