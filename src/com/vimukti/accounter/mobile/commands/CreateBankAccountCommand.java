package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Bank;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Sai Parasad N
 * 
 */
public class CreateBankAccountCommand extends AbstractCommand {
	private static final int BANK_CAT_BEGIN_NO = 1100;
	private static final int BANK_CAT_END_NO = 1179;
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String ACCOUNT_NAME = "Account Name";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String BANK_ACCOUNT_TYPE = "Bank Account Type";
	private static final String ACTIVE = "Active";
	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_ACCOUNT_NUMBER = "Bank Account Number";
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String BANKACCOUNT_REGISTER = "BankAccountRegister";
	private ClientBankAccount bankAccount;

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(ACCOUNT_TYPE, getMessages().pleaseEnter(
				getMessages().accountType()), getMessages().accountType(),
				false, true));

		list.add(new NumberRequirement(ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getMessages().accountNumber()), getMessages()
				.accountNumber(), true, true) {

			@Override
			public void setValue(Object value) {
				try {
					if (bankAccount.getID() == 0) {
						int parseInt = Integer.parseInt((String) value);
						if (parseInt > 1179 || parseInt < 1100) {
							addFirstMessage(getMessages()
									.nameShouldbewithinThe1100to1179Ranage());
							return;
						}
					}
					super.setValue(value);
				} catch (Exception e) {
				}
			}
		});

		list.add(new NameRequirement(ACCOUNT_NAME, getMessages().pleaseEnter(
				getMessages().accountName()), getMessages().accountName(),
				false, true));

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

		list.add(new AmountRequirement(OPENINGBALANCE, getMessages()
				.pleaseEnter(getMessages().openBalance()), getMessages()
				.openBalance(), true, true));

		list.add(new DateRequirement(ASOF, getMessages().pleaseEnter(
				getMessages().asOf()), getMessages().asOf(), true, true));

		list.add(new StringRequirement(COMMENTS, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));

		list.add(new NameRequirement(BANK_NAME, getMessages().pleaseEnter(
				getMessages().bankName()), getMessages().bankName(), true, true));

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
				return getAccountTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new NumberRequirement(BANK_ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getMessages().bankAccountNumber()), getMessages()
				.bankAccountNumber(), false, true));
		list.add(new CommandsRequirement(BANKACCOUNT_REGISTER) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add("accountRegister");
				return list;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (bankAccount.getID() != 0) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			public String onSelection(String value) {
				return "bankRegisters " + bankAccount.getName();
			}
		});

	}

	protected List<String> getBankNameList(Context context) {
		List<String> bankNameList = new ArrayList<String>();
		Set<Bank> banksList = context.getCompany().getBanks();
		for (Bank clientBank : banksList) {
			bankNameList.add(clientBank.getName());
		}
		return bankNameList;
	}

	protected List<String> getAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().cuurentAccount());
		list.add(getMessages().checking());
		list.add(getMessages().moneyMarket());
		list.add(getMessages().saving());
		return list;
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = bankAccount.getID();
		return id != 0 ? "Delete BankAccout " + bankAccount.getID() : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!isUpdate) {
			bankAccount = new ClientBankAccount();
			if (!string.isEmpty()) {
				get(ACCOUNT_TYPE).setValue(string);
			}
			return null;
		}

		bankAccount = CommandUtils.getBankAccountByName(context.getCompany(),
				string);
		if (bankAccount == null) {
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			bankAccount = (ClientBankAccount) CommandUtils.getAccountByNumber(
					context.getCompany(), string);
		}
		if (bankAccount == null) {
			addFirstMessage(
					context,
					getMessages().selectATransactionToUpdate(
							getMessages().account()));
			return "listOfAccounts " + string.trim();
		}

		get(BANK_ACCOUNT_TYPE).setValue(
				getBankAccountType(bankAccount.getType() - 1));
		get(BANK_ACCOUNT_TYPE).setEditable(false);

		get(BANK_ACCOUNT_NUMBER).setValue(bankAccount.getBankAccountNumber());
		get(BANK_ACCOUNT_NUMBER).setEditable(false);
		get(ACCOUNT_NAME).setValue(bankAccount.getName());
		get(ACCOUNT_NAME).setEditable(false);

		get(ACCOUNT_NUMBER).setValue(bankAccount.getNumber());
		get(ACCOUNT_NUMBER).setEditable(false);
		get(BANK_ACCOUNT_NUMBER).setValue(bankAccount.getBankAccountNumber());
		get(OPENINGBALANCE).setValue(bankAccount.getOpeningBalance());
		get(ACTIVE).setValue(bankAccount.getIsActive());
		get(ASOF).setValue(new ClientFinanceDate(bankAccount.getAsOf()));
		get(COMMENTS).setValue(bankAccount.getComment());
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	private String getBankAccountType(int type) {
		AccounterMessages messages = Global.get().messages();
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

	@Override
	protected String getWelcomeMessage() {
		if (bankAccount.getID() == 0) {
			return "Create Bank Account Command is activated.";
		}
		return "Update Bank Account(" + bankAccount.getName()
				+ ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		if (bankAccount.getID() == 0) {
			return getMessages().readyToCreate(getMessages().bankAccount());
		} else {
			return getMessages().readyToUpdate(getMessages().bankAccount());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_TYPE).setDefaultValue("Bank");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(ASOF).setDefaultValue(new ClientFinanceDate());
		get(ACCOUNT_NUMBER).setDefaultValue(
				String.valueOf(autoGenerateAccountnumber(BANK_CAT_BEGIN_NO,
						BANK_CAT_END_NO, context.getCompany())));

	}

	@Override
	public String getSuccessMessage() {
		if (bankAccount.getID() == 0) {
			return getMessages()
					.createSuccessfully(getMessages().bankAccount());
		} else {
			return getMessages()
					.updateSuccessfully(getMessages().bankAccount());
		}
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		bankAccount.setType(Account.TYPE_BANK);
		bankAccount.setName((String) get(ACCOUNT_NAME).getValue());
		bankAccount.setNumber((String) get(ACCOUNT_NUMBER).getValue());
		bankAccount.setOpeningBalance((Double) get(OPENINGBALANCE).getValue());
		ClientFinanceDate d = get(ASOF).getValue();
		bankAccount.setAsOf(d.getDate());
		bankAccount.setComment((String) get(COMMENTS).getValue());
		String type = get(BANK_ACCOUNT_TYPE).getValue();
		bankAccount.setBankAccountType(getType(type));
		String number = get(BANK_ACCOUNT_NUMBER).getValue();
		bankAccount.setBankAccountNumber(number);
		bankAccount.setIsActive((Boolean) get(ACTIVE).getValue());
		bankAccount.setOpeningBalanceEditable(true);
		bankAccount.setCurrencyFactor(1.0);
		create(bankAccount, context);
		return null;
	}

	private int getType(String type) {
		AccounterMessages messages = Global.get().messages();
		if (type.equals(messages.saving()))
			return Account.BANK_ACCCOUNT_TYPE_SAVING;
		else if (type.equals(messages.checking()))
			return Account.BANK_ACCCOUNT_TYPE_CHECKING;
		else if (type.equals(messages.moneyMarket()))
			return Account.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
		else
			return Account.BANK_ACCCOUNT_TYPE_NONE;

	}
}
