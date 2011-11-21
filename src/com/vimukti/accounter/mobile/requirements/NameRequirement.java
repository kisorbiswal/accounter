package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;

public class NameRequirement extends SingleRequirement<String> {

	public NameRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		setDefaultValue("");
	}

	@Override
	protected String getDisplayValue(String value) {
		return value;
	}

	@Override
	protected String getInputFromContext(Context context) {
		return context.getString().isEmpty() ? null : context.getString();
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_STRING);
	}

	@Override
	public boolean isDone() {
		if (!isOptional()) {
			String value = getValue();
			return value != null && !value.isEmpty();
		}
		return true;
	}
}
