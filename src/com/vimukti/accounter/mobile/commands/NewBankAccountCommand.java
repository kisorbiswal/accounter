package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Parasad N
 * 
 */
public class NewBankAccountCommand extends NewAbstractCommand {

	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String ACCOUNT_NAME = "Account Name";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String BANK_ACCOUNT_TYPE = "Bank Account Type";
	private static final String ACTIVE = "Active";

	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_ACCOUNT_NUMBER = "Bank Account Number";
	private static final String ACCOUNT_TYPE = "Account Type";
	private ClientBankAccount account;

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(ACCOUNT_TYPE, "Please Enter Account Type",
				"Account Type", false, true));

		list.add(new NumberRequirement(ACCOUNT_NUMBER,
				"Please Enter Account number", "Account Number", false, true));

		list.add(new NameRequirement(ACCOUNT_NAME, "Please Enter Account Name",
				"Account Name", false, true));

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "This account is Active";
			}

			@Override
			protected String getFalseString() {
				return "This account is InActive";
			}
		});

		list.add(new AmountRequirement(OPENINGBALANCE,
				"Please Enter Opening balece", "Opening balence", true, true));

		list.add(new DateRequirement(ASOF, "Please Enter As Of Date",
				"As Of Date", true, true));

		list.add(new StringRequirement(COMMENTS, "Please Enter Comment",
				"Comment", true, true));

		list.add(new StringListRequirement(BANK_NAME, "Please Enter Bank Name",
				"Bank Name", true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Bank Name has been Selected";
			}

			@Override
			protected String getSelectString() {
				return "Select Bank Name";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getBankNameList(getClientCompany());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(BANK_ACCOUNT_TYPE,
				"Please Enter Bank Account Type", "Bank Account Type", false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return "Account type has been selected";
			}

			@Override
			protected String getSelectString() {
				return "Select Account type";
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

		list.add(new NumberRequirement(BANK_ACCOUNT_NUMBER,
				"Please Enter  Bank Account number", "Bank Account Number",
				true, true));

	}

	protected List<String> getBankNameList(ClientCompany clientCompany) {
		List<String> bankNameList = new ArrayList<String>();
		List<ClientBank> banksList = clientCompany.getBanks();
		if (!banksList.isEmpty()) {
			for (ClientBank clientBank : banksList) {
				bankNameList.add(clientBank.getName());
			}
			return bankNameList;
		}
		return null;
	}

	protected List<String> getAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Current Account");
		list.add("Checking");
		list.add("Money Market");
		list.add("Saving");
		return list;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// String string = context.getString();
		// if (string.isEmpty()) {
		// if (!isUpdate) {
		// account = new ClientBankAccount();
		// return null;
		// }
		// }
		//
		// account = context.getClientCompany().getAccountByName(string);
		// if (account == null) {
		// account = context.getClientCompany().getAccountByNumber(
		// getNumberFromString(string));
		// }
		// if (account == null) {
		// addFirstMessage(context, "Select an account to update.");
		// return "Accounts " + string.trim();
		// }
		//
		// get(ACCOUNT_TYPE)
		// .setValue(getAccountTypes().get(account.getType() - 1));
		//
		// get(ACCOUNT_NAME).setValue(account.getName());
		// get(ACCOUNT_NAME).setEditable(false);
		//
		// get(ACCOUNT_NUMBER).setValue(account.getNumber());
		// get(ACCOUNT_NUMBER).setEditable(false);
		//
		// get(OPENINGBALANCE).setValue(account.getOpeningBalance());
		// get(ACTIVE).setValue(account.getIsActive());
		// get(CONSIDER_AS_CASH_ACCOUNT).setValue(
		// account.isConsiderAsCashAccount());
		// get(ASOF).setValue(new ClientFinanceDate(account.getAsOf()));
		// get(COMMENTS).setValue(account.getComment());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// if (account.getID() == 0) {
		return "Create Bank Account Command is activated.";
		// }
		// return "Update Bank Account(" + account.getName()
		// + ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		// if (account.getID() == 0) {
		return " Bank Account is ready to created with following details.";
		// } else {
		// return "Bank Account is ready to updated with following details.";
		// }
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_TYPE).setDefaultValue("Bank");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(ASOF).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		// if (account.getID() == 0) {
		return "Account is created succesfully.";
		// } else {
		// return "Account is updated successfully.";
		// }
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientBankAccount bankAccount = new ClientBankAccount();
		bankAccount.setType(ClientAccount.TYPE_BANK);
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

		create(bankAccount, context);
		return null;
	}

	private int getType(String type) {
		if (type.equals(AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING))
			return ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
		else if (type
				.equals(AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING))
			return ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
		else if (type
				.equals(AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET))
			return ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
		else
			return ClientAccount.BANK_ACCCOUNT_TYPE_NONE;

	}
}
