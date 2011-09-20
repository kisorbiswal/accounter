package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;

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
		Result result = customerRequirement(context);
		if (result == null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result == null) {
			return result;
		}

		result = depositeOrTransferTo(context);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result depositeOrTransferTo(Context context) {
		Requirement transferedReq = get("depositOrTransferTo");
		Account account = context.getSelection("depositOrTransferTo");
		if (!transferedReq.isDone()) {
			if (account != null) {
				transferedReq.setValue(account);
			} else {
				return accounts(context);
			}
		}
		if (account != null) {
			transferedReq.setValue(account);

		}
		return null;
	}

	private Result accounts(Context context) {
		Result result = context.makeResult();
		ResultList list = new ResultList("depositOrTransferTo");

		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(createAccountRecord((Account) last));
			num++;
		}

		List<Account> transferAccountList = getAccounts(context.getSession());
		for (Account account : transferAccountList) {
			if (account != last) {
				list.add(createAccountRecord(account));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}

		if (list.size() > 0) {
			result.add("Slect an Account.");
		}
		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New Account");
		return result;
	}

	private Record createAccountRecord(Account last) {
		Record record = new Record(last);
		record.add("Account Number", last.getNumber());
		record.add("Account Name", last.getName());
		record.add("Account Type", getAccountTypeString(last.getType()));
		return record;
	}

	private List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return null;
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

		result = paymentMethodRequirement(context, (String) selection);
		if (result != null) {
			return result;
		}

		result = memoRequirement(context, list, selection);
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

	private Result phoneRequirement(Context context, ResultList list,
			String selection) {
		Result result = context.makeResult();
		Requirement req = get("phone");
		String phoneNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("phone")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			phoneNo = order;
			req.setDefaultValue(phoneNo);
		}

		if (selection == phoneNo) {
			context.setAttribute(INPUT_ATTR, "phone");
			return number(context, "Enter Phone number", phoneNo);
		}

		Record cashSaleNoRec = new Record(phoneNo);
		cashSaleNoRec.add("Name", "Phone Number");
		cashSaleNoRec.add("Value", phoneNo);
		list.add(cashSaleNoRec);
		result.add(list);
		return result;
	}

	private Result accountItem(Context context, Account accountItem) {
		context.setAttribute(PROCESS_ATTR, TRANSACTION_ITEM_PROCESS);
		context.setAttribute(OLD_TRANSACTION_ITEM_ATTR, accountItem);

		// String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		// if (lineAttr != null) {
		// context.removeAttribute(ITEM_PROPERTY_ATTR);
		// if (lineAttr.equals("account no")) {
		// accountItem.setNumber(context.getString());
		// } else if (lineAttr.equals("unitPrice")) {
		// transactionItem.setUnitPrice(context.getDouble());
		// } else if (lineAttr.equals("discount")) {
		// transactionItem.setDiscount(context.getDouble());
		// } else if (lineAttr.equals("taxCode")) {
		// TAXCode taxCode = context.getSelection(TAXCODE);
		// transactionItem.setTaxCode(taxCode);
		// }
		// } else {
		// Object selection = context.getSelection(ITEM_DETAILS);
		// if (selection != null) {
		// if (selection == transactionItem.getQuantity()) {
		// context.setAttribute(ITEM_PROPERTY_ATTR, "quantity");
		// return amount(context, "Enter Quantity", transactionItem
		// .getQuantity().getValue());
		// } else if (selection.equals("unitPrice")) {
		// context.setAttribute(ITEM_PROPERTY_ATTR, "unitPrice");
		// return amount(context, "Enter Unitprice",
		// transactionItem.getUnitPrice());
		// } else if (selection.equals("discount")) {
		// context.setAttribute(ITEM_PROPERTY_ATTR, "discount");
		// return amount(context, "Enter Discount",
		// transactionItem.getDiscount());
		// } else if (selection == transactionItem.getTaxCode().getName()) {
		// context.setAttribute(ITEM_PROPERTY_ATTR, "taxCode");
		// return taxCode(context, transactionItem.getTaxCode());
		// } else if (selection.equals("Tax")) {
		// transactionItem.setTaxable(!transactionItem.isTaxable());
		// }
		// } else {
		// selection = context.getSelection(ACTIONS);
		// if (selection == ActionNames.FINISH) {
		// context.removeAttribute(PROCESS_ATTR);
		// context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
		// return null;
		// } else if (selection == ActionNames.DELETE_ITEM) {
		// context.removeAttribute(PROCESS_ATTR);
		// return null;
		// }
		// }
		// }

		// ResultList list = new ResultList(ITEM_DETAILS);
		// Record record = new Record(transactionItem.getQuantity());
		// record.add("", "Quantity");
		// record.add("", transactionItem.getQuantity());
		// list.add(record);
		//
		// record = new Record("unitPrice");
		// record.add("", "Unit Price");
		// record.add("", transactionItem.getUnitPrice());
		// list.add(record);
		//
		// record = new Record("discount");
		// record.add("", "Discount %");
		// record.add("", transactionItem.getDiscount());
		// list.add(record);
		//
		// Company company = getCompany();
		// if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		// record = new Record(transactionItem.getTaxCode().getName());
		// record.add("", "VatCode");
		// record.add("", transactionItem.getTaxCode().getName());
		// list.add(record);
		// } else {
		// record = new Record("Tax");
		// record.add("", "Tax");
		// if (transactionItem.isTaxable()) {
		// record.add("", "Taxable");
		// } else {
		// record.add("", "Non-Taxable");
		// }
		// list.add(record);
		// }
		//
		// Result result = context.makeResult();
		// result.add("Item details");
		// result.add("Item Name :" + transactionItem.getItem().getName());
		// result.add(list);
		// if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		// result.add("Item Vat :" + transactionItem.getVATfraction());
		// }
		// result.add("Item Total :" + transactionItem.getLineTotal());
		//
		// ResultList actions = new ResultList(ACTIONS);
		// record = new Record(ActionNames.DELETE_ITEM);
		// record.add("", "Delete");
		// actions.add(record);
		// record = new Record(ActionNames.FINISH);
		// record.add("", "Finish");
		// actions.add(record);
		// result.add(actions);
		return null;
	}

	private Result memoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("memo");
		String memo = (String) req.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("memo")) {
			String order = context.getSelection(TEXT);
			if (order == null) {
				order = context.getString();
			}
			memo = order;
			req.setDefaultValue(memo);
		}

		if (selection == memo) {
			context.setAttribute(attribute, "memo");
			return text(context, "Enter CashSale memo", memo);
		}

		if (selection == memo) {
			return text(context, "orderNo", memo);
		}

		Record memoRecord = new Record(memo);
		memoRecord.add("Name", "Order No");
		memoRecord.add("Value", memo);
		list.add(memoRecord);
		return null;
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
			req.setDefaultValue(cashSaleNo);
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
		Date transDate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("Cash Sale Date")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setDefaultValue(transDate);
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

	private String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_INCOME;
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_INCOME;
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_EXPENSE;
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_EXPENSE;
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = AccounterClientConstants.TYPE_COST_OF_GOODS_SOLD;
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = AccounterClientConstants.TYPE_CASH;
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = AccounterClientConstants.TYPE_BANK;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_ASSET;
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_INVENTORY_ASSET;
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_ASSET;
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_FIXED_ASSET;
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = AccounterClientConstants.TYPE_CREDIT_CARD;
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = AccounterClientConstants.TYPE_PAYPAL;
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_LIABILITY;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = AccounterClientConstants.TYPE_EQUITY;
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_RECEIVABLE;
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_PAYABLE;
			break;

		}
		return accountTypeName;
	}

}
