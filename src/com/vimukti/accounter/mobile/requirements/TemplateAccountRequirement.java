package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.TemplateAccount;

public abstract class TemplateAccountRequirement extends
		AbstractRequirement<TemplateAccount> {

	private static final String ACCOUNTS_LIST = "accountsList";
	private static final int RECORDS_TO_SHOW = 5;
	private static final String RECORDS_START_INDEX = "recordsStartIndex";

	public TemplateAccountRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {

		Object valuesSelection = context.getSelection(VALUES);
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		Object objSelection = context.getSelection(getName());
		if (objSelection instanceof ActionNames) {
			objSelection = null;
			valuesSelection = getName();
		}

		if (attribute.equals(getName())) {
			if (objSelection != null) {
				List<TemplateAccount> accounts = getValue();
				accounts.add((TemplateAccount) objSelection);
				setValue(accounts);
				context.setAttribute(INPUT_ATTR, "");
			} else {
				valuesSelection = getName();
			}
		}

		if (!isDone()) {
			valuesSelection = getName();
		}

		if (valuesSelection != null && valuesSelection.equals(getName())) {
			List<TemplateAccount> oldValues = new ArrayList<TemplateAccount>();
			for (TemplateAccount templateAccount : oldValues) {
				oldValues.add(templateAccount);
			}
			return showList(context, oldValues);
		}
		List<TemplateAccount> values = getValue();
		Object selection = context.getSelection(ACCOUNTS_LIST);
		if (selection != null) {
			values.remove(selection);
		}

		if (values.size() == 0) {
			return showList(context, values);
		}
		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ACCOUNTS) {
			return showList(context, values);
		}

		Record record = new Record("accountsNumber");
		record.add("", values.size() + " accounts selected.");
		list.add(record);

		if (valuesSelection == "accountsNumber") {
			Result result = new Result();
			result.add(getConstants().Accounts());
			ResultList itemsList = new ResultList(ACCOUNTS_LIST);
			for (TemplateAccount account : values) {
				Record itemRec = new Record(account);
				itemRec.add("", getRecordName());
				itemRec.add("", getDisplayValue(account));
				itemsList.add(itemRec);
			}
			result.add(itemsList);
			Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
			moreItems.add("", getMessages().addMore(getConstants().Accounts()));
			actions.add(moreItems);
			result.add(actions);
			return result;
		}

		return null;
	}

	public Result showList(Context context, List<TemplateAccount> oldRecords) {
		Result result = context.makeResult();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		context.setAttribute(INPUT_ATTR, getName());
		String name = null;
		if (attribute.equals(getName())) {
			name = context.getString();
		}
		if (name == null) {
			List<TemplateAccount> lists = getLists(context);
			if (lists.size() > RECORDS_TO_SHOW) {
				context.setAttribute("oldValue", "");
				result.add(getDisplayString());
				ResultList actions = new ResultList(ACTIONS);
				Record record = new Record(ActionNames.ALL);
				record.add("", "Show All Records");
				actions.add(record);
				result.add(actions);
				return result;
			}
			return displayRecords(context, lists, result, RECORDS_TO_SHOW,
					oldRecords);
		}

		Object selection = context.getSelection(ACTIONS);
		List<TemplateAccount> lists = new ArrayList<TemplateAccount>();
		if (selection == ActionNames.ALL) {
			lists = getLists(context);
			if (lists.size() != 0) {
				result.add("All Records");
			}
			name = null;
		} else if (selection == null) {
			lists = getLists(context, name);
			context.setAttribute("oldValue", name);
			if (lists.size() != 0) {
				result.add("Found " + lists.size() + " record(s)");
			} else {
				result.add("Did not get any records with '" + name + "'.");
				result.add(getDisplayString());
				lists = getLists(context);
			}
		} else {
			String oldValue = (String) context.getAttribute("oldValue");
			if (oldValue != null && !oldValue.equals("")) {
				lists = getLists(context, oldValue);
			} else {
				lists = getLists(context);
			}
		}

		return displayRecords(context, lists, result, RECORDS_TO_SHOW,
				oldRecords);

	}

	private Result displayRecords(Context context,
			List<TemplateAccount> records, Result result, int recordsToShow,
			List<TemplateAccount> oldRecords) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<TemplateAccount> skipCustomers = new ArrayList<TemplateAccount>();
		if (last != null) {
			TemplateAccount lastRec = (TemplateAccount) last;
			customerList.add(createRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		if (oldRecords != null) {
			for (TemplateAccount t : oldRecords) {
				// customerList.add(createRecord(t));
				skipCustomers.add(t);
			}
		}

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<TemplateAccount> pagination = pagination(context, selection,
				actions, records, skipCustomers, recordsToShow);

		for (TemplateAccount rec : pagination) {
			customerList.add(createRecord(rec));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getSelectString());
		} else {
			message.append(getEmptyString());
		}

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		return result;
	}

	protected abstract String getEmptyString();

	public List<TemplateAccount> pagination(Context context,
			ActionNames selection, ResultList actions,
			List<TemplateAccount> records, List<TemplateAccount> skipRecords,
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
		List<TemplateAccount> result = new ArrayList<TemplateAccount>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			TemplateAccount r = records.get(i);
			if (skipRecords.contains(r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE", result.size());
		index += (result.size());
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
	protected Record createRecord(TemplateAccount value) {
		Record itemRec = new Record(value);
		itemRec.add("", value.getName());
		itemRec.add("", value.getType());
		return itemRec;
	}

	/**
	 * To Show a single record
	 * 
	 * @param value
	 * @return
	 */
	protected String getDisplayValue(TemplateAccount value) {
		return value != null ? value.getName() : "";
	}

	/**
	 * When Show all Records,
	 * 
	 * @return
	 */
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().account());
	}

	/**
	 * Filtered List
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	protected abstract List<TemplateAccount> getLists(Context context,
			String name);

	/**
	 * Total Records
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<TemplateAccount> getLists(Context context);

}
