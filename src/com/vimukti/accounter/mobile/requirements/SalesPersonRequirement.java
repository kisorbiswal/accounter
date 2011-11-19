package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class SalesPersonRequirement extends
		ListRequirement<SalesPerson> {

	public SalesPersonRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<SalesPerson> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().salesPerson());
	}

	@Override
	protected Record createRecord(SalesPerson value) {
		Record record = new Record(value);
		record.add("", value.getFirstName());
		return record;
	}

	@Override
	protected String getDisplayValue(SalesPerson value) {
		return value != null ? value.getFirstName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().salesPerson()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().salesPerson());
	}

}
