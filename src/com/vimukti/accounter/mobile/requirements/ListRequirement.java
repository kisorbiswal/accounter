package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class ListRequirement<T> extends AbstractRequirement<T> {
	private static final int RECORDS_TO_SHOW = 0;
	private static final String RECORDS_START_INDEX = null;
	private ChangeListner<T> listner;

	public ListRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<T> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
		this.listner = listner;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Object selection = context.getSelection(VALUES);

		Object selectedObj = context.getSelection(getName());
		if (selectedObj instanceof ActionNames) {
			selectedObj = null;
			selection = getName();
		}

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(getName())) {
			if (selectedObj != null) {
				setValue(selectedObj);
				T value = getValue();
				listner.onSelection(value);
			}
		}

		if (!isDone()) {
			selection = getName();
		}

		if (selection != null && selection.equals(getName())) {
			return showList(context);
		}

		T value = getValue();
		Record customerRecord = new Record(getName());
		customerRecord.add("", getRecordName());
		customerRecord.add("", getDisplayValue(value));
		list.add(customerRecord);

		return null;
	}

	public Result showList(Context context) {
		Result result = context.makeResult();

		String name = null;
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute.equals(getName())) {
			name = context.getString();
		}

		Object selection = context.getSelection(ACTIONS);
		List<T> lists = null;
		if (selection == ActionNames.ALL) {
			lists = getLists(context);
			result.add("All Customers");
		} else if (name != null) {
			lists = getLists(context, name);
			if (lists.size() != 0) {
				result.add("Found " + lists.size() + " record(s)");
			}
		}

		if (lists.size() != 0) {
			return displayRecords(context, lists, result, RECORDS_TO_SHOW);
		}

		if (name != null) {
			result.add("Did not get any records with '" + name + "'.");
		}

		result.add(getDisplayString());
		ResultList actions = new ResultList(ACTIONS);
		Record record = new Record(ActionNames.ALL);
		record.add("", "Show All Customers");

		actions.add(record);
		result.add(actions);

		context.setAttribute(INPUT_ATTR, getName());
		return result;
	}

	private Result displayRecords(Context context, List<T> customers,
			Result result, int recordsToShow) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<T> skipCustomers = new ArrayList<T>();
		if (last != null) {
			@SuppressWarnings("unchecked")
			T lastRec = (T) last;
			customerList.add(createRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		T value2 = getValue();
		if (value2 != null) {
			customerList.add(createRecord(value2));
			skipCustomers.add(value2);
		}

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<T> pagination = pagination(context, selection, actions, customers,
				skipCustomers, recordsToShow);

		for (T costomer : pagination) {
			customerList.add(createRecord(costomer));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getSelectString());
		}
		CommandList commandList = new CommandList();
		commandList.add(getCreateCommandString());

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		result.add(commandList);
		return result;
	}

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

	/**
	 * To show Full Record
	 * 
	 * @param last
	 * @return
	 */
	protected abstract Record createRecord(T value);

	/**
	 * To Show a single record
	 * 
	 * @param value
	 * @return
	 */
	protected abstract String getDisplayValue(T value);

	/**
	 * Create Command Ex: Create Customer
	 * 
	 * @return
	 */
	protected abstract String getCreateCommandString();

	/**
	 * When Show all Records,
	 * 
	 * @return
	 */
	protected abstract String getSelectString();

	/**
	 * Filtered List
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	protected abstract List<T> getLists(Context context, String name);

	/**
	 * Total Records
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<T> getLists(Context context);

}
