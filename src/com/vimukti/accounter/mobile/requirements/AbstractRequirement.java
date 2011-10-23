package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractRequirement extends Requirement {
	public static final String INPUT_ATTR = "input";
	public static final String VALUES = "values";
	public static final String ACTIONS = "actions";

	public AbstractRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
	}

	public abstract Result run(Context context, Result makeResult,
			ResultList list, ResultList actions);

	protected Result show(Context context, String string, String value) {
		Result result = context.makeResult();
		result.add(string);
		if (value != null) {
			ResultList list = new ResultList(getName());
			Record record = new Record(value);
			record.add("", value);
			list.add(record);
			result.add(list);
		}
		return result;
	}
}
