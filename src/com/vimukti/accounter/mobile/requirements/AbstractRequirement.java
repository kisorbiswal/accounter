package com.vimukti.accounter.mobile.requirements;

import java.io.IOException;

import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public abstract class AbstractRequirement extends Requirement {
	public static final String INPUT_ATTR = "input";
	public static final String VALUES = "values";
	public static final String ACTIONS = "actions";

	private IGlobal global;
	private AccounterConstants constants;
	private AccounterMessages messages;

	public AbstractRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		constants = global.constants();
		messages = global.messages();
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

	protected AccounterConstants getConstants() {
		return constants;
	}

	protected AccounterMessages getMessages() {
		return messages;
	}
}
