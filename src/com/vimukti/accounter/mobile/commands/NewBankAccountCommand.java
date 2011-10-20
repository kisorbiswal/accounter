package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Parasad N
 * 
 */
public class NewBankAccountCommand extends AbstractTransactionCommand {

	private static final String ACCOUNT_NAME = "Account Name";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACTIVE = "Active";
	private static final String ASOF = "AsOf";

	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_ACCOUNT_TYPE = "Bank Account Type";
	private static final String BANK_ACCOUNT_NUMBER = "Bank Account Number";
	private static final int BANK_NAME_TO_SHOW = 5;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(ACCOUNT_NAME, false, true));
		list.add(new Requirement(ACCOUNT_NUMBER, false, true));
		list.add(new Requirement(OPENINGBALANCE, true, true));
		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(ASOF, true, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(CONSIDER_AS_CASH_ACCOUNT, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_ACCOUNT_TYPE, false, true));
		list.add(new Requirement(BANK_ACCOUNT_NUMBER, true, true));
	}

	@Override
	public Result run(Context context) {

		Result result = null;

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().bankAccounts()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, ACCOUNT_NAME, getConstants()
				.name(), getMessages()
				.pleaseEnter(getConstants().accountName()));
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getConstants().Accounnumbers()), getConstants()
				.number());
		if (result != null) {
			return result;
		}
		result = bankAcountTypeReq(
				context,
				list,
				BANK_ACCOUNT_TYPE,
				getMessages().pleaseEnter(
						getConstants().bankAccounts() + getConstants().type()));
		if (result != null) {
			return result;
		}
		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		markDone();
		createBankAccountObject(context);

		result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().bankAccounts()));
		return result;
	}

	private void setDefaultValues() {
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(OPENINGBALANCE).setDefaultValue(0.0D);
		get(ASOF).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));
		get(MEMO).setDefaultValue("");
		get(BANK_ACCOUNT_NUMBER).setDefaultValue("");
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.FALSE);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		booleanOptionalRequirement(context, selection, list, ACTIVE,
				"This account is" + getConstants().active(), "This account is"
						+ getConstants().inActive());
		Result result = amountOptionalRequirement(context, list, selection,
				OPENINGBALANCE,
				getMessages().pleaseEnter(getConstants().openingBalance()),
				getConstants().openingBalance());
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, ASOF, getConstants()
				.asOf(), getMessages().pleaseEnter(getConstants().asOf()),
				selection);
		if (result != null) {
			return result;
		}
		result = cashDiscountRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankNameRequriment(context, list, selection, getMessages()
				.pleaseEnter(getConstants().bankName()));
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection,
				BANK_ACCOUNT_NUMBER,
				getMessages().pleaseEnter(getConstants().Accounnumbers()));
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				getMessages().pleaseEnter(getConstants().comments()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().bankAccounts()));
		actions.add(finish);

		return makeResult;
	}

	private void createBankAccountObject(Context context) {

		ClientBankAccount bankAccount = new ClientBankAccount();

		bankAccount.setType(ClientAccount.TYPE_BANK);
		bankAccount.setName((String) get(ACCOUNT_NAME).getValue());
		bankAccount.setNumber((String) get(ACCOUNT_NUMBER).getValue());
		bankAccount.setOpeningBalance((Double) get(OPENINGBALANCE).getValue());
		Date d = get(ASOF).getValue();

		bankAccount.setAsOf(new ClientFinanceDate(d).getDate());
		bankAccount.setComment((String) get(MEMO).getValue());
		String type = get(BANK_ACCOUNT_TYPE).getValue();
		bankAccount.setBankAccountType(getType(type));
		String number = get(BANK_ACCOUNT_NUMBER).getValue();
		bankAccount.setBankAccountNumber(number);
		bankAccount.setConsiderAsCashAccount((Boolean) get(
				CONSIDER_AS_CASH_ACCOUNT).getValue());
		bankAccount.setIsActive((Boolean) get(ACTIVE).getValue());

		create(bankAccount, context);
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

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @param string
	 * @return
	 */
	private Result bankNameRequriment(Context context, ResultList list,
			Object selection, String string) {

		ClientBank bankobj = context.getSelection(BANK_NAME);
		Requirement bankreq = get(BANK_NAME);

		if (bankobj != null) {
			bankreq.setValue(bankobj);
		}

		ClientBank bank = (ClientBank) bankreq.getValue();
		if (selection == string) {
			return banks(context, bank);
		}

		Record paymentTermRecord = new Record(string);
		paymentTermRecord.add("Name", string);
		paymentTermRecord.add("Value", bank == null ? "" : bank.getName());
		list.add(paymentTermRecord);

		return null;

	}

	/**
	 * 
	 * @param context
	 * @param oldBank
	 * @return
	 */
	private Result banks(Context context, ClientBank oldBank) {

		ArrayList<ClientBank> banks = getClientCompany().getBanks();
		Result result = context.makeResult();
		result.add(getMessages().pleaseSelect(getConstants().bankName()));

		ResultList list = new ResultList(BANK_NAME);
		int num = 0;
		if (oldBank != null) {
			list.add(createbankRecord(oldBank));
			num++;
		}
		for (ClientBank bank : banks) {
			if (bank != oldBank) {
				list.add(createbankRecord(bank));
				num++;
			}
			if (num == BANK_NAME_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create New");
		result.add(commandList);
		return result;

	}

	/**
	 * 
	 * @param bank
	 * @return
	 */
	private Record createbankRecord(ClientBank bank) {
		Record record = new Record(bank);
		record.add("Name", BANK_NAME);
		record.add("Value", bank.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @param bankAccountType2
	 * @param list
	 * @return
	 */
	private Result bankAcountTypeReq(Context context, ResultList list,
			String bankAccountType2, String string) {
		Requirement bankAccountTypeReq = get(BANK_ACCOUNT_TYPE);
		String bankAccountType = context.getSelection(BANK_ACCOUNT_TYPE);
		if (bankAccountType != null) {
			bankAccountTypeReq.setValue(bankAccountType);
		}
		if (!bankAccountTypeReq.isDone()) {
			return bankAccountType(context, null);
		}

		Record nameRecord = new Record(bankAccountType);
		nameRecord.add("", getConstants().type());
		nameRecord.add("", bankAccountTypeReq.getValue());
		list.add(nameRecord);
		return null;

	}

	/**
	 * 
	 * @param context
	 * @param oldBankaccountType
	 * @return
	 */
	private Result bankAccountType(Context context, String oldBankaccountType) {
		List<String> bankAccountTypes = new ArrayList<String>();
		bankAccountTypes
				.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING);

		bankAccountTypes
				.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET);
		bankAccountTypes
				.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING);
		Result result = context.makeResult();
		result.add("Select Bank Account type");

		ResultList list = new ResultList(BANK_ACCOUNT_TYPE);
		int num = 0;
		if (oldBankaccountType != null) {
			list.add(createPayMentMethodRecord(oldBankaccountType));
			num++;
		}
		for (String bankAccountType : bankAccountTypes) {
			if (bankAccountType != oldBankaccountType) {
				list.add(createPayMentMethodRecord(bankAccountType));
				num++;
			}
			if (num == BANK_ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		result.add(list);
		return result;
	}

	private Result cashDiscountRequirement(Context context, ResultList list,
			Object selection) {
		Requirement isCashAccountReq = get(CONSIDER_AS_CASH_ACCOUNT);

		Boolean isCashAccoount = (Boolean) isCashAccountReq.getValue();
		if (selection == CONSIDER_AS_CASH_ACCOUNT) {
			isCashAccoount = !isCashAccoount;
			isCashAccountReq.setValue(isCashAccoount);
		}
		String isCashAccount = "";
		if (isCashAccoount) {
			isCashAccount = getMessages().thisIsConsideredACashAccount(
					Global.get().account());
		} else {
			isCashAccount = "This account is not a cash account";
		}
		Record isCashAccountRecord = new Record(CONSIDER_AS_CASH_ACCOUNT);
		isCashAccountRecord.add("", CONSIDER_AS_CASH_ACCOUNT);
		isCashAccountRecord.add(":", isCashAccount);
		list.add(isCashAccountRecord);
		return null;
	}

}
