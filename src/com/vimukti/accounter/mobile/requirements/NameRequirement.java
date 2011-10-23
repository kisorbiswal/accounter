package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NameRequirement extends AbstractRequirement<String> {

	public NameRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(getName())) {
			String selection = context.getSelection(getName());
			if (selection == null) {
				selection = context.getString();
			}
			setValue(selection);
			context.setAttribute(INPUT_ATTR, "");
		}

		Object selection = context.getSelection(VALUES);
		if (!isDone() || (selection != null && selection.equals(getName()))) {
			context.setAttribute(INPUT_ATTR, getName());
			return text(context);
		}

		String customerName = getValue();
		Record nameRecord = new Record(getName());
		nameRecord.add("", getRecordName());
		nameRecord.add("", customerName);
		list.add(nameRecord);
		return null;
	}

	private Result text(Context context) {
		Result result = context.makeResult();
		result.add(getDisplayString());
		if (getValue() != null) {
			ResultList list = new ResultList(getName());
			Record record = new Record(getValue());
			record.add("", getValue());
			list.add(record);
			result.add(list);
		}
		return result;
	}
}
