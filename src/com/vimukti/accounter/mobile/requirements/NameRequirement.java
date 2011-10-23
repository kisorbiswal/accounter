package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class NameRequirement extends SingleRequirement<String> {

	public NameRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
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
