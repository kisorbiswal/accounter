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
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class AbstractTransactionItemsRequirement<T> extends
		ListRequirement<T> {

	private static final String TRANSACTION_ITEMS = "transactionItems";
	protected static final String OLD_TRANSACTION_ITEM_ATTR = "oldTransactionItemAttr";

	protected static final String PROCESS_ATTR = "processAttr";

	public AbstractTransactionItemsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, null);

		setValue(new ArrayList<ClientTransactionItem>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isDone() {
		if (!isOptional()) {
			return ((List<ClientTransactionItem>) getValue()).size() != 0;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDefaultValue(Object defaultValue) {
		((List<ClientTransactionItem>) getValue())
				.addAll((List<ClientTransactionItem>) defaultValue);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(getName())) {
				Result result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
				addFirstMessage(context, getSetMessage());
			}
		}
		List<T> items = context.getSelections(getName());
		List<ClientTransactionItem> transactionItems = getValue();
		if (items != null && items.size() > 0) {
			for (T item : items) {
				ClientTransactionItem transactionItem = new ClientTransactionItem();
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				setItem(transactionItem, item);
				setPrice(transactionItem, item);

				ClientQuantity quantity = new ClientQuantity();
				quantity.setValue(1);
				transactionItem.setQuantity(quantity);

				double lt = transactionItem.getQuantity().getValue()
						* transactionItem.getUnitPrice();
				double disc = transactionItem.getDiscount();
				transactionItem
						.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
								* disc / 100))
								: lt);
				transactionItems.add(transactionItem);
				Result transactionItemResult = checkItemToEdit(context,
						transactionItem);
				if (transactionItemResult != null) {
					return transactionItemResult;
				}
			}
			context.setAttribute(INPUT_ATTR, "");
		}

		boolean show = false;
		if (!isDone()) {
			show = true;
		}

		ClientTransactionItem editTransactionItem = context
				.getSelection(TRANSACTION_ITEMS + getName());
		if (editTransactionItem != null) {
			Result result = transactionItem(context, editTransactionItem);
			if (result != null) {
				return result;
			}
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection == ActionNames.ADD_MORE_ITEMS) {
			show = true;
		}

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(getName())) {
			show = true;
		}

		if (show) {
			List<T> oldValues = new ArrayList<T>();
			for (ClientTransactionItem item : transactionItems) {
				oldValues.add(getTransactionItem(item));
			}
			return showList(makeResult, context, oldValues);
		}

		makeResult.add(getRecordName());
		ResultList itemsList = new ResultList(TRANSACTION_ITEMS + getName());
		for (ClientTransactionItem item : transactionItems) {
			Record itemRec = new Record(item);
			itemRec.add("", getItemDisplayValue(item));
			itemRec.add("", item.getLineTotal());
			itemRec.add("", item.getVATfraction());
			itemsList.add(itemRec);
		}
		makeResult.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", getAddMoreString());
		actions.add(moreItems);
		return null;
	}

	private Result transactionItemProcess(Context context) {
		ClientTransactionItem transactionItem = (ClientTransactionItem) context
				.getAttribute(OLD_TRANSACTION_ITEM_ATTR);
		Result result = transactionItem(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				List<ClientTransactionItem> transItems = getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
			}
			context.setAttribute(INPUT_ATTR, "");
		}
		return result;
	}

	protected Result taxCode(Context context, String displayName,
			ClientTAXCode oldCode) {
		Result result = context.makeResult();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		context.setAttribute(INPUT_ATTR, getName());
		String name = null;
		if (attribute.equals(getName())) {
			name = context.getString();
		}
		if (name == null) {
			context.setAttribute("oldValue", "");
			result.add(displayName);
			ResultList actions = new ResultList(ACTIONS);
			Record record = new Record(ActionNames.ALL);
			record.add("", "Show All Records");
			actions.add(record);
			result.add(actions);
			return result;
		}

		Object selection = context.getSelection(ACTIONS);
		List<ClientTAXCode> lists = new ArrayList<ClientTAXCode>();
		if (selection == ActionNames.ALL) {
			lists = getTaxCodeLists(context);
			if (lists.size() != 0) {
				result.add("All Records");
			}
			name = null;
		} else if (selection == null) {
			lists = getTaxCodeLists(context, name);
			context.setAttribute("oldValue", name);
			if (lists.size() != 0) {
				result.add("Found " + lists.size() + " record(s)");
			} else {
				result.add("Did not get any records with '" + name + "'.");
				result.add(displayName);
				ResultList actions = new ResultList(ACTIONS);
				Record record = new Record(ActionNames.ALL);
				record.add("", "Show All Records");
				actions.add(record);
				result.add(actions);
				return result;
			}
		} else {
			String oldValue = (String) context.getAttribute("oldValue");
			if (oldValue != null && !oldValue.equals("")) {
				lists = getTaxCodeLists(context, oldValue);
			} else {
				lists = getTaxCodeLists(context);
			}
		}
		List<ClientTAXCode> oldRecords = new ArrayList<ClientTAXCode>();
		if (oldCode != null) {
			oldRecords.add(oldCode);
		}
		return displayRecords2(context, lists, result, 5, oldRecords);
	}

	private Result displayRecords2(Context context, List<ClientTAXCode> lists,
			Result result, int recordsToShow, List<ClientTAXCode> oldRecords) {
		ResultList customerList = new ResultList(getName());
		Object last = context.getLast(RequirementType.CUSTOMER);
		List<ClientTAXCode> skipCustomers = new ArrayList<ClientTAXCode>();
		if (last != null) {
			ClientTAXCode lastRec = (ClientTAXCode) last;
			customerList.add(createClientTAXCodeRecord(lastRec));
			skipCustomers.add(lastRec);
		}

		if (oldRecords != null) {
			for (ClientTAXCode t : oldRecords) {
				customerList.add(createClientTAXCodeRecord(t));
				skipCustomers.add(t);
			}
		}

		ResultList actions = new ResultList(ACTIONS);

		ActionNames selection = context.getSelection(ACTIONS);

		List<ClientTAXCode> pagination = pagination2(context, selection,
				actions, lists, skipCustomers, recordsToShow);

		for (ClientTAXCode rec : pagination) {
			customerList.add(createClientTAXCodeRecord(rec));
		}

		int size = customerList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Tax Code");
		} else {
			message.append("No Tax Codes");
		}

		result.add(message.toString());
		result.add(customerList);
		result.add(actions);
		CommandList commandList = new CommandList();
		commandList.add("Create Tax code");
		result.add(commandList);
		return result;
	}

	public List<ClientTAXCode> pagination2(Context context,
			ActionNames selection, ResultList actions,
			List<ClientTAXCode> records, List<ClientTAXCode> skipRecords,
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
		List<ClientTAXCode> result = new ArrayList<ClientTAXCode>();
		for (int i = index; i < records.size(); i++) {
			if (num == recordsToShow) {
				break;
			}
			ClientTAXCode r = records.get(i);
			if (skipRecords.contains(r)) {
				continue;
			}
			num++;
			result.add(r);
		}
		context.setAttribute("LAST_PAGE_SIZE",
				skipRecords.size() + result.size());
		index += (skipRecords.size() + result.size());
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

	private Record createClientTAXCodeRecord(ClientTAXCode lastRec) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ClientTAXCode> getTaxCodeLists(Context context, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ClientTAXCode> getTaxCodeLists(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result number(Context context, String displayString,
			String oldValu) {
		return show(context, displayString, oldValu);
	}

	protected Result amount(Context context, String displayString,
			double oldValu) {
		return show(context, displayString, String.valueOf(oldValu));
	}

	protected abstract T getTransactionItem(ClientTransactionItem item);

	protected abstract void setPrice(ClientTransactionItem transactionItem,
			T item);

	protected abstract void setItem(ClientTransactionItem transactionItem,
			T item);

	protected abstract Result transactionItem(Context context,
			ClientTransactionItem editTransactionItem);

	protected abstract Result checkItemToEdit(Context context,
			ClientTransactionItem transactionItem);

	protected abstract String getAddMoreString();

	protected abstract String getItemDisplayValue(ClientTransactionItem item);

	protected abstract ClientCompany getClientCompany();
}
