package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXCode;
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

public class CashPurchaseCommand extends AbstractTransactionCommand {

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
		list.add(new Requirement("paymentMethod", false, true));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("memo", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
		list.add(new Requirement("chequeNo", true, true));
		list.add(new Requirement("deliveryDate", true, true));
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
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
		result = context.makeResult();
		result = createSupplierRequirement(context);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}
		result = accountsRequirement(context);
		if (result == null) {
			return result;
		}

		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}

		result = depositeOrTransferTo(context, "depositOrTransferTo");
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		completeProcess(context);
		markDone();
		return null;
	}

	private void completeProcess(Context context) {
		Company company = context.getCompany();
		CashPurchase cashPurchase = new CashPurchase();
		Date date = get(DATE).getValue();
		cashPurchase.setDate(new FinanceDate(date));

		cashPurchase.setType(Transaction.TYPE_CASH_PURCHASE);

		String number = get("number").getValue();
		cashPurchase.setNumber(number);

		// FIXME
		List<TransactionItem> items = get("items").getValue();
		List<TransactionItem> accounts = get("accounts").getValue();
		accounts.addAll(items);
		cashPurchase.setTransactionItems(accounts);

		// TODO Location
		// TODO Class

		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (TransactionItem item : items) {
				item.setTaxCode(taxCode);
			}
		}

		Vendor vendor = get("supplier").getValue();
		cashPurchase.setVendor(vendor);

		Contact contact = get("contact").getValue();
		cashPurchase.setContact(contact);

		// TODO Payments

		String phone = get("phone").getValue();

		String memo = get(MEMO).getValue();
		cashPurchase.setMemo(memo);

		String paymentMethod = get("paymentMethod").getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		Account account = get("depositOrTransferTo").getValue();
		cashPurchase.setPayFrom(account);
		String chequeNo = get("chequeNo").getValue();
		if (paymentMethod.equals(US_CHECK) || paymentMethod.equals(UK_CHECK)) {
			cashPurchase.setCheckNumber(chequeNo);
		}
		Date deliveryDate = get("deliveryDate").getValue();
		// cashPurchase.setD
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
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

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

		Requirement supplierReq = get("supplier");
		Vendor supplier = (Vendor) supplierReq.getValue();

		selection = context.getSelection("values");
		if (supplier == selection) {
			return createSupplierRequirement(context);
		}

		// ResultList list = new ResultList("values");

		Record supplierRecord = new Record(supplier);
		supplierRecord.add("Name", "Customer");
		supplierRecord.add("Value", supplier.getName());

		list.add(supplierRecord);

		Result result = dateRequirement(context, list, selection,
				"deliveryDate");
		if (result != null) {
			return result;
		}
		result = dateRequirement(context, list, selection, "date");
		if (result != null) {
			return result;
		}
		result = contactRequirement(context, list, selection, supplier);
		if (result != null) {
			return result;
		}

		result = cashPurchaseNoRequirement(context, list, selection);
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

		result = chequeNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CashPurchase is ready to create with following values.");
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
		finish.add("", "Finish to create CashPurchase.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result cashPurchaseNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String invoiceNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("invoiceNo")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			invoiceNo = order;
			req.setValue(invoiceNo);
		}

		if (selection == invoiceNo) {
			context.setAttribute(INPUT_ATTR, "invoiceNo");
			return number(context, "Enter Cash Purcase number", invoiceNo);
		}

		Record invoiceNoRec = new Record(invoiceNo);
		invoiceNoRec.add("Name", "CashPurcase Number");
		invoiceNoRec.add("Value", invoiceNo);
		list.add(invoiceNoRec);
		return null;
	}

	private Result dateRequirement(Context context, ResultList list,
			Object selection, String requirement) {
		Requirement req = get(requirement);
		Date dueDate = (Date) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(requirement)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
		}
		if (selection == dueDate) {
			context.setAttribute(INPUT_ATTR, requirement);
			return date(context, "Enter Date", dueDate);
		}

		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", requirement);
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);
		return null;
	}

}
