package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AbstractTableRequirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */

public class NewJournalEntryCommand extends NewAbstractTransactionCommand {

	private static final String VOUCHER = "Voucher";
	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String MEMO = "memo";
	protected static final String ACCOUNT = "Account";
	protected static final String DEBITS = "Debits";
	protected static final String CREDITS = "Credits";
	ClientJournalEntry entry;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().journalEntryDate()), getMessages()
				.journalEntryDate(), false, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().journalEntryNo()),
				getMessages().journalEntryNo(), false, true));

		list.add(new AbstractTableRequirement<ClientEntry>(VOUCHER,
				getMessages().pleaseSelect(getMessages().voucherNo()),
				getMessages().voucher(), true, false, true) {

			@Override
			protected void addRequirement(List<Requirement> list) {
				list.add(new AccountRequirement(ACCOUNT, getMessages()
						.pleaseEnterNameOrNumber(getMessages().Account()),
						getMessages().Account(), false, true, null) {

					@Override
					protected String getSetMessage() {
						return getMessages().hasSelected(
								getMessages().Account());
					}

					@Override
					protected List<Account> getLists(Context context) {
						List<Account> filteredList = new ArrayList<Account>();
						for (Account obj : context.getCompany().getAccounts()) {
							if (new ListFilter<Account>() {

								@Override
								public boolean filter(Account e) {
									return e.getIsActive();
								}
							}.filter(obj)) {
								filteredList.add(obj);
							}
						}
						return filteredList;
					}

					@Override
					protected String getEmptyString() {
						return getMessages().youDontHaveAny(
								getMessages().Accounts());
					}
				});

				list.add(new AmountRequirement(CREDITS, getMessages()
						.pleaseEnter(getMessages().creditAmount()),
						getMessages().credit(), true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						if (value != null && currentValue != null) {
							currentValue.setCredit((Double) value);
							currentValue.setDebit(0.0d);
						}
					}

					@Override
					public Double getValue() {
						return currentValue.getCredit();
					}

					@Override
					protected void createRecord(ResultList list) {
					}
				});

				list.add(new AmountRequirement(DEBITS, getMessages()
						.pleaseEnter(getMessages().debitAmount()),
						getMessages().debit(), true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						if (value != null && currentValue != null) {
							currentValue.setDebit((Double) value);
							currentValue.setCredit(0.0d);
						}
					}

					@Override
					public Double getValue() {
						return currentValue.getDebit();
					}

					@Override
					protected void createRecord(ResultList list) {
						Record nameRecord = new Record(CREDITS);
						nameRecord.add(getMessages().credit(),
								currentValue.getCredit());
						list.add(nameRecord);
						super.createRecord(list);
					}

				});

				list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
						getMessages().memo()), getMessages().memo(), true, true));

			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().voucher());
			}

			@Override
			protected void getRequirementsValues(ClientEntry obj) {
				Account account = get(ACCOUNT).getValue();
				String memo = get(MEMO).getValue();
				// double debits = get(DEBITS).getValue();
				// double credits = get(CREDITS).getValue();
				obj.setAccount(account.getID());
				obj.setMemo(memo);
				// obj.setDebit(debits);
				// obj.setCredit(credits);
			}

			@Override
			protected void setRequirementsDefaultValues(ClientEntry obj) {
				get(ACCOUNT).setValue(
						CommandUtils.getServerObjectById(obj.getAccount(),
								AccounterCoreType.ACCOUNT));
				get(MEMO).setDefaultValue(obj.getMemo());
				get(DEBITS).setDefaultValue(obj.getDebit());
				get(CREDITS).setDefaultValue(obj.getCredit());
			}

			@Override
			protected ClientEntry getNewObject() {
				return new ClientEntry();
			}

			@Override
			protected Record createFullRecord(ClientEntry t) {
				Record record = new Record(t);
				ClientAccount account = ((ClientAccount) CommandUtils
						.getClientObjectById(t.getAccount(),
								AccounterCoreType.ACCOUNT, getCompanyId()));
				record.add(Global.get().messages().Account(),
						account == null ? "" : account.getDisplayName());
				record.add(getMessages().credit(), t.getCredit());
				record.add(getMessages().debit(), t.getDebit());
				record.add(getMessages().memo(), t.getMemo());
				return record;
			}

			@Override
			protected List<ClientEntry> getList() {
				return null;
			}

			@Override
			protected Record createRecord(ClientEntry t) {
				return createFullRecord(t);
			}

			@Override
			protected String getAddMoreString() {
				return getMessages().add();
			}

			@Override
			public boolean isDone() {
				List<ClientEntry> values = getValue();
				return values.size() >= 2;
			}

			@Override
			protected boolean contains(List<ClientEntry> oldValues,
					ClientEntry t) {
				for (ClientEntry entry : oldValues) {
					if (entry.getAccount() != 0
							&& entry.getAccount() == t.getAccount()) {
						return true;
					}
				}
				return false;
			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		double totalDebits = 0;
		double totalCredits = 0;
		List<ClientEntry> transItems = get(VOUCHER).getValue();
		for (ClientEntry item : transItems) {
			totalCredits += item.getCredit();
			totalDebits += item.getDebit();
		}

		makeResult.add(getMessages().totalDebits() + " : " + totalDebits);
		makeResult.add(getMessages().totalCredits() + " : " + totalCredits);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		double totalDebits = 0;
		double totalCredits = 0;
		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		List<ClientEntry> transItems = get(VOUCHER).getValue();
		for (ClientEntry item : transItems) {
			ClientTransactionItem transactionItem = new ClientTransactionItem();
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			transactionItem.setAccount(item.getAccount());
			if (item.getCredit() != 0.0) {
				transactionItem.setLineTotal(-item.getCredit());
			} else if (item.getDebit() != 0.0) {
				transactionItem.setLineTotal(item.getDebit());
			}
			transactionItem.setDescription(item.getMemo());
			transactionItems.add(transactionItem);
			totalCredits += item.getCredit();
			totalDebits += item.getDebit();
		}
		Result makeResult = new Result();

		if (totalCredits != totalDebits) {
			makeResult.add(getMessages().totalMustBeSame());
			return makeResult;
		}

		if (totalCredits == 0) {
			makeResult
					.add(getMessages().transactiontotalcannotbe0orlessthan0());
			return makeResult;
		}

		entry.setTransactionItems(transactionItems);
		entry.setType(ClientTransaction.TYPE_JOURNAL_ENTRY);
		ClientFinanceDate date = get(DATE).getValue();
		entry.setTransactionDate(date.getDate());

		String number = get(NUMBER).getValue();
		entry.setNumber(number);

		String memo = get(MEMO).getValue();
		entry.setMemo(memo);

		entry.setTotal(totalDebits);
		entry.setDebitTotal(totalDebits);
		entry.setCreditTotal(totalCredits);

		create(entry, context);

		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select an journalEntry to update.");
				return "Invoices List";
			}
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			ClientJournalEntry journalEntry = (ClientJournalEntry) CommandUtils
					.getClientTransactionByNumber(context.getCompany(), string,
							AccounterCoreType.JOURNALENTRY);
			if (journalEntry == null) {
				addFirstMessage(context, "Select an journalEntry to update.");
				return "journalEntries List " + string;
			}
			entry = journalEntry;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			entry = new ClientJournalEntry();
		}
		return null;
	}

	private void setValues() {
		get(DATE).setValue(entry.getDate());
		get(NUMBER).setValue(entry.getNumber());
		List<ClientTransactionItem> transactionItems = entry
				.getTransactionItems();
		List<ClientEntry> clientEntries = new ArrayList<ClientEntry>();
		for (ClientTransactionItem transactionItem : transactionItems) {
			ClientEntry entry = new ClientEntry();
			entry.setMemo(transactionItem.getDescription());
			entry.setAccount(transactionItem.getAccount());
			if (transactionItem.getLineTotal() > 0) {
				entry.setDebit(transactionItem.getLineTotal());
			} else {
				entry.setCredit(-transactionItem.getLineTotal());
			}
			clientEntries.add(entry);
		}
		get(VOUCHER).setValue(clientEntries);
		get(MEMO).setValue(entry.getMemo());

	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().journalEntry());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().journalEntry());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().journalEntry());
	}
}
