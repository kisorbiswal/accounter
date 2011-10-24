package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ListRequirement;

public class CountryRequirement extends ListRequirement<String> {

	public CountryRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<String> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(String value) {
		Record record = new Record(value);
		record.add("", value);
		return record;
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getCreateCommandString() {
		return null;
	}

	@Override
	protected String getSelectString() {
		return "Select a Country";
	}

	@Override
	protected List<String> getLists(Context context, String name) {
		List<String> countries = new ArrayList<String>();
		countries.add("United Kingdom");
		countries.add("US");
		countries.add("India");
		return countries;
	}

	@Override
	protected List<String> getLists(Context context) {
		List<String> countries = new ArrayList<String>();
		countries.add("United Kingdom");
		countries.add("US");
		countries.add("India");
		return countries;
	}

}
