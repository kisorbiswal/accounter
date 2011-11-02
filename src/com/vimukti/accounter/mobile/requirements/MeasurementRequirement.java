package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientMeasurement;

public abstract class MeasurementRequirement extends ListRequirement<ClientMeasurement>{

	
	public MeasurementRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientMeasurement> listner) {
		super(requirementName, enterString, recordName, isOptional, isAllowFromContext,
				listner);
	}

	@Override
	protected String getEmptyString() {
		return getConstants().noRecordsToShow();
	}
	
	@Override
	protected Record createRecord(ClientMeasurement value) {
		Record record = new Record(value);
		record.add(getConstants().name(), value.getName());
		record.add(getConstants().description(), value.getDesctiption());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientMeasurement value) {
		return getConstants().measurementName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().measurement()));
		
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().measurement());
	}

	@Override
	protected boolean filter(ClientMeasurement e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<ClientMeasurement> getLists(Context context) {
		return context.getClientCompany().getMeasurements();
	}

}
