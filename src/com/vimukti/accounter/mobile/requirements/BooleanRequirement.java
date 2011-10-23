package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class BooleanRequirement extends SingleRequirement<Boolean> {

	public BooleanRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(Boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Boolean getInputFromContect(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
