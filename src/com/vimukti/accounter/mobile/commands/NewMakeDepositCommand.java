package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewMakeDepositCommand extends NewAbstractTransactionCommand {
	private static final String DEPOSIT_OR_TRANSFER_FROM = "depositOrTransferFrom";
	private static final String DEPOSIT_OR_TRANSFER_TO = "DepositOrTransferTo";
	ClientMakeDeposit makeDeposit;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_FROM, getMessages()
				.pleaseEnterName(getMessages().fromAccount()), getMessages()
				.fromAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().fromAccount());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				NewMakeDepositCommand.this.addCreateAccountCommands(list);
			}

			@Override
			protected List<Account> getLists(Context context) {
				return NewMakeDepositCommand.this.getAccounts(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						Global.get().messages().Accounts());
			}
		});

		list.add(new AccountRequirement(DEPOSIT_OR_TRANSFER_TO, getMessages()
				.pleaseEnterName(getMessages().depositAccount()), getMessages()
				.depositAccount(), false, true, null) {

			@Override
			protected void setCreateCommand(CommandList list) {
				NewMakeDepositCommand.this.addCreateAccountCommands(list);
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().depositAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return NewMakeDepositCommand.this.getAccounts(context);
			}

			@Override
			public void setValue(Object value) {
				Account depositTo = (Account) value;
				if (depositTo == null) {
					return;
				} else if (!NewMakeDepositCommand.this
						.isDifferentAccounts(depositTo)) {
					addFirstMessage(getMessages().pleaseEnterName(
							getMessages().depositAccount()));
					super.setValue(value);
				} else {
					addFirstMessage(getMessages()
							.dipositAccountAndTransferAccountShouldBeDiff());
				}
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		});

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getMessages().amount()), getMessages().amount(), false, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	protected void addCreateAccountCommands(CommandList list) {
		list.add(new UserCommand("Create BankAccount", "Bank"));
		list.add(new UserCommand("Create BankAccount",
				"Create Other CurrentAsset Account", "Other Current Asset"));
		list.add(new UserCommand("Create BankAccount",
				"Create Current Liability Account", "Current Liability"));
		list.add(new UserCommand("Create BankAccount", "Create Equity Account",
				"Equity"));
	}

	protected List<Account> getAccounts(Context context) {
		List<Account> filteredList = new ArrayList<Account>();
		for (Account obj : context.getCompany().getAccounts()) {
			if (new ListFilter<Account>() {

				@Override
				public boolean filter(Account e) {
					return e.getIsActive()
							&& (Arrays.asList(Account.TYPE_OTHER_CURRENT_ASSET,
									Account.TYPE_OTHER_CURRENT_LIABILITY,
									Account.TYPE_BANK, Account.TYPE_EQUITY)
									.contains(e.getType()));
				}
			}.filter(obj)) {
				filteredList.add(obj);
			}
		}
		return filteredList;
	}

	protected boolean isDifferentAccounts(Account depositTo) {
		Account depositFrom = get(DEPOSIT_OR_TRANSFER_FROM).getValue();
		if (depositFrom.getID() == depositTo.getID()) {
			return true;
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientFinanceDate date = get(DATE).getValue();
		makeDeposit.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		makeDeposit.setNumber(number);

		makeDeposit.setType(ClientTransaction.TYPE_MAKE_DEPOSIT);

		Account depositOrTransferToAccount = get(DEPOSIT_OR_TRANSFER_TO)
				.getValue();
		makeDeposit.setDepositIn(depositOrTransferToAccount.getID());

		Account depositOrTransferFromAccount = get(DEPOSIT_OR_TRANSFER_FROM)
				.getValue();
		makeDeposit.setDepositFrom(depositOrTransferFromAccount.getID());

		double amount = get(AMOUNT).getValue();
		makeDeposit.setCashBackAmount(amount);

		makeDeposit.setTotal(amount);

		String memo = get(MEMO).getValue();
		makeDeposit.setMemo(memo);

		// caluclateTotals(makeDeposit);

		// if (context.getPreferences().isEnableMultiCurrency()) {
		// Currency currency = get(CURRENCY).getValue();
		// if (currency != null) {
		// makeDeposit.setCurrency(currency.getID());
		// }
		//
		// double factor = get(CURRENCY_FACTOR).getValue();
		// makeDeposit.setCurrencyFactor(factor);
		// }
		create(makeDeposit, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a transaction to update.");
				return "Transaction Detail By Account";
			}
			makeDeposit = getTransaction(string, AccounterCoreType.MAKEDEPOSIT,
					context);
			if (makeDeposit == null) {
				addFirstMessage(context, "Select a transction to update.");
				return "Transaction Detail By Account ," + string;
			}
			setValues(context);
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			makeDeposit = new ClientMakeDeposit();
		}
		setTransaction(makeDeposit);
		return null;
	}

	private void setValues(Context context) {
		get(DATE).setValue(makeDeposit.getDate());
		get(NUMBER).setValue(makeDeposit.getNumber());
		get(DEPOSIT_OR_TRANSFER_FROM).setValue(
				CommandUtils.getServerObjectById(makeDeposit.getDepositFrom(),
						AccounterCoreType.ACCOUNT));
		get(DEPOSIT_OR_TRANSFER_TO).setValue(
				CommandUtils.getServerObjectById(makeDeposit.getDepositIn(),
						AccounterCoreType.ACCOUNT));
		get(AMOUNT).setValue(makeDeposit.getTotal());
		get(MEMO).setValue(makeDeposit.getMemo());
	}

	@Override
	protected String getWelcomeMessage() {
		return makeDeposit.getID() == 0 ? getMessages().creating(
				getMessages().makeDeposit())
				: "Maker deposit is ready to updating ";
	}

	@Override
	protected String getDetailsMessage() {
		return makeDeposit.getID() == 0 ? getMessages().readyToCreate(
				getMessages().makeDeposit())
				: "Make deposit is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_MAKE_DEPOSIT, getCompany()));
		get(MEMO).setDefaultValue("");
		// get(CURRENCY).setDefaultValue(null);
		// get(CURRENCY_FACTOR).setDefaultValue(1.0);
	}

	@Override
	public String getSuccessMessage() {
		return makeDeposit.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().makeDeposit()) : getMessages()
				.updateSuccessfully(getMessages().makeDeposit());
	}

	@Override
	protected Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

}
