package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewCustomerCreditMemoCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

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
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement("reasonForIssuue", true, true));
		list.add(new Requirement(TAXCODE, false, true));

	}

	@Override
	public Result run(Context context) {
		setDefaultValues(context);
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
		makeResult.add(getMessages().readyToCreate(
				getMessages().customerCreditNote(Global.get().customer())));
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
								&& account.getType() != ClientAccount.TYPE_EXPENSE
								&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
								&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						} else {
							return false;
						}
					}
				});
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

		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		completeProcess(context);
		markDone();
		result = new Result();
		result.add(getMessages().finishToCreate(
				getMessages().customerCreditNote(Global.get().customer())));
		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
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
				getConstants().transactionDate(),
				getMessages().pleaseEnter(getConstants().transactionDate()),
				selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getConstants().creditNoteNo(),
				getMessages().pleaseEnter(getConstants().creditNoteNo()));
		if (result != null) {
			return result;
		}

		Requirement supplierReq = get("customer");
		ClientCustomer customer = (ClientCustomer) supplierReq.getValue();
		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				getConstants().reasonForIssue(), "reasonForIssuue",
				getConstants().reasonForIssue());
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add(
				"",
				getMessages().finishToCreate(
						getMessages().customerCreditNote(
								Global.get().customer())));
		actions.add(finish);

		return makeResult;
	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						context.getCompany()));
		get(CONTACT).setDefaultValue(new ClientContact());
		get("reasonForIssuue").setDefaultValue("");

	}

	private void completeProcess(Context context) {

		ClientCustomerCreditMemo creditMemo = new ClientCustomerCreditMemo();

		ClientFinanceDate date = get(DATE).getValue();
		creditMemo.setDate(date.getDate());

		creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);

		String number = get(NUMBER).getValue();
		creditMemo.setNumber(number);

		List<ClientTransactionItem> items = get("items").getValue();
		List<ClientTransactionItem> accounts = get("accounts").getValue();
		accounts.addAll(items);

		// TODO Location
		// TODO Class

		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			TAXCode taxCode = get("tax").getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}

		}
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		creditMemo.setTransactionItems(accounts);
		ClientCustomer customer = get("customer").getValue();
		creditMemo.setCustomer(customer.getID());

		String memo = get("reasonForIssuue").getValue();
		creditMemo.setMemo(memo);
		updateTotals(creditMemo);
		create(creditMemo, context);

	}

}
