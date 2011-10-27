package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class SingleRequirement<T> extends AbstractRequirement<T> {

	public SingleRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(getName())) {
			T selection = context.getSelection(getName());
			if (selection == null) {
				selection = getInputFromContext(context);
			}
			setValue(selection);
			context.setAttribute(INPUT_ATTR, "");
		}

		Object selection = context.getSelection(VALUES);
		if (!isDone() || (selection != null && selection.equals(getName()))) {
			context.setAttribute(INPUT_ATTR, getName());
			T v = getValue();
			return show(context, getEnterString(), v == null ? null
					: getDisplayValue(v));
		}

		T customerName = getValue();
		Record nameRecord = new Record(getName());
		nameRecord.add("", getRecordName());
		nameRecord.add("", getDisplayValue(customerName));
		list.add(nameRecord);
		return null;
	}

	protected abstract String getDisplayValue(T value);

	protected abstract T getInputFromContext(Context context);

}
