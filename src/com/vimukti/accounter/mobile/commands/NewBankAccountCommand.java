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

		list.add(new Requirement(ACCOUNT_NAME, false, true));
		list.add(new Requirement(ACCOUNT_NUMBER, false, true));
		list.add(new Requirement(OPENINGBALANCE, true, true));
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

		Result result = null;

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult
				.add(" BankAccount is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, ACCOUNT_NAME,
				"Please Enter the account name ");
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, ACCOUNT_NUMBER,
				"Please Enter the account number ");
		if (result != null) {
			return result;
		}
		result = bankAcountTypeReq(context, list, BANK_ACCOUNT_TYPE,
				"BankAccountType");
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
		return result;
	}

	private void setDefaultValues() {
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(OPENINGBALANCE).setDefaultValue(0.0D);
		get(ASOF).setDefaultValue(new Date(System.currentTimeMillis()));
		get(COMMENTS).setDefaultValue("");
		get(BANK_NAME).setDefaultValue(new ClientBank());
		get(BANK_ACCOUNT_NUMBER).setDefaultValue(0);
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
				"This account is Active", "This account is InActive");

		Result result = amountOptionalRequirement(context, list, selection,
				OPENINGBALANCE, "Opening balance");
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, ASOF, "Enter AsOfDate",
				selection);
		if (result != null) {
			return result;
		}
		result = cashDiscountRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = bankNameRequriment(context, list, selection, "Bank Name");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection,
				BANK_ACCOUNT_NUMBER, "Enter BankAccount Number");
		if (result != null) {
			return result;
		}
		result = commentsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create BankAccount");
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
		bankAccount.setComment((String) get(COMMENTS).getValue());
		String type = get(BANK_ACCOUNT_TYPE).getValue();
		bankAccount.setBankAccountType(getType(type));
		bankAccount.setBankAccountNumber((String) get(BANK_ACCOUNT_NUMBER)
				.getValue());
		bankAccount.setConsiderAsCashAccount((Boolean) get(
				CONSIDER_AS_CASH_ACCOUNT).getValue());
		bankAccount.setIsActive((Boolean) get(ACTIVE).getValue());

		create(bankAccount, context);
	}

	private int getType(String type) {
		// TODO
		return 1;

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
		nameRecord.add("", "Name");
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
			isCashAccount = "This account is cash account";
		} else {
			isCashAccount = "This account is not a cash account";
		}
		Record isCashAccountRecord = new Record(CONSIDER_AS_CASH_ACCOUNT);
		isCashAccountRecord.add("", CONSIDER_AS_CASH_ACCOUNT);
		isCashAccountRecord.add(":", isCashAccount);
		list.add(isCashAccountRecord);
		return null;
	}

	private Result commentsRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(COMMENTS);
		String comments = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(COMMENTS)) {
			String input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getString();
			}
			comments = input;
			req.setValue(comments);
		}

		if (selection == COMMENTS) {
			context.setAttribute(INPUT_ATTR, COMMENTS);
			return text(context, "Enter Comments", null);
		}

		Record memoRecord = new Record(COMMENTS);
		memoRecord.add("", COMMENTS);
		memoRecord.add("", comments);
		list.add(memoRecord);
		return null;
	}

}
