package com.vimukti.accounter.mobile.commands;

import java.util.Date;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractCommand extends Command {
	protected static final String DATE = "date";
	protected static final String NUMBER = "number";
	protected static final String TEXT = "text";

	protected Result text(Context context, String message, String oldText) {
		Result result = context.makeResult();
		result.add(message);
		if (oldText != null) {
			ResultList list = new ResultList(TEXT);
			Record record = new Record(oldText);
			record.add("", oldText);
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result number(Context context, String message, String oldNumber) {
		Result result = context.makeResult();
		result.add(message);
		if (oldNumber != null) {
			ResultList list = new ResultList(NUMBER);
			Record record = new Record(oldNumber);
			record.add("", oldNumber);
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result date(Context context, String message, Date date) {
		Result result = context.makeResult();
		result.add(message);
		if (date != null) {
			ResultList list = new ResultList(DATE);
			Record record = new Record(date);
			record.add("", date.toString());
			list.add(record);
			result.add(list);
		}
		return result;
	}

	protected Result address(Context context, Address oldAddress) {
		Result result = context.makeResult();
		result.add("Enter ");
		return null;
	}
}
