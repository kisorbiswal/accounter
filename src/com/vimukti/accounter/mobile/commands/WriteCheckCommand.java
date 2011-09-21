package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Utility;

public class WriteCheckCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("payee", false, true));
		list.add(new ObjectListRequirement("accounts", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
			}
		});
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
		list.add(new Requirement("bankAccounts", false, true));
		list.add(new Requirement("amount", true, true));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("memo", true, true));
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
			} else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = payeeRequirement(context);
		if (result != null) {
			return result;
		}

		result = bankAccountRequirement(context);
		if (result != null) {
			return result;
		}

		result = accountsRequirement(context);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		Company company = getCompany();
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
		return result;
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
			case ADD_MORE_ACCOUNTS:
				return accounts(context);
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		Requirement accountReq = get("accounts");
		List<TransactionItem> itemsList = accountReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		Requirement itemReq = get("items");
		List<TransactionItem> accountsList = itemReq.getValue();

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

		Requirement payeeReq = get("payee");
		Payee payee = (Payee) payeeReq.getValue();
		Record payeeRecord = new Record(payee);
		payeeRecord.add("Name", "Payee");
		payeeRecord.add("Value", payee.getName());

		list.add(payeeRecord);

		Result result = writeCheckDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = writeCheckNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}
		result = amountRequirment(context, list, selection);
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add("WriteCheck is ready to create with following values.");
		result.add(list);

		result.add("Accounts:-");
		ResultList accounts = new ResultList("transactionItems");
		for (TransactionItem account : accountsList) {
			Record accountRec = new Record(account);
			accountRec.add("Name", account.getAccount().getName());
			accountRec.add("Total", account.getLineTotal());
			accountRec.add("Type",
					Utility.getAccountTypeString(account.getType()));
		}
		result.add(accounts);
		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (TransactionItem item : itemsList) {
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
		finish.add("", "Finish to create WriteCheck.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result amountRequirment(Context context, ResultList list,
			Object selection) {
		Requirement req = get("amount");
		Double amount = req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("amount")) {
			Double input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getDouble();
			}
			amount = input;
			req.setValue(amount);
		}
		if (selection == amount) {
			context.setAttribute(attribute, "amount");
			return amount(context, "Enter WirteCheck Amount", amount);
		}
		Record amountRecord = new Record(amount);
		amountRecord.add("Name", "amount");
		amountRecord.add("Value", amount);
		list.add(amountRecord);
		return null;
	}

	private Result writeCheckNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String writeCheckNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("writeCheckNo")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			writeCheckNo = order;
			req.setValue(writeCheckNo);
		}

		if (selection == writeCheckNo) {
			context.setAttribute(INPUT_ATTR, "writeCheckNo");
			return number(context, "Enter WriteCheck number", writeCheckNo);
		}

		Record writeCheckNoRec = new Record(writeCheckNo);
		writeCheckNoRec.add("Name", "WriteCheck Number");
		writeCheckNoRec.add("Value", writeCheckNo);
		list.add(writeCheckNoRec);
		return null;
	}

	private Result writeCheckDateRequirement(Context context, ResultList list,
			Object selection) {

		Requirement dateReq = get("date");
		Date transDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("writeCheckDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setValue(transDate);
		}
		if (selection == transDate) {
			context.setAttribute(INPUT_ATTR, "writeCheckDate");
			return date(context, "Enter WriteCheck Date", transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", "WriteCheck Date");
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}
}
