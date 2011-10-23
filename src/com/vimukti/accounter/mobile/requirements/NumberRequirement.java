package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class NumberRequirement extends SingleRequirement<String> {

	public NumberRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContect(Context context) {
		return context.getString();
	}

}
