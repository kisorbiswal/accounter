package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CashExpenseCommond extends AbstractTransactionCommand {

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
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("memo", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
		list.add(new Requirement("paymentmethod", false, true));
		list.add(new Requirement("chequeNo", true, true));
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

		result = createSupplierRequirement(context);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		result = accountsRequirement(context);
		if (result != null) {
			return result;
		}
		result = depositeOrTransferTo(context);
		if (result != null) {
			return result;
		}

		result = paymentMethod(context, null);
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
		CashPurchase cashPurchase = new CashPurchase();
		cashPurchase.setType(Transaction.TYPE_CASH_EXPENSE);
		Vendor vendor = get("supplier").getValue();
		cashPurchase.setVendor(vendor);
		String paymentMethod = get("paymentmethod").getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		Account account = get("depositOrTransferTo").getValue();
		cashPurchase.setPayFrom(account);
		Date date = get("date").getValue();
		cashPurchase.setDate(new FinanceDate(date));
		String number = get("number").getValue();
		cashPurchase.setNumber(number);
		String memoText = get("memo").getValue();
		cashPurchase.setMemo(memoText);
		List<TransactionItem> items = get("items").getValue();
		List<TransactionItem> accounts = get("accounts").getValue();
		accounts.addAll(items);
		cashPurchase.setTransactionItems(accounts);
		if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK) {
			cashPurchase.setTotal(getTransactionTotal(accounts, getCompany()));
		}
		create(cashPurchase, context);
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

		result = numberRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = chequeNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CashExpnse is ready to create with following values.");
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
		finish.add("", "Finish to create CashExpnse.");
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
			return number(context, "Enter  number", orderNo);
		}

		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "CashExpense No");
		orderNoRecord.add("Value", orderNo);
		list.add(orderNoRecord);
		return null;
	}

}
