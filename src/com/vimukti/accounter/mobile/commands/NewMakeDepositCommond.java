package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewMakeDepositCommond extends AbstractTransactionCommand {
	private static final String TRANSFERED_ACCOUNT = "transferedAccount";
	private static final String TRANSFERED_TO = "transferedTo";
	private static final String OLD_DEPOSITE_TRANSACTION_ITEM_ATTR = null;
	private static final Object DEPOSITE_TRANSACTION_PROCESS = null;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(TRANSFERED_TO, false, true));
		list.add(new ObjectListRequirement(TRANSFERED_ACCOUNT, false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("accountName", false, true));
				list.add(new Requirement("reference", true, true));
				list.add(new Requirement("amount", false, true));
			}
		});
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();
		if (process != null) {
			if (process.equals(DEPOSITE_TRANSACTION_PROCESS)) {
				result = depositTransactionProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		// Preparing result
		Result makeResult = context.makeResult();
		makeResult
				.add("Transfer Funds Transaction is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		setTransactionType(ClientTransaction.TYPE_MAKE_DEPOSIT);
		result = createSupplierRequirement(context, list, SUPPLIER);
		if (result != null) {
			return result;
		}

		// result = depositeOrTransferTo(context, TRANSFERED_TO);
		if (result != null) {
			return result;
		}
		result = transferdAccountRequiremnet(context, TRANSFERED_ACCOUNT);
		if (result != null) {
			return result;
		}
		result = createOptional(context);
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
		get(MEMO).setDefaultValue("");
	}

	private void completeProcess(Context context) {

		Company company = context.getCompany();
		MakeDeposit makeDeposit = new MakeDeposit();

		Date date = get(DATE).getValue();
		makeDeposit.setDate(new FinanceDate(date));

		makeDeposit.setType(Transaction.TYPE_MAKE_DEPOSIT);

		String number = get("number").getValue();
		makeDeposit.setNumber(number);

		List<TransactionItem> items = get(TRANSFERED_TO).getValue();
		makeDeposit.setTransactionItems(items);
		List<TransactionMakeDeposit> list = get(TRANSFERED_ACCOUNT).getValue();
		makeDeposit.setTransactionMakeDeposit(list);

		// TODO Location
		// TODO Class

		makeDeposit.setTotal(getTransactionMakeDepositTotal(list, company));

		// TODO Payments

		String memo = get(MEMO).getValue();
		makeDeposit.setMemo(memo);

		// TODO Discount Date
		// TODO Estimates
		// TODO sales Order
		create(makeDeposit, context);

	}

	private double getTransactionMakeDepositTotal(
			List<TransactionMakeDeposit> items, Company company) {
		Double totallinetotal = 0.0;
		for (TransactionMakeDeposit citem : items) {
			Double lineTotalAmt = citem.getAmount();
			totallinetotal += lineTotalAmt;
		}
		return totallinetotal;
	}

	private Result createOptional(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ACCOUNTS:
				return accounts(context, TRANSFERED_ACCOUNT,
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
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");
		Requirement transferTo = get(TRANSFERED_TO);
		Account account = transferTo.getValue();
		Record nameRec = new Record(account);
		nameRec.add("Account name", "Account Name");
		nameRec.add("value", account.getName());
		Record accountRec = new Record(account);
		accountRec.add("Account type", "Account Type");
		accountRec.add("Account Type", getAccountTypeString(account.getType()));
		list.add(nameRec);
		list.add(accountRec);

		Result result = dateRequirement(context, list, selection, DATE,
				"Enter the date");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				NUMBER);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("MakeDeposite is ready to create with following values.");
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Transaction.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result depositTransactionProcess(Context context) {
		TransactionMakeDeposit transactionItem = (TransactionMakeDeposit) context
				.getAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR);
		Result result = depositTransaction(context, transactionItem);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get(TRANSFERED_ACCOUNT);
				List<TransactionItem> transItems = itemsReq.getValue();
				transItems.remove(transactionItem);
				context.removeAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR);
			}
		}
		return result;
	}

	private Result depositTransaction(Context context,
			TransactionMakeDeposit transactionItem) {
		context.setAttribute(PROCESS_ATTR, DEPOSITE_TRANSACTION_PROCESS);
		context.setAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR,
				transactionItem);

		String lineAttr = (String) context.getAttribute(ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ITEM_PROPERTY_ATTR);
			if (lineAttr.equals("reference")) {
				// transactionItem.getre
			} else if (lineAttr.equals("amount")) {
				transactionItem.setAmount(context.getDouble());
			}
		} else {
			Object selection = context.getSelection(ITEM_DETAILS);
			if (selection != null) {
				if (selection == transactionItem.getReference()) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "reference");
					return reference(context, "Enter Reference",
							transactionItem.getReference());
				} else if (selection.equals("amount")) {
					context.setAttribute(ITEM_PROPERTY_ATTR, "amount");
					return amount(context, "Enter Amount",
							transactionItem.getAmount());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}

		ResultList list = new ResultList(ITEM_DETAILS);
		Record record = new Record(transactionItem.getReference());
		record.add("", "Reference");
		record.add("", transactionItem.getReference());
		list.add(record);

		record = new Record("amount");
		record.add("", "Amount");
		record.add("", transactionItem.getAmount());
		list.add(record);
		Result result = context.makeResult();
		result.add("Transaction details");
		result.add("Transaction Name :"
				+ transactionItem.getAccount().getName());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}

	private Result reference(Context context, String string, String reference) {
		Result result = context.makeResult();
		result.add(string);
		if (reference != null) {
			ResultList list = new ResultList(NUMBER);
			Record record = new Record(reference);
			record.add("", reference);
			list.add(record);
			result.add(list);
		}
		return result;
	}

	private Result transferdAccountRequiremnet(Context context,
			String transferedAccount) {
		Requirement itemsReq = get(transferedAccount);
		List<TransactionItem> transactionItems = context
				.getSelections(transferedAccount);

		if (!itemsReq.isDone()) {
			if (transactionItems.size() > 0) {
				itemsReq.setValue(transactionItems);
			} else {
				return transferedAccount(context, transferedAccount);
			}
		}
		if (transactionItems != null && transactionItems.size() > 0) {
			List<TransactionItem> items = itemsReq.getValue();
			items.addAll(transactionItems);
		}
		return null;
	}

	private Result transferedAccount(Context context, String transferedAccount) {

		return null;
	}
}
