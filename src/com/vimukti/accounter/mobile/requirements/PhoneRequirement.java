package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;

public class PhoneRequirement extends SingleRequirement<Integer> {

	public PhoneRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(Integer value) {
		return value.toString();
	}

	@Override
	protected Integer getInputFromContext(Context context) {
		return context.getInteger();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_PHONE);
	}

}
