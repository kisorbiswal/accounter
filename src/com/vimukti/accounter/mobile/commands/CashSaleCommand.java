package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CashSaleCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";
	private static final String ITEM_PROPERTY_ATTR = null;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
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
		list.add(new Requirement("paymentMethod", false, true));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("memo", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = customerRequirement(context);
		if (result == null) {
			return result;
		}

		result = paymentMethod(context, null);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result == null) {
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
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement transferTo = get("depositOrTransferTo");
		Account account = transferTo.getValue();
		Record accountRec = new Record(account);
		accountRec.add("Number", "Account No");
		accountRec.add("value", account.getNumber());
		accountRec.add("Account name", "Account Name");
		accountRec.add("value", account.getNumber());
		accountRec.add("Account type", "Account Type");
		accountRec.add("Account Type", getAccountTypeString(account.getType()));
		list.add(accountRec);

		Requirement custmerReq = get("customer");
		Customer customer = (Customer) custmerReq.getValue();

		selection = context.getSelection("values");
		if (customer == selection) {
			return customerRequirement(context);
		}

		// ResultList list = new ResultList("values");

		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());

		list.add(custRecord);

		Result result = cashSaleDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = cashSaleNoRequirement(context, list, selection);
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

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CashSale is ready to create with following values.");
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
		finish.add("", "Finish to create CashSale.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result cashSaleNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String cashSaleNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("orderNo")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			cashSaleNo = order;
			req.setValue(cashSaleNo);
		}

		if (selection == cashSaleNo) {
			context.setAttribute(INPUT_ATTR, "orderNo");
			return number(context, "Enter CashSale number", cashSaleNo);
		}

		Record cashSaleNoRec = new Record(cashSaleNo);
		cashSaleNoRec.add("Name", "Cash Sale Number");
		cashSaleNoRec.add("Value", cashSaleNo);
		list.add(cashSaleNoRec);
		return null;
	}

	private Result cashSaleDateRequirement(Context context, ResultList list,
			Object selection) {

		Requirement dateReq = get("date");
		Date transDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("Cash Sale Date")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setValue(transDate);
		}
		if (selection == transDate) {
			context.setAttribute(INPUT_ATTR, "Cash Sale Date");
			return date(context, "Enter Cash Sale Date", transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", "Cash Sale Date");
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}

}
