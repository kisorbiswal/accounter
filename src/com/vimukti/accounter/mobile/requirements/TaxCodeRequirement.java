package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class TaxCodeRequirement extends ListRequirement<TAXCode> {

	public TaxCodeRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<TAXCode> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(TAXCode value) {
		Record record = new Record(value);
		record.add("", value.getName() + "-" + value.getSalesTaxRate());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getConstants().taxCode());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getConstants().taxCode());
	}

	@Override
	protected String getDisplayValue(TAXCode value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().taxCode()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().taxCode());
	}

	@Override
	protected boolean filter(TAXCode e, String name) {
		return e.getName().toLowerCase().startsWith(name.toLowerCase());
	}
}
