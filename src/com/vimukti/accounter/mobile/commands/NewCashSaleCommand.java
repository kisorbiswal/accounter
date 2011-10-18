package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashSaleCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";

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
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(PAYMENT_MENTHOD, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement("memo", true, true));
		list.add(new Requirement("depositOrTransferTo", false, true));
		list.add(new Requirement(TAXCODE, false, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();
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

		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add("Cash sale is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		setTransactionType(CUSTOMER_TRANSACTION);
		result = customerRequirement(context, list, "customer");
		if (result != null) {
			return result;
		}

		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				});
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_MENTHOD);
		if (result != null) {
			return result;
		}

		result = depositeOrTransferTo(context, "depositOrTransferTo");
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			result = taxCodeRequirement(context, list);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(NUMBER).setDefaultValue("1");
		get(PHONE).setDefaultValue("");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
		get(PAYMENT_MENTHOD).setDefaultValue("Cash");

	}

	private void completeProcess(Context context) {

		Company company = context.getCompany();
		ClientCashSales cashSale = new ClientCashSales();
		Date date = get(DATE).getValue();
		cashSale.setDate(date.getTime());

		cashSale.setType(Transaction.TYPE_CASH_SALES);

		String number = get("number").getValue();
		cashSale.setNumber(number);

		// FIXME
		List<ClientTransactionItem> items = get("items").getValue();
		List<ClientTransactionItem> accounts = get("accounts").getValue();
		accounts.addAll(items);
		cashSale.setTransactionItems(accounts);

		// TODO Location
		// TODO Class

		ClientCustomer customer = get("customer").getValue();
		cashSale.setCustomer(customer.getID());

		ClientContact contact = get("contact").getValue();
		cashSale.setContact(contact);

		// TODO Payments

		String phone = get(PHONE).getValue();

		String memo = get(MEMO).getValue();
		cashSale.setMemo(memo);

		String paymentMethod = get(PAYMENT_MENTHOD).getValue();
		cashSale.setPaymentMethod(paymentMethod);
		ClientAccount account = get("depositOrTransferTo").getValue();
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		cashSale.setDepositIn(account.getID());
		// if (context.getCompany())
		// ClientTAXCode taxCode = get(TAXCODE).getValue();
		// cashSale.setTaxTotal(getTaxTotal(accounts, taxCode));
		updateTotals(cashSale);
		create(cashSale, context);

	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = dateOptionalRequirement(context, list, DATE,
				"Enter date", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter Cash Sale Number");
		if (result != null) {
			return result;
		}
		Requirement customerReq = get("customer");
		ClientCustomer customer = customerReq.getValue();
		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				"Enter Phone Number");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Supplier Credit.");
		actions.add(finish);

		return makeResult;
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
