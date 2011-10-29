package com.vimukti.accounter.mobile.requirements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;

public abstract class TransactionPayBillRequirement extends
		AbstractRequirement<ClientTransactionPayBill> {

	private static final String RECORDS_START_INDEX = null;
	private static final int RECORDS_TO_SHOW = 0;
	private static final String BILLS_LIST = null;

	public TransactionPayBillRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
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
				List<ClientTransactionPayBill> accounts = getValue();
				accounts.add((ClientTransactionPayBill) objSelection);
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
			List<ClientTransactionPayBill> oldValues = new ArrayList<ClientTransactionPayBill>();
			for (ClientTransactionPayBill templateAccount : oldValues) {
				oldValues.add(templateAccount);
			}
			return showList(context, oldValues);
		}
		List<ClientTransactionPayBill> values = getValue();
		Object selection = context.getSelection(BILLS_LIST);
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
		makeResult.add(getRecordName());
		ResultList itemsList = new ResultList(BILLS_LIST + getName());
		for (ClientTransactionPayBill item : values) {
			itemsList.add(createRecord(item));
		}
		makeResult.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(getConstants().bills()));
		actions.add(moreItems);
		return null;
	}

	public Result showList(Context context,
			List<ClientTransactionPayBill> oldRecords) {
		Result result = context.makeResult();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		context.setAttribute(INPUT_ATTR, getName());

		Object selection = context.getSelection(ACTIONS);
		List<ClientTransactionPayBill> lists = new ArrayList<ClientTransactionPayBill>();
		if (selection == ActionNames.ALL) {
			lists = getLists(context);
			if (lists.size() != 0) {
				result.add("All Records");
			}

			result.add(getDisplayString());
			lists = getLists(context);
		}

		return displayRecords(context, lists, result, RECORDS_TO_SHOW,
				oldRecords);

	}

	protected abstract String getDisplayString();

	private Result displayRecords(Context context,
			List<ClientTransactionPayBill> records, Result result,
			int recordsToShow, List<ClientTransactionPayBill> oldRecords) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<ClientTransactionPayBill> skipCustomers = new ArrayList<ClientTransactionPayBill>();
		if (last != null) {
			ClientTransactionPayBill lastRec = (ClientTransactionPayBill) last;
			customerList.add(createRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		if (oldRecords != null) {
			for (ClientTransactionPayBill t : oldRecords) {
				// customerList.add(createRecord(t));
				skipCustomers.add(t);
			}
		}

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<ClientTransactionPayBill> pagination = pagination(context,
				selection, actions, records, skipCustomers, recordsToShow);

		for (ClientTransactionPayBill rec : pagination) {
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

	public List<ClientTransactionPayBill> pagination(Context context,
			ActionNames selection, ResultList actions,
			List<ClientTransactionPayBill> records,
			List<ClientTransactionPayBill> skipRecords, int recordsToShow) {
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
		List<ClientTransactionPayBill> result = new ArrayList<ClientTransactionPayBill>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			ClientTransactionPayBill r = records.get(i);
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
	protected Record createRecord(ClientTransactionPayBill last) {
		Record paybillRecord = new Record(last);
		paybillRecord.add("", getConstants().dueDate());
		paybillRecord.add("",
				getDateAsString(new ClientFinanceDate(last.getDueDate())));
		paybillRecord.add("", getConstants().billNo());
		paybillRecord.add("", last.getBillNumber());
		paybillRecord.add("", getConstants().originalAmount());
		paybillRecord.add("", last.getOriginalAmount());
		paybillRecord.add("", getConstants().amountDue());
		paybillRecord.add("", last.getAmountDue());
		paybillRecord.add("", getConstants().discountDate());
		paybillRecord.add("",
				getDateAsString(new ClientFinanceDate(last.getDiscountDate())));
		paybillRecord.add("", getConstants().cashDiscount());
		paybillRecord.add("", last.getCashDiscount());
		return paybillRecord;
	}

	public String getDateAsString(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(getClientCompany()
				.getPreferences().getDateFormat());
		return format.format(date.getDateAsObject());
	}

	/**
	 * To Show a single record
	 * 
	 * @param value
	 * @return
	 */
	protected String getDisplayValue(ClientTransactionPayBill value) {
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
	 * Total Records
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<ClientTransactionPayBill> getLists(Context context);

}
