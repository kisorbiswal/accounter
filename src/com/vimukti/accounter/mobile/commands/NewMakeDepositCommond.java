package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewMakeDepositCommond extends AbstractTransactionCommand {
	private static final String TRANSFERED_ACCOUNT = "transferedAccount";
	private static final String OLD_DEPOSITE_TRANSACTION_ITEM_ATTR = "oldDepositTransctionItemProcess";
	private static final String DEPOSITE_TRANSACTION_PROCESS = "depositTransactionProcess";
	private static final String MAKE_DEPOSITE_ACCOUNT_ITEM_DETAILS = "makeAdepositeAccountDetails";
	private static final String MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR = "makeadepositeaccountitempropertyattr";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(DEPOSIT_OR_TRANSFER_TO, false, true));
		list.add(new ObjectListRequirement(TRANSFERED_ACCOUNT, false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("receivedFrom", true, true));
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
		makeResult.add(getMessages()
				.readyToCreate(getConstants().makeDeposit()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		setTransactionType(ClientTransaction.TYPE_MAKE_DEPOSIT);

		result = accountRequirement(context, list, DEPOSIT_OR_TRANSFER_TO,
				getConstants().account(), new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return account.getIsActive()
								&& (Arrays
										.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
												ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
												ClientAccount.TYPE_BANK,
												ClientAccount.TYPE_EQUITY)
										.contains(account.getType()));
					}
				});
		if (result != null) {
			return result;
		}

		result = makeDepositTransactionAccountsRequirement(context, makeResult,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
								ClientAccount.TYPE_EQUITY).contains(
								account.getType());
					}
				}, actions);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptional(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().makeDeposit()));
		return result;
	}

	private Result makeDepositTransactionAccountsRequirement(Context context,
			Result result, ListFilter<ClientAccount> listFilter,
			ResultList actions) {
		Requirement transItemsReq = get(TRANSFERED_ACCOUNT);
		List<ClientAccount> accounts = context
				.getSelections(TRANSFERED_ACCOUNT);

		if (accounts != null && accounts.size() > 0) {
			for (ClientAccount account : accounts) {
				ClientTransactionMakeDeposit transactionItem = new ClientTransactionMakeDeposit();
				transactionItem
						.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
				transactionItem.setAccount(account.getID());
				List<ClientTransactionMakeDeposit> transactionItems = transItemsReq
						.getValue();
				if (transactionItems == null) {
					transactionItems = new ArrayList<ClientTransactionMakeDeposit>();
					transItemsReq.setValue(transactionItems);
				}
				transactionItems.add(transactionItem);
				if (transactionItem.getAmount() == 0) {
					context.putSelection(MAKE_DEPOSITE_ACCOUNT_ITEM_DETAILS,
							"amount");
					Result transactionItemResult = makeADepositAccountItem(
							context, transactionItem);
					if (transactionItemResult != null) {
						return transactionItemResult;
					}
				}
			}
		}

		if (!transItemsReq.isDone()) {
			return makeDepositItems(context, TRANSFERED_ACCOUNT, listFilter);
		}

		Object selection = context.getSelection("accountItems");
		if (selection != null) {
			result = transactionAccountItem(context,
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ACCOUNTS) {
			return accounts(context, TRANSFERED_ACCOUNT, listFilter);
		}

		List<ClientTransactionMakeDeposit> accountTransItems = transItemsReq
				.getValue();
		ResultList accountItems = new ResultList("accountItems");
		result.add(getMessages()
				.accountTransactionItems(Global.get().Account()));
		for (ClientTransactionMakeDeposit item : accountTransItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", getClientCompany()
					.getAccount(item.getAccount()).getName());
			itemRec.add("Amount", item.getAmount());
			itemRec.add("Reference", item.getReference());
			itemRec.add("Total", item.getAmount());
			accountItems.add(itemRec);
		}
		result.add(accountItems);

		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(getConstants().Accounts()));
		actions.add(moreItems);
		return null;
	}

	private Result makeDepositItems(Context context, String label,
			ListFilter<ClientAccount> listFilter) {
		Result result = context.makeResult();
		ArrayList<ClientAccount> accounts = getAccounts(listFilter);
		accounts = new ArrayList<ClientAccount>(accounts);
		Collections.sort(accounts, new Comparator<ClientAccount>() {

			@Override
			public int compare(ClientAccount o1, ClientAccount o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		ResultList list = new ResultList(label);
		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			list.add(createAccountRecord(context, (ClientAccount) last));
			num++;
		}
		Requirement itemsReq = get(label);
		List<ClientTransactionMakeDeposit> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTransactionMakeDeposit>();
		}
		List<Long> availableAccounts = new ArrayList<Long>();
		for (ClientTransactionMakeDeposit transactionItem : transItems) {
			availableAccounts.add(transactionItem.getAccount());
		}
		for (ClientAccount account : accounts) {
			if (account != last || !availableAccounts.contains(account.getID())) {
				list.add(createAccountRecord(context, account));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(getConstants().account()));
		} else {
			result.add(getMessages().noRecordsToShow());
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add(getMessages().addanewAccount(getConstants().account()));
		result.add(commands);
		return result;
	}

	private Result makeADepositAccountItem(Context context,
			ClientTransactionMakeDeposit transactionItem) {
		context.setAttribute(PROCESS_ATTR, DEPOSITE_TRANSACTION_PROCESS);
		context.setAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR,
				transactionItem);

		String lineAttr = (String) context
				.getAttribute(MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR);
			if (lineAttr.equals("amount")) {
				if (context.getDouble() != null) {
					transactionItem.setAmount(context.getDouble());
				} else {
					transactionItem.setAmount(context.getInteger()
							.doubleValue());
				}
			} else if (lineAttr.equals("reference")) {
				if (context.getString() != null) {
					transactionItem.setReference(context.getString());
				}
			}
		} else {
			Object selection = context
					.getSelection(MAKE_DEPOSITE_ACCOUNT_ITEM_DETAILS);
			if (selection != null) {
				if (selection.equals("amount")) {
					context.setAttribute(
							MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR, "amount");
					return amount(context,
							getMessages().pleaseEnter(getConstants().amount()),
							transactionItem.getAmount());
				} else if (selection.equals("reference")) {
					context.setAttribute(
							MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR,
							"reference");
					return number(
							context,
							getMessages().pleaseEnter(
									getConstants().reference()),
							transactionItem.getReference());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					if (transactionItem.getAmount() == 0) {
						context.setAttribute(
								MAKE_DEPOSITE_ACCOUNT_ITEM_PROPERTY_ATTR,
								"amount");
						return amount(
								context,
								getMessages().pleaseEnter(
										getConstants().amount()),
								transactionItem.getAmount());
					}
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}
		ResultList list = new ResultList(MAKE_DEPOSITE_ACCOUNT_ITEM_DETAILS);
		Record record = new Record("amount");
		record.add("", getConstants().amount());
		record.add("", transactionItem.getAmount());
		list.add(record);

		record = new Record("reference");
		record.add("", getConstants().reference());
		record.add("", transactionItem.getReference());
		list.add(record);

		// TODO NEED TO CALCULATE LINE TOTAL
		// double lineTotal = transactionItem.getUnit()
		// - ((transactionItem.getUnitPrice() * transactionItem
		// .getDiscount()) / 100);
		// transactionItem.setLineTotal(lineTotal);
		Result result = context.makeResult();
		result.add(getMessages().account(getConstants().details()));
		ClientAccount account = getClientCompany().getAccount(
				transactionItem.getAccount());
		if (account != null) {
			result.add(getMessages().account(getConstants().name())
					+ account.getName());
		}
		// double lt = transactionItem.getUnitPrice();
		// double disc = transactionItem.getDiscount();
		// transactionItem
		// .setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
		// * disc / 100)) : lt);
		result.add(getConstants().totalAmount() + transactionItem.getAmount());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", getConstants().delete());
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", getConstants().finish());
		actions.add(record);
		result.add(actions);
		return result;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(NUMBER).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
	}

	private void completeProcess(Context context) {

		ClientMakeDeposit makeDeposit = new ClientMakeDeposit();

		Date date = get(DATE).getValue();
		makeDeposit.setDate(new FinanceDate(date).getDate());

		makeDeposit.setType(ClientTransaction.TYPE_MAKE_DEPOSIT);

		String number = get("number").getValue();
		makeDeposit.setNumber(number);

		ClientAccount account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		makeDeposit.setDepositIn(account.getID());

		List<ClientTransactionMakeDeposit> list = get(TRANSFERED_ACCOUNT)
				.getValue();
		makeDeposit.setTransactionMakeDeposit(list);

		String memo = get(MEMO).getValue();
		makeDeposit.setMemo(memo);
		caluclateTotals(makeDeposit);
		create(makeDeposit, context);

	}

	private void caluclateTotals(ClientMakeDeposit makeDeposit) {
		List<ClientTransactionMakeDeposit> allrecords = makeDeposit
				.getTransactionMakeDeposit();
		double lineTotal = 0.0;
		double totalTax = 0.0;

		for (ClientTransactionMakeDeposit record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;

			Double lineTotalAmt = record.getAmount();
			lineTotal += lineTotalAmt;

		}

		double grandTotal = totalTax + lineTotal;

		makeDeposit.setTotal(grandTotal);

	}

	private Result createOptional(Context context, ResultList list,
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
				getConstants().date(),
				getMessages().pleaseEnter(getConstants().date()), selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(
				context,
				list,
				selection,
				NUMBER,
				getConstants().makeDeposit() + getConstants().number(),
				getMessages().pleaseEnter(
						getConstants().makeDeposit() + getConstants().number()));
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().addMemo(),
				getMessages().pleaseEnter(getConstants().addMemo()));

		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().makeDeposit()));
		actions.add(finish);

		return makeResult;
	}

	private Result depositTransactionProcess(Context context) {
		ClientTransactionMakeDeposit transactionItem = (ClientTransactionMakeDeposit) context
				.getAttribute(OLD_DEPOSITE_TRANSACTION_ITEM_ATTR);
		Result result = makeADepositAccountItem(context, transactionItem);
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

	private Record createAccountRecord(Context context, ClientAccount account) {
		Record record = new Record(account);
		record.add("Account Name", getMessages().account(getConstants().name()));
		record.add("Account Name value", account.getName());
		record.add("Account Balance", getConstants().currentBalance());
		record.add("Current Balance", account.getCurrentBalance());
		record.add("Account Type", getMessages().account(getConstants().type()));
		record.add("Account Type Value",
				getAccountTypeString(account.getType()));
		return record;
	}
}
