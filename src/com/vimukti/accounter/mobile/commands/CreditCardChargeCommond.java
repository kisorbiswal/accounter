package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CreditCardChargeCommond extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("supplier", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new ObjectListRequirement("accounts", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", false, true));
				list.add(new Requirement("price", false, true));
			}
		});
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("memo", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
		list.add(new Requirement("deliveryDate", true, true));
	}

	@Override
	public Result run(Context context) {

		Result result = createSupplierRequirement(context);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}
		//result = accountsRequirement(context);
		if (result != null) {
			return result;
		}
		result = depositeOrTransferTo(context, "depositOrTransferTo");
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void completeProcess(Context context) {
		// TODO Auto-generated method stub

	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		Requirement accountReq = get("accounts");
		List<TransactionItem> accountItem = accountReq.getValue();

		selection = context.getSelection("accountItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement supplierReq = get("supplier");
		Vendor supplier = (Vendor) supplierReq.getValue();

		selection = context.getSelection("values");
		if (supplier == selection) {
			return createSupplierRequirement(context);
		}

		Record supplierRecord = new Record(supplier);
		supplierRecord.add("Name", "Customer");
		supplierRecord.add("Value", supplier.getName());

		list.add(supplierRecord);

		Requirement transferTo = get("depositOrTransferTo");
		Account account = transferTo.getValue();
		Record numberRec = new Record(account);
		numberRec.add("Number", "Account No");
		numberRec.add("value", account.getNumber());
		Record nameRec = new Record(account);
		nameRec.add("Account name", "Account Name");
		nameRec.add("value", account.getName());
		Record accountRec = new Record(account);
		accountRec.add("Account type", "Account Type");
		accountRec.add("Account Type", getAccountTypeString(account.getType()));
		list.add(numberRec);
		list.add(nameRec);
		list.add(accountRec);

		Result result = dateOptionalRequirement(context, list, "date",
				"Enter Date", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, "deliveryDate",
				"Enter Delivery Date", selection);
		if (result != null) {
			return result;
		}
		result = contactRequirement(context, list, selection, supplier);
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		// result = billToRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		result = phoneRequirement(context, list, (String) selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CreditCardCharge is ready to create with following values.");
		result.add(list);
		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
		}
		result.add(items);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create CreditCardCharge.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result numberRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("number");
		String orderNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("number")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			orderNo = order;
			req.setValue(orderNo);
		}

		if (selection == orderNo) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter CreditCardCharge number", orderNo);
		}

		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "CreditCardCharge No");
		orderNoRecord.add("Value", orderNo);
		list.add(orderNoRecord);
		return null;
	}

}
