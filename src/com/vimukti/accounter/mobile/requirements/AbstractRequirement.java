package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractRequirement<T> extends Requirement {
	public static final String INPUT_ATTR = "input";
	public static final String VALUES = "values";
	public static final String ACTIONS = null;
	private static final String RECORDS_START_INDEX = null;

	public AbstractRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
	}

	public abstract Result run(Context context, Result makeResult,
			ResultList list, ResultList actions);

	public List<T> pagination(Context context, ActionNames selection,
			ResultList actions, List<T> records, List<T> skipRecords,
			int recordsToShow) {
		if (selection != null && selection == ActionNames.PREV_PAGE) {
			Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
			Integer lastPageSize = (Integer) context
					.getAttribute("LAST_PAGE_SIZE");
			context.setAttribute(RECORDS_START_INDEX,
					index
							- (recordsToShow + (lastPageSize == null ? 0
									: lastPageSize)));
		} else if (selection == null || selection != ActionNames.NEXT_PAGE) {
			context.setAttribute(RECORDS_START_INDEX, 0);
		}

		int num = skipRecords.size();
		Integer index = (Integer) context.getAttribute(RECORDS_START_INDEX);
		if (index == null || index < 0) {
			index = 0;
		}
		List<T> result = new ArrayList<T>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			T r = records.get(i);
			if (skipRecords.contains(r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE", result.size());
		index += result.size();
		context.setAttribute(RECORDS_START_INDEX, index);

		if (records.size() > index) {
			Record inActiveRec = new Record(ActionNames.NEXT_PAGE);
			inActiveRec.add("", "Next Page");
			actions.add(inActiveRec);
		}

		if (index > recordsToShow) {
			Record inActiveRec = new Record(ActionNames.PREV_PAGE);
			inActiveRec.add("", "Prev Page");
			actions.add(inActiveRec);
		}
		return result;
	}

	protected Result displayRecords(Context context, List<T> customers,
			Result result, int recordsToShow) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<T> skipCustomers = new ArrayList<T>();
		if (last != null) {
			@SuppressWarnings("unchecked")
			T lastRec = (T) last;
			customerList.add(createCustomerRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		T value2 = getValue();
		if (value2 != null) {
			customerList.add(createCustomerRecord(value2));
			skipCustomers.add(value2);
		}

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<T> pagination = pagination(context, selection, actions, customers,
				skipCustomers, recordsToShow);

		for (T costomer : pagination) {
			customerList.add(createCustomerRecord(costomer));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getDisplayString());
		}
		CommandList commandList = new CommandList();
		commandList.add("Create Customer");

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		result.add(commandList);
		return result;
	}

	protected Record createCustomerRecord(T last) {
		return new Record(last);
	}
}
