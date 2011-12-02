package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;

public class AmountRequirement extends SingleRequirement<Double> {

	public AmountRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue(0.0d);
	}

	@Override
	public <T> T getValue() {
		Object value2 = super.getValue();
		return (T) (value2 == null ? 0d : value2);
	}

	@Override
	public boolean isDone() {
		Double value = getValue();
		if (isOptional()) {
			return true;
		}
		return value != 0;
	}

	@Override
	protected String getDisplayValue(Double value) {
		return value != null ? value.toString() : "";
	}

	@Override
	protected Double getInputFromContext(Context context) {
		return (Double) (context.getDouble() == null ? getValue() : context
				.getDouble());
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_AMOUNT);
	}

}
