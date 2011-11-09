package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.MakeDepositTableRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewMakeDepositCommond extends NewAbstractTransactionCommand {
	private static final String TRANSFERED_ACCOUNT = "transferedAccount";
	private static final String DEPOSIT_OR_TRANSFER_TO = "depositOrTransferTo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

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
				.currency(), true, true) {
			@Override
			protected String getDisplayValue(Double value) {
				String primaryCurrency = getClientCompany().getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (context.getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(context.getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterName(
						getMessages().depositAccount(Global.get().Account())),
				getMessages().depositAccount(Global.get().Account()), false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depositAccount(Global.get().Account()));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getIsActive()
									&& (Arrays
											.asList(Account.TYPE_OTHER_CURRENT_ASSET,
													Account.TYPE_OTHER_CURRENT_LIABILITY,
													Account.TYPE_BANK,
													Account.TYPE_EQUITY)
											.contains(e.getType()));
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}
		});

		list.add(new MakeDepositTableRequirement(TRANSFERED_ACCOUNT, "", Global
				.get().Accounts()));

		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
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

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientMakeDeposit makeDeposit = new ClientMakeDeposit();

		ClientFinanceDate date = get(DATE).getValue();
		makeDeposit.setDate(date.getDate());

		makeDeposit.setType(ClientTransaction.TYPE_MAKE_DEPOSIT);

		String number = get(NUMBER).getValue();
		makeDeposit.setNumber(number);

		Account account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		makeDeposit.setDepositIn(account.getID());

		List<ClientTransactionMakeDeposit> list = get(TRANSFERED_ACCOUNT)
				.getValue();

		makeDeposit.setTransactionMakeDeposit(list);

		makeDeposit.setCashBackAccount(0);
		makeDeposit.setCashBackMemo("");
		makeDeposit.setCashBackAmount(0);

		String memo = get(MEMO).getValue();
		makeDeposit.setMemo(memo);
		caluclateTotals(makeDeposit);
		for (ClientTransactionMakeDeposit clientTransactionMakeDeposit : list) {
			clientTransactionMakeDeposit.setID(0);
			clientTransactionMakeDeposit.setMakeDeposit(makeDeposit);
		}

		if (context.getPreferences().isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				makeDeposit.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			makeDeposit.setCurrencyFactor(factor);
		}
		create(makeDeposit, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().makeDeposit());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().makeDeposit());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(MEMO).setDefaultValue("");
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().makeDeposit());
	}

}
