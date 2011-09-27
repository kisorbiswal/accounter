package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CustomerCreditMemoCommond extends AbstractTransactionCommand {

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
		list.add(new Requirement("creditNumber", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("reasonForIssuue", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
		list.add(new Requirement("deliveryDate", true, true));
		Company company = getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			list.add(new Requirement("tax", false, true));
		}
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

		result = accountsRequirement(context);
		if (result == null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result == null) {
			return result;
		}

		Company company = context.getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			Requirement taxReq = get("tax");
			TAXCode taxcode = context.getSelection(TAXCODE);
			if (!taxReq.isDone()) {
				if (taxcode != null) {
					taxReq.setValue(taxcode);
				} else {
					return taxCode(context, null);
				}
			}
			if (taxcode != null) {
				taxReq.setValue(taxcode);
			}
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

		Requirement custmerReq = get("customer");
		Customer customer = (Customer) custmerReq.getValue();

		selection = context.getSelection("values");
		if (customer == selection) {
			return customerRequirement(context);
		}

		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());
		list.add(custRecord);

		Result result = dateOptionalRequirement(context, list, "date",
				"Enter Date", selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = creditNoRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				"reasonForIssuue", "Enter Reson For Issue");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CustomerCreditMemo is ready to create with following values.");
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

	private Result creditNoRequirement(Context context, ResultList list,
			Object selection, Customer customer) {
		Requirement req = get("creditNumber");
		String cashSaleNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("creditNumber")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			cashSaleNo = order;
			req.setValue(cashSaleNo);
		}

		if (selection == cashSaleNo) {
			context.setAttribute(INPUT_ATTR, "creditNumber");
			return number(context, "Enter CreditNo", cashSaleNo);
		}

		Record cashSaleNoRec = new Record(cashSaleNo);
		cashSaleNoRec.add("Name", "Cash Sale Number");
		cashSaleNoRec.add("Value", cashSaleNo);
		list.add(cashSaleNoRec);
		return null;
	}

}
