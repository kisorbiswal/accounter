package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AbstractTableRequirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewJournalEntryCommand extends NewAbstractCommand {

	private static final String VOUCHER = "Voucher";
	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String MEMO = "memo";
	protected static final String ACCOUNT = "Account";
	protected static final String DEBITS = "Debits";
	protected static final String CREDITS = "Credits";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().journalEntryDate()), getConstants()
				.journalEntryDate(), false, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().journalEntryNo()), getConstants()
				.journalEntryNo(), false, true));

		list.add(new AbstractTableRequirement<ClientEntry>(VOUCHER,
				getMessages().pleaseSelect(getConstants().voucherNo()),
				getConstants().voucher(), true, false, true) {

			@Override
			protected void addRequirement(List<Requirement> list) {
				list.add(new AccountRequirement(ACCOUNT, getMessages()
						.pleaseEnterNameOrNumber(Global.get().Account()),
						Global.get().Account(), false, true, null) {

					@Override
					protected String getSetMessage() {
						return getMessages()
								.hasSelected(Global.get().Account());
					}

					@Override
					protected List<ClientAccount> getLists(Context context) {
						return getClientCompany().getActiveAccounts();
					}

					@Override
					protected String getEmptyString() {
						return getMessages().youDontHaveAny(
								Global.get().Accounts());
					}
				});

				list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
						getConstants().memo()), getConstants().memo(), true,
						true));

				list.add(new AmountRequirement(DEBITS, getMessages()
						.pleaseEnter(getConstants().debitAmount()),
						getConstants().debit(), true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						if(currentValue!=null){
						currentValue.setCredit(0.0d);
						}
					}
				});

				list.add(new AmountRequirement(CREDITS, getMessages()
						.pleaseEnter(getConstants().creditAmount()),
						getConstants().credit(), true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						if(currentValue!=null){
						currentValue.setDebit(0.0d);
						}
					}
				});
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().voucher());
			}

			@Override
			protected void getRequirementsValues(ClientEntry obj) {
				ClientAccount account = get(ACCOUNT).getValue();
				String memo = get(MEMO).getValue();
				double debits = get(DEBITS).getValue();
				double credits = get(CREDITS).getValue();

				obj.setAccount(account.getID());
				obj.setMemo(memo);
				obj.setDebit(debits);
				obj.setCredit(credits);
			}

			@Override
			protected void setRequirementsDefaultValues(ClientEntry obj) {
				get(ACCOUNT).setValue(
						getClientCompany().getAccount(obj.getAccount()));
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
				record.add(Global.get().Account(), getClientCompany()
						.getAccount(t.getAccount()).getDisplayName());
				record.add(getConstants().memo(), t.getMemo());
				record.add(getConstants().debit(), t.getDebit());
				record.add(getConstants().credit(), t.getCredit());
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
				return getConstants().add();
			}

		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

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

		makeResult.add(getConstants().totalDebits() + " : " + totalDebits);
		makeResult.add(getConstants().totalCredits() + " : " + totalCredits);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		double totalDebits = 0;
		double totalCredits = 0;
		List<ClientEntry> transItems = get(VOUCHER).getValue();
		for (ClientEntry item : transItems) {
			totalCredits += item.getCredit();
			totalDebits += item.getDebit();
		}
		Result makeResult = new Result();

		if (totalCredits != totalDebits) {
			makeResult.add(getConstants().totalMustBeSame());
			return makeResult;
		}

		if (totalCredits == 0) {
			makeResult.add(getConstants()
					.transactiontotalcannotbe0orlessthan0());
			return makeResult;
		}

		ClientJournalEntry entry = new ClientJournalEntry();
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
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().journalEntry());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().journalEntry());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().journalEntry());
	}
}
