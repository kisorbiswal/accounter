package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class TaxAgencyRequirement extends ListRequirement<TAXAgency> {

	public TaxAgencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<TAXAgency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(TAXAgency value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(TAXAgency value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().taxAgency()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().taxAgency());
	}

}
