package com.vimukti.accounter.mobile.requirements;

import java.io.IOException;
import java.util.List;

import com.vimukti.accounter.main.ServerGlobal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public abstract class AbstractRequirement<T> extends Requirement {
	public static final String INPUT_ATTR = "input";
	public static final String VALUES = "values";
	public static final String ACTIONS = "actions";
	public static final String FIRST_MESSAGE = "firstMessage";
	private IGlobal global;
	private AccounterConstants constants;
	private AccounterMessages messages;
	private ClientCompany clientCompany;

	public AbstractRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		try {
			global = new ServerGlobal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		constants = global.constants();
		messages = global.messages();
	}

	@SuppressWarnings("unchecked")
	public void addFirstMessage(Context context, String string) {
		((List<String>) context.getAttribute(FIRST_MESSAGE)).add(string);
	}

	public ClientCompany getClientCompany() {
		return clientCompany;
	}

	@Override
	public Result process(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		clientCompany = context.getClientCompany();
		return run(context, makeResult, list, actions);
	}

	public abstract Result run(Context context, Result makeResult,
			ResultList list, ResultList actions);

	protected Result show(Context context, String string, String value) {
		Result result = context.makeResult();
		result.add(string);
		if (value != null && !value.isEmpty()) {
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
