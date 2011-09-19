package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewAccountCommand extends AbstractTransactionCommand {
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACTIVE = "Active";
	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";

	private static final int ACCOUNT_TYPES_TO_SHOW = 5;
	private static final String INPUT_ATTR = "input";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
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
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		result = accountTypeRequirement(context);
		if (result != null) {
			return result;
		}
		result = nameRequirement(context);
		if (result != null) {
			return result;
		}
		result = accountNumberRequirement(context);
		if (result != null) {
			return result;
		}
		result = openingBalanceRequirement(context);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return null;
		}

		return null;
	}

	private Result createOptionalResult(Context context) {
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

		Requirement accTypeReq = get(ACCOUNT_TYPE);
		// TODO

		Requirement accNameReq = get(ACCOUNT_NAME);
		String name = (String) accNameReq.getValue();
		if (name == selection) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_NAME);
			return text(context, "Please Enter the account name", name);
		}

		Requirement accounNumberReq = get(ACCOUNT_NUMBER);
		Integer num = (Integer) accounNumberReq.getValue();
		if (num == selection) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_NUMBER);
			return number(context, "Please Enter Accoount Number",
					String.valueOf(num));
		}

		return null;
	}

	private Result openingBalanceRequirement(Context context) {
		Requirement openingBal = get(OPENINGBALANCE);
		Double balance = context.getSelection(OPENINGBALANCE);
		if (balance != null) {
			openingBal.setValue(balance);
		}
		if (!openingBal.isDone()) {
			return getResultToAsk(context, "Please enter opening Balance:");
		}

		return null;
	}

	private Result accountTypeRequirement(Context context) {
		Requirement accType = get(ACCOUNT_TYPE);
		if (!accType.isDone()) {
			return getAccountTypesResult(context);
		}
		return null;
	}

	private Result getAccountTypesResult(Context context) {
		Result result = context.makeResult();
		ResultList accountTypesList = new ResultList("");

		return result;
	}

	private Record createAccountTypeRecord(Object last) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result accountNumberRequirement(Context context) {
		Requirement accNumber = get(ACCOUNT_NUMBER);
		int number = context.getSelection(ACCOUNT_NUMBER);
		if (number != 0) {
			accNumber.setValue(number);
		}
		if (!accNumber.isDone()) {
			return getResultToAsk(context, "Please enter Account Number:");
		}
		return null;
	}

	private Result nameRequirement(Context context) {
		Requirement accName = get(ACCOUNT_NAME);
		String accountName = context.getSelection(ACCOUNT_NAME);
		if (accountName != null) {
			accName.setValue(accountName);
		}
		if (!accName.isDone()) {
			return getResultToAsk(context, "Please enter the Accoount name.");
		}
		return null;
	}

	private Result getResultToAsk(Context context, String mesg) {
		Result result = context.makeResult();
		result.add(mesg);
		return result;
	}

}
