package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AbstractTableRequirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
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
						if (currentValue != null) {
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
						if (currentValue != null) {
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
				Account account = get(ACCOUNT).getValue();
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
				record.add(Global.get().Account(),
						((ClientAccount) CommandUtils.getClientObjectById(
								t.getAccount(), AccounterCoreType.ACCOUNT,
								getCompanyId())).getDisplayName());

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
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (context.getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(context.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

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

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				entry.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			entry.setCurrencyFactor(factor);
		}

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
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().journalEntry());
	}
}
