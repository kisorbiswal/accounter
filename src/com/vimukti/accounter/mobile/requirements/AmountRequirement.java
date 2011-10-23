package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;

public class AmountRequirement extends SingleRequirement<Double> {

	public AmountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(Double value) {
		return value.toString();
	}

	@Override
	protected Double getInputFromContect(Context context) {
		return context.getDouble();
	}

}
