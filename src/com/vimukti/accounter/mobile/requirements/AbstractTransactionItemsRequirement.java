package com.vimukti.accounter.mobile.requirements;

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
		AbstractRequirement {

	private static final String TRANSACTION_ITEMS = null;

	public AbstractTransactionItemsRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext);
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
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
		}
		if (!isDone()) {
			return items(context);
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
			return items(context);
		}

		makeResult.add(getItemsDisplayString());
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

	protected abstract void setPrice(ClientTransactionItem transactionItem,
			T item);

	protected abstract void setItem(ClientTransactionItem transactionItem,
			T item);

	protected abstract Result items(Context context);

	protected abstract Result transactionItem(Context context,
			ClientTransactionItem editTransactionItem);

	protected abstract Result checkItemToEdit(Context context,
			ClientTransactionItem transactionItem);

	protected abstract String getCreateCommand();

	protected abstract String getSelectMessage();

	protected abstract Record creatItemRecord(T value);

	protected abstract List<T> getItems();

	protected abstract String getAddMoreString();

	protected abstract String getItemDisplayValue(ClientTransactionItem item);

	protected abstract String getItemsDisplayString();
}
