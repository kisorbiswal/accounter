package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;

public abstract class MeasurementRequirement extends
		ListRequirement<Measurement> {

	public MeasurementRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Measurement> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected Record createRecord(Measurement value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().description(), value.getDesctiption());
		return record;
	}

	@Override
	protected String getDisplayValue(Measurement value) {
		return value.getName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().measurement()));

	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().measurement());
	}

	@Override
	protected boolean filter(Measurement e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<Measurement> getLists(Context context) {
		return new ArrayList<Measurement>(context.getCompany()
				.getMeasurements());
	}

}
