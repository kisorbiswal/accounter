package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCustomerCreditMemoCommond extends AbstractTransactionCommand {

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
		if (getClientCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
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

		result = accountItemsRequirement(context, null,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				}, null);
		if (result == null) {
			return result;
		}

		result = itemsRequirement(context, null, null);
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
		CustomerCreditMemo creditMemo = new CustomerCreditMemo();
		Company company = context.getCompany();

		Date date = get(DATE).getValue();
		creditMemo.setDate(new FinanceDate(date));

		creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);

		String number = get("number").getValue();
		creditMemo.setNumber(number);

		List<ClientTransactionItem> items = get("items").getValue();
		List<ClientTransactionItem> accounts = get("accounts").getValue();
		accounts.addAll(items);
		// creditMemo.setTransactionItems(accounts);

		// TODO Location
		// TODO Class

		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
			// TODO if (getCompany().getPreferences().isChargeSalesTax()) {
			// if (taxCode != null) {
			// for (TransactionItem record : items) {
			// record.setTaxItemGroup(taxCode.getTAXItemGrpForSales());
			// }
			// }
			// transaction.setSalesTaxAmount(this.salesTax);
			// }
		}

		Customer customer = get("customer").getValue();
		creditMemo.setCustomer(customer);

		String memo = get("reasonForIssuue").getValue();
		creditMemo.setMemo(memo);

		// creditMemo.setTotal(getTransactionTotal(accounts));
		// TODO Discount Date
		// TODO Estimates
		// TODO sales Order
		create(creditMemo, context);

	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

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
								return true;
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
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}
		Requirement accountReq = get("accounts");
		List<TransactionItem> accountItem = accountReq.getValue();

		selection = context.getSelection("accountItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement custmerReq = get("customer");
		ClientCustomer customer = (ClientCustomer) custmerReq.getValue();

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
			Object selection, ClientCustomer customer) {
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
