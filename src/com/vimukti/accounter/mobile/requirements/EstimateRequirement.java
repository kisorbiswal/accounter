package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class EstimateRequirement extends ListRequirement<Estimate> {

	public EstimateRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Estimate> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);

	}

	@Override
	protected String getEmptyString() {
		return "There are no Estimates";
	}

	@Override
	protected String getSetMessage() {
		return "Estimate is selected";
	}

	@Override
	protected Record createRecord(Estimate value) {
		Record rec = new Record(value);
		rec.add("", getConstants().quote());
		rec.add("", value.getTotal());

		return rec;
	}

	@Override
	protected String getDisplayValue(Estimate value) {
		return value != null ? value.getCustomer().getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().quote()));

	}

	@Override
	protected String getSelectString() {
		return getMessages().selectTypeOfThis(getConstants().quote());
	}

}