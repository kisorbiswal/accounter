package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientBank;

/**
 * 
 * @author vimukti2
 * 
 */

public class NewBankAccountCommand extends AbstractTransactionCommand {
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACCOUNT_NAME = "Account Name";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACTIVE = "Active";
	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_ACCOUNT_TYPE = "Bank Account Type";
	private static final String BANK_ACCOUNT_NUMBER = "Bank Account Type";
	private static final int BANK_NAME_TO_SHOW = 5;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(ACCOUNT_TYPE, true, true));
		list.add(new Requirement(ACCOUNT_NAME, false, true));
		list.add(new Requirement(ACCOUNT_NUMBER, false, true));
		list.add(new Requirement(OPENINGBALANCE, false, true));
		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(ASOF, true, true));
		list.add(new Requirement(COMMENTS, true, true));
		list.add(new Requirement(CONSIDER_AS_CASH_ACCOUNT, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_ACCOUNT_TYPE, false, true));
		list.add(new Requirement(BANK_ACCOUNT_NUMBER, true, true));
	}

	@Override
	public Result run(Context context) {

		Result result = new Result();// numberRequirement(context);
		if (result == null) {
			// TODO
		}
		result = nameRequirement(context);
		if (result == null) {
			// TODO
		}
		result = bankAcountTypeReq(context);
		if (result == null) {
			// TODO
		}
		result = createOptionalReq(context);
		if (result == null) {
			// TODO
		}
		createBankAccountObject(context);
		markDone();
		return result;
	}

	private void createBankAccountObject(Context context) {

		BankAccount bankAccount = new BankAccount();

		bankAccount.setType(2);
		bankAccount.setName((String) get(ACCOUNT_NAME).getValue());
		bankAccount.setNumber((String) get(ACCOUNT_NUMBER).getValue());
		bankAccount.setOpeningBalance((Double) get(OPENINGBALANCE).getValue());
		bankAccount.setAsOf(new FinanceDate((Long) get(ASOF).getValue()));
		bankAccount.setComment((String) get(COMMENTS).getValue());
		// bankAccount.setBank(bank)
		bankAccount.setBankAccountType((Integer) get(BANK_ACCOUNT_TYPE)
				.getValue());
		bankAccount.setBankAccountNumber((String) get(BANK_ACCOUNT_NUMBER)
				.getValue());
		bankAccount.setConsiderAsCashAccount((Boolean) get(
				CONSIDER_AS_CASH_ACCOUNT).getValue());
		bankAccount.setIsActive((Boolean) get(ACTIVE).getValue());

		create(bankAccount, context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createOptionalReq(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement accNameReq = get(ACCOUNT_NAME);
		String name = (String) accNameReq.getValue();
		if (name == selection) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_NAME);
			return text(context, "Please Enter the account name", name);
		}

		Requirement accounNumberReq = get(ACCOUNT_NUMBER);
		String num = (String) accounNumberReq.getValue();
		if (num == selection) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_NUMBER);
			return text(context, "Please Enter Accoount Number", num);
		}

		Requirement bankaccTypeRequirement = get(BANK_ACCOUNT_TYPE);
		String bankaccountType = bankaccTypeRequirement.getValue();
		if (bankaccountType == selection) {
			context.setAttribute(INPUT_ATTR, BANK_ACCOUNT_TYPE);
			return text(context, "please enter Bank Account Type",
					bankaccountType);
		}

		Record numberRecord = new Record(num);
		numberRecord.add(INPUT_ATTR, "accountNumber");
		numberRecord.add("Value", num);
		list.add(numberRecord);

		Record nameRecord = new Record(name);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);
		// ACTIVE
		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This account is Active";
		} else {
			activeString = "This account is InActive";
		}
		Record isActiveRecord = new Record(ACTIVE);
		isActiveRecord.add("Name", "");
		isActiveRecord.add("Value", activeString);
		list.add(isActiveRecord);
		// OPENINGBALANCE
		Requirement openingBalanceReq = get(OPENINGBALANCE);
		Double bal = (Double) openingBalanceReq.getValue();
		if (bal == selection) {
			context.setAttribute(INPUT_ATTR, OPENINGBALANCE);
			openingBalanceReq.setValue(0.0D);
		}
		Record openingBalRec = new Record(OPENINGBALANCE);
		openingBalRec.add("Name", OPENINGBALANCE);
		openingBalRec.add("Value", bal);
		list.add(openingBalRec);

		Result result = dateOptionalRequirement(context, list, ASOF,
				"Enter As Of date", selection);
		if (result != null) {
			return result;
		}
		// CONSIDER_AS_CASH_ACCOUNT
		Requirement isCashAccountReq = get(CONSIDER_AS_CASH_ACCOUNT);
		Boolean isCashAccoount = (Boolean) isCashAccountReq.getValue();
		if (selection == isCashAccoount) {
			context.setAttribute(INPUT_ATTR, CONSIDER_AS_CASH_ACCOUNT);
			isCashAccoount = !isCashAccoount;
			isCashAccountReq.setValue(isCashAccoount);
		}
		String isCashAccount = "";
		if (isCashAccoount) {
			isCashAccount = "This account is cash account";
		} else {
			isCashAccount = "This account is not a cash account";
		}
		Record isCashAccountRecord = new Record(CONSIDER_AS_CASH_ACCOUNT);
		isCashAccountRecord.add("Name", "");
		isCashAccountRecord.add("Value", isCashAccount);
		list.add(isCashAccountRecord);
		// COMMENTS
		result = stringOptionalRequirement(context, list, selection, COMMENTS,
				"Enter Comments Text");
		if (result != null) {
			return result;
		}
		// BANK_NAME
		result = bankNameOptionalRequriment(context, list, selection);
		if (result != null) {
			return result;
		}
		// BANK_ACCOUNT_NUMBER
		result = numberOptionalRequirement(context, list, selection,
				BANK_ACCOUNT_NUMBER, "Enter Bank account Number.");
		if (result != null) {
			return result;
		}

		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Bank Account.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result bankNameOptionalRequriment(Context context, ResultList list,
			Object selection) {

		Object bankobj = context.getSelection(BANK_NAME);
		Requirement bankreq = get(BANK_NAME);

		ClientBank bank = (ClientBank) bankreq.getValue();

		if (selection == bank) {
			return banks(context, bank);

		}
		if (bankobj != null) {
			bank = (ClientBank) bankobj;
			bankreq.setValue(bank);
		}

		Record paymentTermRecord = new Record(bank);
		paymentTermRecord.add("Name", BANK_NAME);
		paymentTermRecord.add("Value", bank.getName());
		list.add(paymentTermRecord);

		Result result = new Result();
		result.add(list);

		return result;

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
		result.add("Select Bank Name");

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
		commandList.add("Create banks");
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
	 * @return
	 */
	private Result bankAcountTypeReq(Context context) {
		Requirement bankAccountTypeReq = get(BANK_ACCOUNT_TYPE);
		String bankAccountType = context.getSelection(BANK_ACCOUNT_TYPE);
		if (bankAccountType != null) {
			bankAccountTypeReq.setValue(bankAccountType);
		}
		if (!bankAccountTypeReq.isDone()) {
			return bankAccountType(context, null);
		}
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
		bankAccountTypes.add("Checking");
		bankAccountTypes.add("Money Market");
		bankAccountTypes.add("Saving");
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

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result nameRequirement(Context context) {
		Requirement nameReq = get(ACCOUNT_NAME);
		if (!nameReq.isDone()) {
			String string = context.getString();
			if (string != null) {
				nameReq.setValue(string);
			} else {
				return text(context, "Please Enter the account name ", string);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(ACCOUNT_NAME)) {
			nameReq.setValue(input);
		}
		return null;
	}

}
