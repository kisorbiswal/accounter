package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
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
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewCashExpenseCommand extends AbstractTransactionCommand {

	private static final String DEPOSIT_TO = "depositOrTransferTo";
	private static final String CHEQUE_NO = "chequeNo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(SUPPLIER, false, true));
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
		list.add(new ObjectListRequirement(ACCOUNTS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(TAXCODE, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(DEPOSIT_TO, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(CHEQUE_NO, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult
				.add(" Cash Expense is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

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

		result = createSupplierRequirement(context, list, SUPPLIER);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}

		result = accountItemsRequirement(context, makeResult,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() != ClientAccount.TYPE_CASH
								&& e.getType() != ClientAccount.TYPE_BANK
								&& e.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& e.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& e.getType() != ClientAccount.TYPE_INCOME
								&& e.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& e.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& e.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& e.getType() != ClientAccount.TYPE_EQUITY
								&& e.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				}, actions);
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, DEPOSIT_TO,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET)
								.contains(e.getType())) {
							return true;
						} else {
							return false;
						}
					}
				});
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
		if (result != null) {
			return result;
		}

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
		return completeProcess(context);
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(CHEQUE_NO).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue("1");
	}

	private Result completeProcess(Context context) {
		ClientCashPurchase cashPurchase = new ClientCashPurchase();
		cashPurchase.setType(ClientTransaction.TYPE_CASH_EXPENSE);
		ClientVendor vendor = get(SUPPLIER).getValue();
		cashPurchase.setVendor(vendor.getID());
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashPurchase.setPaymentMethod(paymentMethod);
		ClientAccount account = get(DEPOSIT_TO).getValue();
		cashPurchase.setPayFrom(account.getID());
		Date date = get(DATE).getValue();
		cashPurchase.setDate(date.getTime());
		String number = get(NUMBER).getValue();
		cashPurchase.setNumber(number);
		String memoText = get(MEMO).getValue();
		cashPurchase.setMemo(memoText);
		// FIXME how to show accounts and items........
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		accounts.addAll(items);
		// ....................
		cashPurchase.setTransactionItems(accounts);
		create(cashPurchase, context);

		markDone();
		Result result = new Result();
		result.add("Cash Expense was created successfully.");
		return result;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
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

		selection = context.getSelection("values");

		Result result = dateOptionalRequirement(context, list, DATE,
				"Enter Date", selection);
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, NUMBER,
				"Please enter the cash expense number");
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

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create CashExpnse.");
		actions.add(finish);

		return makeResult;
	}

}
