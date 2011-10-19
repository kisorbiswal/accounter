package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
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
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewJournalEntryCommand extends AbstractTransactionCommand {

	private static final String VOUCHER = "Voucher";
	private static final String ACCOUNT = "Account";
	private static final String CREDIT = "Credit";
	private static final String DEBIT = "Debit";
	private static final Object ENTRIES_PROCESS = "entriesProcess";
	private static final String OLD_ENTRY_ATTR = "oldEntryAttribute";
	private static final String ENTRY_PROPERTY_ATTR = "entryPropertyAttr";
	private static final String ENTRY_DETAILS = "entryDetails";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DATE, false, true));
		list.add(new Requirement(NUMBER, false, true));
		list.add(new ObjectListRequirement(VOUCHER, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(DATE, true, true));
				list.add(new Requirement(ACCOUNT, false, true));
				list.add(new Requirement(CREDIT, false, true));
				list.add(new Requirement(DEBIT, false, true));
				list.add(new Requirement(MEMO, true, true));
			}
		});

		list.add(new Requirement(MEMO, true, true));

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
				.add(" Journal Entry is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ENTRIES_PROCESS)) {
				result = entriesProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = numberRequirement(context, list, NUMBER,
				"Please enter the  Journel Entry Number");
		if (result != null) {
			return result;
		}
		result = entryRequirement(context, makeResult, actions);
		makeResult.add(actions);
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		createJournalEntryObject(context);
		return null;
	}

	private void createJournalEntryObject(Context context) {
		ClientJournalEntry entry = new ClientJournalEntry();
		Date date = get(DATE).getValue();
		entry.setTransactionDate(date.getTime());

		String number = get(NUMBER).getValue();
		entry.setNumber(number);

		List<ClientEntry> clientEntries = get(VOUCHER).getValue();
		entry.setEntry(clientEntries);

		String memo = get(MEMO).getValue();
		entry.setMemo(memo);

		double totalDebits = 0;
		double totalCredits = 0;
		for (ClientEntry item : clientEntries) {
			totalCredits += item.getCredit();
			totalDebits += item.getDebit();
		}
		entry.setTotal(totalDebits);
		entry.setDebitTotal(totalDebits);
		entry.setCreditTotal(totalCredits);

		create(entry, context);
	}

	private Result entriesProcess(Context context) {
		ClientEntry entry = (ClientEntry) context.getAttribute(OLD_ENTRY_ATTR);
		Result result = entry(context, entry);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get("entries");
				List<ClientJournalEntry> transItems = itemsReq.getValue();
				transItems.remove(entry);
				context.removeAttribute(OLD_ENTRY_ATTR);
			}
		}
		return result;
	}

	private Result entry(Context context, ClientEntry entry) {
		context.setAttribute(PROCESS_ATTR, ENTRIES_PROCESS);
		context.setAttribute(OLD_ENTRY_ATTR, entry);

		String lineAttr = (String) context.getAttribute(ENTRY_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(ENTRY_PROPERTY_ATTR);
			if (lineAttr.equals(DATE)) {
				entry.setEntryDate(context.getDate().getTime());
			} else if (lineAttr.equals(ACCOUNT)) {
				ClientAccount account = context.getSelection(ACCOUNT);
				entry.setAccount(account.getID());
			} else if (lineAttr.equals(CREDIT)) {
				if (context.getDouble() != null) {
					entry.setCredit(context.getDouble());
				} else {
					entry.setCredit(context.getInteger().doubleValue());
				}
				entry.setDebit(0);
			} else if (lineAttr.equals(DEBIT)) {
				if (context.getDouble() != null) {
					entry.setDebit(context.getDouble());
				} else {
					entry.setDebit(context.getInteger().doubleValue());
				}
				entry.setCredit(0);
			} else if (lineAttr.equals(MEMO)) {
				entry.setMemo(context.getString());
			}
		} else {
			Object selection = context.getSelection(ENTRY_DETAILS);
			if (selection != null) {
				if (selection.equals(DATE)) {
					context.setAttribute(ENTRY_PROPERTY_ATTR, DATE);
					return date(context, "Please enter the date", new Date(
							entry.getEntryDate()));
				} else if (selection.equals(ACCOUNT)) {
					context.setAttribute(ENTRY_PROPERTY_ATTR, ACCOUNT);
					return accounts(context, ACCOUNT,
							new ListFilter<ClientAccount>() {

								@Override
								public boolean filter(ClientAccount e) {
									return true;
								}
							});
				} else if (selection.equals(CREDIT)) {
					context.setAttribute(ENTRY_PROPERTY_ATTR, CREDIT);
					return amount(context, "Please enter the credit amount",
							entry.getCredit());
				} else if (selection.equals(DEBIT)) {
					context.setAttribute(ENTRY_PROPERTY_ATTR, DEBIT);
					return amount(context, "Please enter the debit amount",
							entry.getDebit());
				} else if (selection.equals(MEMO)) {
					context.setAttribute(ENTRY_PROPERTY_ATTR, MEMO);
					return text(context, "Enter Memo", entry.getMemo());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_ENTRY_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}
		ResultList list = new ResultList(ENTRY_DETAILS);
		Record record = new Record(DATE);
		record.add("", "Date");
		record.add("", entry.getEntryDate());
		list.add(record);

		record = new Record(ACCOUNT);
		record.add("", "Account");
		record.add("", entry.getAccount());
		list.add(record);

		record = new Record(DEBIT);
		record.add("", "Debit");
		record.add("", entry.getDebit());
		list.add(record);

		record = new Record(CREDIT);
		record.add("", "Credit");
		record.add("", entry.getCredit());
		list.add(record);

		record = new Record(MEMO);
		record.add("", "Memo");
		record.add("", entry.getMemo());
		list.add(record);

		Result result = context.makeResult();
		result.add("Item details");
		result.add("Item Name :"
				+ getClientCompany().getAccount(entry.getAccount()).getName());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH_ITEM);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(MEMO).setDefaultValue("");
		Requirement requirement = get(VOUCHER);
		Object value = requirement.getValue();
		if (value == null) {
			requirement.setValue(new ArrayList<ClientEntry>());
		}
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return entries(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Result result = null;
		result = dateOptionalRequirement(context, list, DATE,
				"Enter journalEntry Date", selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create JournalEntry");
		actions.add(finish);

		return null;
	}

	private Result entryRequirement(Context context, Result result,
			ResultList actions) {
		Requirement entriesReq = get(VOUCHER);
		List<ClientEntry> values = entriesReq.getValue();
		List<ClientAccount> items = context.getSelections(VOUCHER);
		if (items != null) {
			for (ClientAccount account : items) {
				ClientEntry clientEntry = new ClientEntry();
				clientEntry.setAccount(account.getID());
				clientEntry.setMemo("");
				clientEntry.setEntryDate(new Date().getTime());
				clientEntry.setType(ClientEntry.TYPE_FINANCIAL_ACCOUNT);
				values.add(clientEntry);
			}
		}

		if (values.size() < 2) {
			return entries(context);
		}

		Object selection = context.getSelection("transactionItems");
		if (selection != null) {
			result = entry(context, (ClientEntry) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ITEMS) {
			return entries(context);
		}

		double totalDebits = 0;
		double totalCredits = 0;
		result.add("Items:-");
		ResultList itemsList = new ResultList("transactionItems");
		List<ClientEntry> transItems = entriesReq.getValue();
		for (ClientEntry item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("", new ClientFinanceDate(item.getEntryDate()));
			itemRec.add("", getClientCompany().getAccount(item.getAccount())
					.getName());
			itemRec.add("", item.getMemo());
			itemRec.add("", item.getDebit());
			itemRec.add("", item.getCredit());
			itemsList.add(itemRec);
			totalCredits += item.getCredit();
			totalDebits += item.getDebit();
		}
		result.add(itemsList);
		result.add("Total Debits:" + totalDebits);
		result.add("Total Credits:" + totalCredits);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		Record deleteItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Delete items");
		actions.add(deleteItems);
		if (totalCredits != totalDebits) {
			result.add("The Debit total and Credit totals must be same");
			return result;
		}
		if (totalCredits == 0) {
			result.add("Transaction total can not be 0 or less than 0");
			return result;
		}
		return null;
	}

	private Result entries(Context context) {
		return accounts(context, VOUCHER, new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				return true;
			}
		});
	}
}
