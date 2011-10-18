package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashPurchaseCommand extends AbstractTransactionCommand {

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
		list.add(new Requirement("Payment method", false, true));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(MEMO, true, true));
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
		result = createSupplierRequirement(context, null, null);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context, null, null);
		if (result != null) {
			return result;
		}
		result = accountItemsRequirement(context, null,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return e.getIsActive();
					}
				}, null);
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context,null, null);
		if (result != null) {
			return result;
		}

		result = depositeOrTransferTo(context, "depositOrTransferTo");
		if (result != null) {
			return result;
		}
		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void setDefaultValues() {
		get("date").setDefaultValue(new Date());
		get("deliveryDate").setDefaultValue(new Date());
		get("number").setDefaultValue("1");
		get("Payment method").setDefaultValue("cash");
	}

	private void completeProcess(Context context) {
		ClientCashPurchase cashPurchase = new ClientCashPurchase();
		Date date = get("date").getValue();
		cashPurchase.setDate(date.getTime());

		cashPurchase.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		String number = get("number").getValue();
		cashPurchase.setNumber(number);

		// FIXME
		List<ClientTransactionItem> items = get("items").getValue();
		for (ClientTransactionItem item : items) {
			ClientItem clientItem = getClientCompany().getItem(item.getItem());
			ClientAccount account = getClientCompany().getAccount(
					clientItem.getIncomeAccount());
			if (account != null) {
				clientItem.setIncomeAccount(account.getID());
			} else {
				account = getClientCompany().getAccount(
						clientItem.getExpenseAccount());
				clientItem.setExpenseAccount(account.getID());
			}
		}

		List<ClientTransactionItem> accounts = get("accounts").getValue();
		for (ClientTransactionItem item : accounts) {
			ClientAccount account = getClientCompany().getAccount(
					item.getAccount());
			item.setAccount(account.getID());
		}

		accounts.addAll(items);
		cashPurchase.setTransactionItems(accounts);

		// TODO Location
		// TODO Class

		if (getClientCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			ClientTAXCode taxCode = get("tax").getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		ClientVendor vendor = get("supplier").getValue();
		cashPurchase.setVendor(vendor.getID());

		ClientContact contact = get("contact").getValue();
		cashPurchase.setContact(contact);

		// TODO Payments

		String phone = get("phone").getValue();

		String memo = get(MEMO).getValue();
		cashPurchase.setMemo(memo);

		String paymentMethod = get("Payment method").getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		ClientAccount account = get("depositOrTransferTo").getValue();
		cashPurchase.setPayFrom(account.getID());
		String chequeNo = get("chequeNo").getValue();
		if (paymentMethod.equals(US_CHECK) || paymentMethod.equals(UK_CHECK)) {
			cashPurchase.setCheckNumber(chequeNo);
		}
		Date deliveryDate = get("deliveryDate").getValue();
		// TODO cashPurchase.setD
		updateTotals(cashPurchase);
		create(cashPurchase, context);

	}

	private Result createOptionalResult(Context context) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case ADD_MORE_ACCOUNTS:
				return accountItems(context, "accounts",
						new ListFilter<ClientAccount>() {
							@Override
							public boolean filter(ClientAccount e) {
								return e.getIsActive();
							}
						});
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		Requirement itemsReq = get("items");
		List<ClientTransactionItem> transItems = itemsReq.getValue();
		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		Requirement accountReq = get("accounts");
		List<ClientTransactionItem> accountItem = accountReq.getValue();

		selection = context.getSelection("accountItems");
		if (selection != null) {
			Result result = transactionAccountItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		ResultList list = new ResultList("values");

		Requirement transferTo = get("depositOrTransferTo");
		ClientAccount account = transferTo.getValue();
		Record accountRec = new Record(account);
		accountRec.add("Number", "Account Number");
		accountRec.add("Number", account.getNumber());
		accountRec.add("Account name", "Account Name");
		accountRec.add("Account name", account.getNumber());
		accountRec.add("Account Type", "Account Type");
		accountRec.add("Account Type", getAccountTypeString(account.getType()));
		list.add(accountRec);

		Requirement supplierReq = get("supplier");
		ClientVendor supplier = (ClientVendor) supplierReq.getValue();

		selection = context.getSelection("values");
		if (supplier == selection) {
			return createSupplierRequirement(context, null, null);
		}

		Record supplierRecord = new Record(supplier);
		supplierRecord.add("Name", "Vendor Name:");
		supplierRecord.add("Value", supplier.getName());
		list.add(supplierRecord);

		Result result = dateOptionalRequirement(context, list, "deliveryDate",
				"Enter date", selection);
		if (result != null) {
			return result;
		}
		// result = contactRequirement(context, list, selection, supplier);
		// if (result != null) {
		// return result;
		// }

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"Enter cash purchase number");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "date", "Enter date",
				selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter phone number");
		if (result != null) {
			return result;
		}

		result = chequeNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Add a memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("CashPurchase is ready to create with following values.");
		result.add(list);
		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (ClientTransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", "Item Name:");
			itemRec.add("Name", getClientCompany().getItem(item.getItem())
					.getName());
			itemRec.add("Total", "Item Total:");
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", "VatCode:");
			itemRec.add("VatCode", item.getVATfraction());
			items.add(itemRec);
		}
		result.add(items);
		ResultList accountItems = new ResultList("accountItems");
		for (ClientTransactionItem item : accountItem) {
			Record accountRecord = new Record(item);
			accountRecord.add("Name", "Account Name:");
			accountRecord.add("Name",
					getClientCompany().getAccount(item.getAccount()).getName());
			accountRecord.add("Total", "Total:");
			accountRecord.add("Total", item.getLineTotal());
			accountItems.add(accountRecord);
		}
		result.add(accountItems);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record moreAccounts = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", "Add more accounts");
		actions.add(moreAccounts);
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
		String attribute = (String) context.getString();
		if (attribute != null) {
			invoiceNo = attribute;
			req.setValue(invoiceNo);
		}
		if (selection == invoiceNo) {
			context.setAttribute(INPUT_ATTR, "invoiceNo");
			return number(context, "Enter Cash Purcase number", null);
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
