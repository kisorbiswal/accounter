package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class BooleanRequirement extends AbstractRequirement<Boolean> {

	public BooleanRequirement(String requirementName, boolean isAllowFromContext) {
		super(requirementName, null, null, true, isAllowFromContext);
		setDefaultValue(false);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {

		String attribute = context.getSelection(VALUES);
		if (attribute == getName()) {
			Boolean isActive = getValue();
			setValue(!isActive);
		}

		Boolean isActive = getValue();
		Record isActiveRecord = new Record(getName());
		isActiveRecord.add("", isActive ? getTrueString() : getFalseString());
		list.add(isActiveRecord);
		return null;
	}

	protected abstract String getFalseString();

	protected abstract String getTrueString();

}
