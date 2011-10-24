package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class AbstractTransactionItemsRequirement<T> extends
		ListRequirement<T> {

	private static final String TRANSACTION_ITEMS = "transactionItems";
	protected static final String OLD_TRANSACTION_ITEM_ATTR = "oldTransactionItemAttr";
	protected static final String PROCESS_ATTR = "processAttr";
	protected static final Object TRANSACTION_ITEM_PROCESS = "transactionItemProcess";

	public AbstractTransactionItemsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, null);
		setDefaultValue(new ArrayList<ClientTransactionItem>());
		setValue(new ArrayList<ClientTransactionItem>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isDone() {
		if (!isOptional()) {
			return ((List<ClientTransactionItem>) getValue()).size() != 0;
		}
		return ((List<ClientTransactionItem>) getDefaultValue()).size() != 0;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				Result result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
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
			return showList(context, null);
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

}
