package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewAccountCommand extends AbstractTransactionCommand {

	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";

	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";

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
		list.add(new Requirement(OPENINGBALANCE, true, true));
		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(ASOF, true, true));
		list.add(new Requirement(COMMENTS, true, true));
		list.add(new Requirement(CONSIDER_AS_CASH_ACCOUNT, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().account()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, ACCOUNT_NAME, getConstants()
				.accountName(),
				getMessages().pleaseEnter(getConstants().accountName()));
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, ACCOUNT_NUMBER, getMessages()
				.pleaseEnter(getConstants().Accounnumbers()), getConstants()
				.account() + getConstants().number());
		if (result != null) {
			return result;
		}

		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		markDone();
		return createNewAccount(context);

	}

	private void setDefaultValues() {
		get(OPENINGBALANCE).setDefaultValue(0.0D);
		get(ACCOUNT_TYPE).setDefaultValue("Income");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(ASOF).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));
		get(COMMENTS).setDefaultValue(" ");
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
	}

	private Result createNewAccount(Context context) {

		ClientAccount account = new ClientAccount();

		String accType = (String) get(ACCOUNT_TYPE).getValue();
		String accname = (String) get(ACCOUNT_NAME).getValue();
		String accountNum = (String) get(ACCOUNT_NUMBER).getValue();
		double openingBal = (Double) get(OPENINGBALANCE).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		boolean isCashAcount = (Boolean) get(CONSIDER_AS_CASH_ACCOUNT)
				.getValue();
		Date asOf = get(ASOF).getValue();
		String comment = get(COMMENTS).getValue();

		account.setDefault(true);
		account.setType(getAccTypes().indexOf(accType) + 1);
		account.setName(accname);
		account.setNumber(String.valueOf(accountNum));
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(asOf.getTime());
		account.setConsiderAsCashAccount(isCashAcount);
		account.setComment(comment);
		create(account, context);
		markDone();

		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().account()));

		return result;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

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

		Result result = accountTypesRequirement(context, selection, list);
		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, selection, list, ACTIVE,
				"This account is Active", "This account is InActive");

		result = amountOptionalRequirement(context, list, selection,
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

		result = commentsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().account()));
		actions.add(finish);

		return makeResult;

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
			return text(context,
					getMessages().pleaseEnter(getConstants().comment()), null);
		}

		Record memoRecord = new Record(COMMENTS);
		memoRecord.add("", COMMENTS);
		memoRecord.add("", comments);
		list.add(memoRecord);
		return null;
	}

}
