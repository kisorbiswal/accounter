package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewAccountCommand extends NewAbstractCommand {

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
	private ClientAccount account;

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getWelcomeMessage() {
		if (account.getID() == 0) {
			return "Create Account Command is activated.";
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

		list.add(new NameRequirement(ACCOUNT_NAME, getMessages()
				.pleaseEnterName(getMessages().account()),
				getMessages().name(), false, true));

		list.add(new NumberRequirement(ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getMessages().accountNumber()), getMessages()
				.accountNumber(), false, true) {
			@Override
			public void setValue(Object value) {
				try {
					if (account.getID() == 0) {
						int parseInt = Integer.parseInt((String) value);
						if (parseInt > 2000 || parseInt < 1100) {
							addFirstMessage(getMessages()
									.nameShouldbewithinThe1100to1199Ranage());
							return;
						}
					}
					super.setValue(value);
				} catch (Exception e) {
				}
			}
		});

		list.add(new AmountRequirement(OPENINGBALANCE, getMessages()
				.pleaseEnter(getMessages().openBalance()), getMessages()
				.openBalance(), true, true));

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
				if (NewAccountCommand.this.get(ACCOUNT_TYPE).getValue()
						.equals(getMessages().creditCard())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

		});
		list.add(new NumberRequirement(CARD_OR_LOAD_NUMBER, getMessages()
				.pleaseEnter(getMessages().cardOrLoadNumber()), getMessages()
				.cardOrLoadNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (NewAccountCommand.this.get(ACCOUNT_TYPE).getValue()
						.equals(getMessages().creditCard())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
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
			return getMessages().readyToCreate(getMessages().account());
		} else {
			return getMessages().readyToUpdate(getMessages().account());
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_NUMBER).setDefaultValue(
				String.valueOf(autoGenerateAccountnumber(1100, 1999,
						context.getCompany())));
		get(ACCOUNT_TYPE).setDefaultValue(getMessages().income());
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		if (account.getID() == 0) {
			return getMessages().createSuccessfully(getMessages().account());
		} else {
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
		if (accType == "Credit card") {
			account.setCreditLimit((Double) get(CREDIT_LIMIT).getValue());
			account.setCardOrLoanNumber((String) get(CARD_OR_LOAD_NUMBER)
					.getValue());
		}
		account.setOpeningBalanceEditable(true);
		account.setDefault(true);
		account.setType(getAccountType(accType));
		account.setName(accname);
		account.setNumber(accountNum);
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(asOf.getDate());
		account.setConsiderAsCashAccount(isCashAcount);
		account.setComment(comment);
		account.setCurrencyFactor(1.0);
		create(account, context);
		return null;
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
			account = new ClientAccount();
			if (!string.isEmpty()) {
				get(ACCOUNT_TYPE).setValue(string);
				get(ACCOUNT_TYPE).setEditable(false);
			}
			return null;
		}

		account = CommandUtils.getAccountByName(context.getCompany(), string);
		if (account == null) {
			long numberFromString = getNumberFromString(string);
			if (numberFromString != 0) {
				string = String.valueOf(numberFromString);
			}
			account = CommandUtils.getAccountByNumber(context.getCompany(),
					string);
		}
		if (account == null) {
			addFirstMessage(context, "Select an account to update.");
			return "Accounts " + string.trim();
		}

		get(ACCOUNT_TYPE).setValue(getAccountType(account.getType()));
		get(ACCOUNT_TYPE).setEditable(false);
		get(ACCOUNT_NAME).setValue(account.getName());

		get(ACCOUNT_NUMBER).setValue(account.getNumber());
		if (getAccountTypes().get(account.getType() - 1) == "Credit card") {
			get(CREDIT_LIMIT).setValue(account.getCreditLimit());
			get(CARD_OR_LOAD_NUMBER).setValue(account.getCardOrLoanNumber());
		}
		get(OPENINGBALANCE).setValue(account.getOpeningBalance());
		get(ACTIVE).setValue(account.getIsActive());
		get(CONSIDER_AS_CASH_ACCOUNT).setValue(
				account.isConsiderAsCashAccount());
		get(CONSIDER_AS_CASH_ACCOUNT).setEditable(false);
		get(ASOF).setValue(new ClientFinanceDate(account.getAsOf()));
		get(ASOF).setEditable(false);
		get(COMMENTS).setValue(account.getComment());
		return null;
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
}
