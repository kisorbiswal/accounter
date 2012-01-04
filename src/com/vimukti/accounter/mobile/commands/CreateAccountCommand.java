package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Bank;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyListRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CreateAccountCommand extends AbstractCommand {

	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACTIVE = "Active";

	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";
	private static final String CREDIT_LIMIT = "creditLimit";
	private static final String CARD_OR_LOAD_NUMBER = "cardOrLoadNumber";
	private static final String ACCOUNT_REGISTER = "AccountRegister";
	private static final String BANK_ACCOUNT_NUMBER = "Bank Account Number";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_ACCOUNT_TYPE = "Bank Account Type";
	private static final String IS_SUB_ACCOUNT = "isSubAccount";
	private static final String PARENT_ACCOUNT = "parentAccount";
	private ClientAccount account;

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getWelcomeMessage() {
		if (account.getID() == 0) {
			if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
				return "Create Bank Account Command is activated.";
			}
			return "Create Account Command is activated.";
		}
		if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
			return "Update Bank Account(" + account.getName()
					+ ") Command is activated.";
		}
		return "Update Account(" + account.getName()
				+ ") Command is activated.";
	}

	@Override
	protected String getDeleteCommand(Context context) {
		return account.getID() == 0 ? null : "deleteAccount " + account.getID();
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(ACCOUNT_TYPE, getMessages()
				.pleaseEnter(getMessages().accountType()), getMessages()
				.accountType(), true, true, null) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				accountTypeChanged((String) value);
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().accountType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new NumberRequirement(ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getMessages().accountNumber()), getMessages()
				.accountNumber(), false, true) {
			@Override
			public void setValue(Object value) {
				if (value != null) {
					String validateAccountNumber = validateAccountNumber(Long
							.valueOf((String) value));
					if (validateAccountNumber != null) {
						addFirstMessage(validateAccountNumber);
						return;
					}
				}
				super.setValue(value);
			}
		});

		list.add(new NameRequirement(ACCOUNT_NAME, getMessages()
				.pleaseEnterName(getMessages().account()),
				getMessages().name(), false, true));

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().accountisActive();
			}

			@Override
			protected String getFalseString() {
				return getMessages().accountisInActive();
			}
		});

		list.add(new CurrencyListRequirement(CURRENCY, getMessages()
				.pleaseSelect(getMessages().currency()), getMessages()
				.currency(), false, true, null) {

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(getMessages().currency());
			}

			@Override
			protected boolean filter(Currency e, String name) {
				return e.getFormalName().startsWith(name);
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String accountType = get(ACCOUNT_TYPE).getValue();
				if (ClientAccount
						.isAllowCurrencyChange(CreateAccountCommand.this
								.getAccountType(accountType))) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				return get(CURRENCY).getValue();
			}
		});

		list.add(new AmountRequirement(OPENINGBALANCE, getMessages()
				.pleaseEnter(getMessages().openBalance()), getMessages()
				.openBalance(), true, true));

		list.add(new DateRequirement(ASOF, getMessages().pleaseEnter(
				getMessages().asOf()), getMessages().asOf(), true, true));

		list.add(new StringRequirement(COMMENTS, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));

		list.add(new BooleanRequirement(CONSIDER_AS_CASH_ACCOUNT, true) {

			@Override
			protected String getTrueString() {
				return getMessages().accountCashAccount();
			}

			@Override
			protected String getFalseString() {
				return getMessages().accountisNotCashAccount();
			}
		});

		list.add(new AmountRequirement(CREDIT_LIMIT, getMessages().pleaseEnter(
				getMessages().creditLimit()), getMessages().creditLimit(),
				true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (CreateAccountCommand.this.get(ACCOUNT_TYPE).getValue()
						.equals(getMessages().creditCard())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});
		list.add(new NumberRequirement(CARD_OR_LOAD_NUMBER, getMessages()
				.pleaseEnter(getMessages().cardOrLoanNumber()), getMessages()
				.cardOrLoanNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (CreateAccountCommand.this.get(ACCOUNT_TYPE).getValue()
						.equals(getMessages().creditCard())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});

		list.add(new ListRequirement<Bank>(BANK_NAME, getMessages()
				.pleaseSelect(getMessages().bank()), getMessages().bank(),
				true, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				int accountType = CreateAccountCommand.this
						.getAccountType((String) get(ACCOUNT_TYPE).getValue());
				if (accountType == ClientAccount.TYPE_BANK) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny("Banks");
			}

			@Override
			protected String getSetMessage() {
				return getMessages().pleaseSelect(getMessages().bank());
			}

			@Override
			protected Record createRecord(Bank value) {
				Record bankRecord = new Record(value);
				bankRecord.add(getMessages().bankName(), value.getName());
				return bankRecord;
			}

			@Override
			protected String getDisplayValue(Bank value) {
				return value == null ? " " : value.getName();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newBank");
			}

			@Override
			protected String getSelectString() {
				return getMessages().hasSelected(getMessages().bank());
			}

			@Override
			protected boolean filter(Bank e, String name) {
				return e.getName().equals(name);
			}

			@Override
			protected List<Bank> getLists(Context context) {
				return new ArrayList<Bank>(context.getCompany().getBanks());
			}
		});

		list.add(new StringListRequirement(BANK_ACCOUNT_TYPE, getMessages()
				.pleaseEnter(getMessages().bankAccountType()), getMessages()
				.bankAccountType(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().accountType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().accountType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getBankAccountTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				int accountType = CreateAccountCommand.this
						.getAccountType((String) get(ACCOUNT_TYPE).getValue());
				if (accountType == ClientAccount.TYPE_BANK) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(BANK_ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getMessages().bankAccountNumber()), getMessages()
				.bankAccountNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				int accountType = CreateAccountCommand.this
						.getAccountType((String) get(ACCOUNT_TYPE).getValue());
				if (accountType == ClientAccount.TYPE_BANK) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new BooleanRequirement(IS_SUB_ACCOUNT, true) {

			@Override
			protected String getTrueString() {
				return "This account considered as a sub account";
			}

			@Override
			protected String getFalseString() {
				return "This account can not be considered as a sub account";
			}
		});

		list.add(new AccountRequirement(PARENT_ACCOUNT, getMessages()
				.pleaseSelect(getMessages().parentAccount()), getMessages()
				.parentAccount(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().parentAccount());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().parentAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean isSubAccount = CreateAccountCommand.this.get(
						IS_SUB_ACCOUNT).getValue();
				if (isSubAccount) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new CommandsRequirement(ACCOUNT_REGISTER) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add("accountRegister");
				return list;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (account.getID() != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public String onSelection(String value) {
				return "bankRegisters " + account.getName();
			}
		});

	}

	protected List<String> getBankAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().cuurentAccount());
		list.add(getMessages().checking());
		list.add(getMessages().moneyMarket());
		list.add(getMessages().saving());
		return list;
	}

	protected void accountTypeChanged(String value) {
		int accountType = getAccountType(value);
		long nextAccountNumber = CommandUtils.getNextAccountNumber(
				getCompany(), UIUtils.getAccountSubBaseType(accountType));
		if (account.getID() == 0) {
			if (accountType == ClientAccount.TYPE_BANK) {
				account = new ClientBankAccount();
			} else {
				account = new ClientAccount();
			}
		} else {
			if (accountType == ClientAccount.TYPE_BANK) {
				account = CommandUtils.getBankAccountByName(getCompany(),
						account.getName());
			} else {
				account = CommandUtils.getAccountByName(getCompany(),
						account.getName());
			}
		}
		get(ACCOUNT_NUMBER).setValue(String.valueOf(nextAccountNumber));
		resetRequirementValues();
	}

	protected List<String> getAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().income());
		list.add(getMessages().otherIncome());
		list.add(getMessages().expense());
		list.add(getMessages().accountPayble());
		list.add(getMessages().otherExpense());
		list.add(getMessages().costOfGoodSold());
		list.add(getMessages().cash());
		list.add(getMessages().bank());
		list.add(getMessages().otherCurrentAsset());
		list.add(getMessages().accountReceivable());
		list.add(getMessages().inventoryAsset());
		list.add(getMessages().otherAssets());
		list.add(getMessages().fixedAssest());
		list.add(getMessages().creditCard());
		list.add(getMessages().payrollLiability());
		list.add(getMessages().otherCurrentLiability());
		list.add(getMessages().longTermLiabilities());
		list.add(getMessages().equity());
		list.add(getMessages().paypal());
		return list;
	}

	@Override
	protected String getDetailsMessage() {
		if (account.getID() == 0) {
			if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
				return getMessages().readyToCreate(getMessages().bankAccount());
			}
			return getMessages().readyToCreate(getMessages().account());
		} else {
			if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
				return getMessages().readyToUpdate(getMessages().bankAccount());
			}
			return getMessages().readyToUpdate(getMessages().account());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		String accountNumber = String.valueOf(autoGenerateAccountnumber(1100,
				1999, context.getCompany()));
		if (!context.getPreferences().getUseAccountNumbers()) {
			get(ACCOUNT_NUMBER).setValue(accountNumber);
		}
		get(ACCOUNT_NUMBER).setDefaultValue(accountNumber);
		get(ACCOUNT_TYPE).setDefaultValue(getMessages().income());
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
		get(CURRENCY).setValue(
				getServerObject(Currency.class, getPreferences()
						.getPrimaryCurrency().getID()));
	}

	@Override
	public String getSuccessMessage() {
		if (account.getID() == 0) {
			if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
				return getMessages().createSuccessfully(
						getMessages().bankAccount());
			}
			return getMessages().createSuccessfully(getMessages().account());
		} else {
			if (get(ACCOUNT_TYPE).getValue().equals(getMessages().bank())) {
				return getMessages().updateSuccessfully(
						getMessages().bankAccount());
			}
			return getMessages().updateSuccessfully(getMessages().account());
		}
	}

	@Override
	public Result onCompleteProcess(Context context) {
		String accType = get(ACCOUNT_TYPE).getValue();
		String accname = get(ACCOUNT_NAME).getValue();
		String accountNum = get(ACCOUNT_NUMBER).getValue();
		double openingBal = get(OPENINGBALANCE).getValue();
		boolean isActive = get(ACTIVE).getValue();
		boolean isCashAcount = get(CONSIDER_AS_CASH_ACCOUNT).getValue();
		ClientFinanceDate asOf = get(ASOF).getValue();
		String comment = get(COMMENTS).getValue();
		account.setOpeningBalanceEditable(true);
		account.setDefault(true);
		account.setType(getAccountType(accType));
		account.setName(accname);
		account.setNumber(accountNum);
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(asOf.getDate());
		account.setConsiderAsCashAccount(isCashAcount);
		switch (account.getType()) {
		case ClientAccount.TYPE_BANK:
			String bankAccType = get(BANK_ACCOUNT_TYPE).getValue();
			String number = get(BANK_ACCOUNT_NUMBER).getValue();
			Bank bank = get(BANK_NAME).getValue();
			ClientBankAccount bankAccount = (ClientBankAccount) account;
			bankAccount.setBankAccountNumber(number);
			bankAccount.setBank(bank == null ? 0 : bank.getID());
			if (bankAccType != null) {
				((ClientBankAccount) account)
						.setBankAccountType(getBankAccountType(bankAccType));
			}
			account.setIncrease(Boolean.FALSE);
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			if (get(CREDIT_LIMIT).getValue() != null) {
				account.setCreditLimit((Double) get(CREDIT_LIMIT).getValue());
				account.setCardOrLoanNumber((String) get(CARD_OR_LOAD_NUMBER)
						.getValue());
			}
			break;
		default:
			// if (selectedSubAccount != null)
			// data.setParent(selectedSubAccount.getID());
			// if (hierText != null)
			// data.setHierarchy(UIUtils.toStr(hierText.getValue()));
			break;
		}
		account.setComment(comment);
		account.setCurrencyFactor(1.0);
		if (account.isAllowCurrencyChange()) {
			Currency currency = get(CURRENCY).getValue();
			account.setCurrency(currency.getID());
			Double currencyFactor = get(CURRENCY_FACTOR).getValue();
			account.setCurrencyFactor(currencyFactor);
		}
		account.updateBaseTypes();
		if (account.getType() == ClientAccount.TYPE_INCOME
				|| account.getType() == ClientAccount.TYPE_OTHER_INCOME
				|| account.getType() == ClientAccount.TYPE_CREDIT_CARD
				|| account.getType() == ClientAccount.TYPE_PAYROLL_LIABILITY
				|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY
				|| account.getType() == ClientAccount.TYPE_EQUITY
				|| account.getType() == ClientAccount.TYPE_ACCOUNT_PAYABLE) {
			account.setIncrease(Boolean.TRUE);
		} else {
			account.setIncrease(Boolean.FALSE);
		}

		Boolean isSubAccount = get(IS_SUB_ACCOUNT).getValue();
		if (isSubAccount) {
			Account parentAcc = get(PARENT_ACCOUNT).getValue();
			if (parentAcc != null) {
				account.setParent(parentAcc.getID());
			}
		}
		if (account.getType() == ClientAccount.TYPE_BANK) {
			ClientBankAccount bankAccount = (ClientBankAccount) account;
			create(bankAccount, context);
		} else {
			create(account, context);
		}
		return null;
	}

	private int getBankAccountType(String type) {
		AccounterMessages messages = getMessages();
		if (type.equals(messages.saving()))
			return ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
		else if (type.equals(messages.checking()))
			return ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
		else if (type.equals(messages.moneyMarket()))
			return ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
		else
			return ClientAccount.BANK_ACCCOUNT_TYPE_NONE;
	}

	private int getAccountType(String accType) {
		if (accType.equalsIgnoreCase("cash")) {
			return 1;
		} else if (accType.equalsIgnoreCase("bank")) {
			return 2;
		} else if (accType.equalsIgnoreCase("Account Payble")) {
			return 3;
		} else if (accType.equalsIgnoreCase("Other current Asset")) {
			return 4;
		} else if (accType.equalsIgnoreCase("Inventory Asset")) {
			return 5;
		} else if (accType.equalsIgnoreCase("Fixed Asset")) {
			return 6;
		} else if (accType.equalsIgnoreCase("Other Asset")) {
			return 7;
		} else if (accType.equalsIgnoreCase("Account Receivable")) {
			return 8;
		} else if (accType.equalsIgnoreCase("Other Current Liability")) {
			return 9;
		} else if (accType.equalsIgnoreCase("Credit card")) {
			return 10;
		} else if (accType.equalsIgnoreCase("Payroll Liability")) {
			return 11;
		} else if (accType.equalsIgnoreCase("Long Term Liability")) {
			return 12;
		} else if (accType.equalsIgnoreCase("Equity")) {
			return 13;
		} else if (accType.equalsIgnoreCase("Income")) {
			return 14;
		} else if (accType.equalsIgnoreCase("Cost of Goods Sold")) {
			return 15;
		} else if (accType.equalsIgnoreCase("Expense")) {
			return 16;
		} else if (accType.equalsIgnoreCase("Other Income")) {
			return 17;
		} else if (accType.equalsIgnoreCase("Other Expense")) {
			return 18;
		} else if (accType.equalsIgnoreCase("Paypal")) {
			return 19;
		}
		return 0;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!isUpdate) {
			if (context.getCommandString().contains("bank")) {
				account = new ClientBankAccount();
			} else {
				account = new ClientAccount();
			}
			if (!string.isEmpty()) {
				get(ACCOUNT_TYPE).setValue(string);
			}
			return null;
		}
		if (context.getCommandString().contains("bank")
				|| context.getCommandString().contains("Bank")) {
			ClientBankAccount bankAccount = CommandUtils.getBankAccountByName(
					context.getCompany(), string);
			if (bankAccount == null) {
				long numberFromString = getNumberFromString(string);
				if (numberFromString != 0) {
					string = String.valueOf(numberFromString);
				}
				bankAccount = (ClientBankAccount) CommandUtils
						.getAccountByNumber(context.getCompany(), string);
			}
			account = bankAccount;
		} else {
			account = CommandUtils.getAccountByName(context.getCompany(),
					string);
			if (account == null) {
				long numberFromString = getNumberFromString(string);
				if (numberFromString != 0) {
					string = String.valueOf(numberFromString);
				}
				account = CommandUtils.getAccountByNumber(context.getCompany(),
						string);
			}
		}
		if (account == null) {
			addFirstMessage(context, "Select an account to update.");
			return "Accounts " + string.trim();
		}

		get(ACCOUNT_TYPE).setValue(getAccountType(account.getType()));
		get(ACCOUNT_NAME).setValue(account.getName());

		get(ACCOUNT_NUMBER).setValue(account.getNumber());
		get(CREDIT_LIMIT).setValue(account.getCreditLimit());
		get(CARD_OR_LOAD_NUMBER).setValue(account.getCardOrLoanNumber());
		get(OPENINGBALANCE).setValue(account.getOpeningBalance());
		get(ACTIVE).setValue(account.getIsActive());
		get(CONSIDER_AS_CASH_ACCOUNT).setValue(
				account.isConsiderAsCashAccount());
		get(CONSIDER_AS_CASH_ACCOUNT).setEditable(false);
		get(ASOF).setValue(new ClientFinanceDate(account.getAsOf()));
		get(COMMENTS).setValue(account.getComment());
		get(CURRENCY).setValue(
				getServerObject(Currency.class, account.getCurrency()));
		get(CURRENCY).setEditable(false);
		get(CURRENCY_FACTOR).setValue(account.getCurrencyFactor());
		if (account.getType() == ClientAccount.TYPE_BANK) {
			get(BANK_NAME).setValue(
					CommandUtils.getServerObjectById(
							((ClientBankAccount) account).getBank(),
							AccounterCoreType.BANK));
			get(BANK_NAME).setEditable(false);
			get(BANK_ACCOUNT_TYPE).setValue(
					getBankAccountType(((ClientBankAccount) account)
							.getBankAccountType() - 1));
			get(BANK_ACCOUNT_TYPE).setEditable(false);
			get(BANK_ACCOUNT_NUMBER).setValue(
					((ClientBankAccount) account).getBankAccountNumber());
		}
		get(IS_SUB_ACCOUNT).setValue(account.getParent() != 0);
		get(PARENT_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(account.getParent(),
						AccounterCoreType.ACCOUNT));
		return null;
	}

	private String getBankAccountType(int type) {
		AccounterMessages messages = getMessages();
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			return messages.checking();
		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			return messages.moneyMarket();
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			return messages.saving();
		case ClientAccount.BANK_ACCCOUNT_TYPE_CURRENT_ACCOUNT:
			return messages.cuurentAccount();
		default:
			break;
		}

		return "";

	}

	private String getAccountType(int i) {
		i--;// Starting from 0
		switch (i) {
		case 0:
			return "cash";
		case 1:
			return "bank";
		case 2:
			return "Account Payble";
		case 3:
			return "Other current Asset";
		case 4:
			return "Inventory Asset";
		case 5:
			return "Fixed Asset";
		case 6:
			return "Other Asset";
		case 7:
			return "Account Receivable";
		case 8:
			return "Other Current Liability";
		case 9:
			return "Credit card";
		case 10:
			return "Payroll Liability";
		case 11:
			return "Long Term Liability";
		case 12:
			return "Equity";
		case 13:
			return "Income";
		case 14:
			return "Cost of Goods Sold";
		case 15:
			return "Expense";
		case 16:
			return "Other Income";
		case 17:
			return "Other Expense";
		case 18:
			return "Paypal";
		default:
			break;
		}
		return "";
	}

	private void resetRequirementValues() {
		if (account.getID() != 0) {
			return;
		}
		get(OPENINGBALANCE).setValue(0.0);
		get(ASOF).setValue(new ClientFinanceDate());
		get(COMMENTS).setValue("");
		get(CREDIT_LIMIT).setValue(0.0);
		get(CARD_OR_LOAD_NUMBER).setValue("");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
		get(CURRENCY).setValue(
				getServerObject(Currency.class, getPreferences()
						.getPrimaryCurrency().getID()));
		get(BANK_NAME).setValue(null);
		get(BANK_ACCOUNT_TYPE).setValue(null);
		get(BANK_ACCOUNT_NUMBER).setValue(null);
	}

	private String validateAccountNumber(Long number) {
		Set<Account> accounts = getCompany().getAccounts();
		for (Account account : accounts) {
			if (number.toString().equals(account.getNumber())
					&& account.getID() != this.account.getID()) {
				return getMessages().alreadyAccountExist();
			}
		}
		int accountType = getAccountType((String) get(ACCOUNT_TYPE).getValue());
		int accountSubBaseType = UIUtils.getAccountSubBaseType(accountType);

		Integer[] nominalCodeRange = getCompany().getNominalCodeRange(
				accountSubBaseType);

		if (nominalCodeRange == null
				&& accountSubBaseType == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
			return null;
		}

		if (number < nominalCodeRange[0] || number > nominalCodeRange[1]) {
			return getMessages()
					.theAccountNumberchosenisincorrectPleaschooseaNumberbetween()
					+ "  "
					+ nominalCodeRange[0]
					+ getMessages().to()
					+ nominalCodeRange[1];
		}
		return null;

	}
}