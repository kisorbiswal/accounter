package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class CurrencyRequirement extends ListRequirement<Currency> {

	public CurrencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Currency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(Currency value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(Currency value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {

	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getConstants().currency());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().currency());
	}

	@Override
	protected boolean filter(Currency e, String name) {
		return e.getName().contains(name);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().currency());
	}
}
