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

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, "Please Enter Journal Entry Date",
				"Date", false, true));

		list.add(new NumberRequirement(NUMBER,
				"Please Enter Journal Entry number", "Number", false, true));

		list.add(new AbstractTableRequirement<ClientEntry>(VOUCHER,
				"Please Add Another Vocher", "Vocher(s)", true, false, true) {

			@Override
			protected void addRequirement(List<Requirement> list) {
				list.add(new AccountRequirement("Account",
						"Please Enter Account name or number", "Account",
						false, true, null) {

					@Override
					protected String getSetMessage() {
						return "Account has been selected for Vocher";
					}

					@Override
					protected List<ClientAccount> getLists(Context context) {
						return getClientCompany().getActiveAccounts();
					}

					@Override
					protected String getEmptyString() {
						return "There are no accounts";
					}
				});

				list.add(new StringRequirement("Memo",
						"Please Enter Vocher Memo", "Memo", true, true));

				list.add(new AmountRequirement("Debits", "Please Enter Debits",
						"Debits", true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						get("Credits").setValue(0d);
					}
				});

				list.add(new AmountRequirement("Credits",
						"Please Enter Credits", "Credits", true, true) {
					@Override
					public void setValue(Object value) {
						super.setValue(value);
						get("Debits").setValue(0d);
					}
				});
			}

			@Override
			protected String getEmptyString() {
				return "There are no Vochers";
			}

			@Override
			protected void getRequirementsValues(ClientEntry obj) {
				ClientAccount account = get("Account").getValue();
				String memo = get("Memo").getValue();
				double debits = get("Debits").getValue();
				double credits = get("Credits").getValue();

				obj.setAccount(account.getID());
				obj.setMemo(memo);
				obj.setDebit(debits);
				obj.setCredit(credits);
			}

			@Override
			protected void setRequirementsDefaultValues(ClientEntry obj) {
				get("Account").setValue(
						getClientCompany().getAccount(obj.getAccount()));
				get("Memo").setDefaultValue(obj.getMemo());
				get("Debits").setDefaultValue(obj.getDebit());
				get("Credits").setDefaultValue(obj.getCredit());
			}

			@Override
			protected ClientEntry getNewObject() {
				return new ClientEntry();
			}

			@Override
			protected Record createFullRecord(ClientEntry t) {
				Record record = new Record(t);
				record.add("Account",
						getClientCompany().getAccount(t.getAccount())
								.getDisplayName());
				record.add("Memo", t.getMemo());
				record.add("Debits", t.getDebit());
				record.add("Credits", t.getCredit());
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
				return "Add";
			}

		});
		list.add(new StringRequirement(MEMO, "Please Journal Entry Memo",
				"Memo", true, true));

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
		return "Creating Journal Entry...";
	}

	@Override
	protected String getDetailsMessage() {
		return "Creating Journal Entry with following details.";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "New Journal Entry is successfully created.";
	}
}
