package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewAccountCommand extends AbstractTransactionCommand {
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACTIVE = "Active";
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
		list.add(new Requirement(OPENINGBALANCE, false, true));
		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(ASOF, true, true));
		list.add(new Requirement(COMMENTS, true, true));
		list.add(new Requirement(CONSIDER_AS_CASH_ACCOUNT, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = accountNumberRequirement(context);
		if (result != null) {
			return result;
		}

		result = nameRequirement(context);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		return createNewAccount(context);
		// markDone();

	}

	private Result createNewAccount(Context context) {
		Account account = new Account();

		Integer accType = (Integer) get(ACCOUNT_TYPE).getValue();
		String accname = (String) get(ACCOUNT_NAME).getValue();
		String accountNum = (String) get(ACCOUNT_NUMBER).getValue();
		double openingBal = (Double) get(OPENINGBALANCE).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		boolean isCashAcount = (Boolean) get(CONSIDER_AS_CASH_ACCOUNT)
				.getValue();
		Date asOf = (Date) get(ASOF).getValue();

		account.setType(accType);
		account.setName(accname);
		account.setNumber(accountNum);
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(new FinanceDate(asOf));
		account.setConsiderAsCashAccount(isCashAcount);

		Session session = context.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(account);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(" Account was created successfully.");

		return result;
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
		Integer actype = (Integer) accTypeReq.getValue();
		if (actype == selection) {
			context.setAttribute(INPUT_ATTR, ACCOUNT_TYPE);
			return number(context, "Please enter the account Type", "" + actype);
		}

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

		ResultList list = new ResultList("values");

		Record accTypeRecord = new Record(actype);
		accTypeRecord.add(INPUT_ATTR, "actype");
		accTypeRecord.add("Value", actype);
		list.add(accTypeRecord);

		Record numberRecord = new Record(num);
		numberRecord.add(INPUT_ATTR, "accountNumber");
		numberRecord.add("Value", num);
		list.add(numberRecord);

		Record nameRecord = new Record(name);
		nameRecord.add(INPUT_ATTR, "Name");
		nameRecord.add("Value", name);
		list.add(nameRecord);

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

		Result result = asOfDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

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

		result = commentsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add(" Account is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Account");
		actions.add(finish);
		result.add(actions);

		return result;

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

		if (selection == comments) {
			context.setAttribute(attribute, COMMENTS);
			return text(context, "Enter Comments", comments);
		}

		Record memoRecord = new Record(comments);
		memoRecord.add("Name", COMMENTS);
		memoRecord.add("Value", comments);
		list.add(memoRecord);
		return null;
	}

	private Result accountNumberRequirement(Context context) {
		Requirement numberReq = get(ACCOUNT_NUMBER);
		if (!numberReq.isDone()) {
			String num = context.getString();
			if (num != null) {
				numberReq.setValue(num);
			} else {
				return text(context, "Please Enter the account number ", ""
						+ num);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(ACCOUNT_NUMBER)) {
			numberReq.setValue(input);
		}
		return null;
	}

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

	private Result asOfDateRequirement(Context context, ResultList list,
			Object selection) {

		Requirement dateReq = get("asof");
		Date asOfDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ASOF)) {
			Date date = context.getSelection(ASOF);
			if (date == null) {
				date = context.getDate();
			}
			asOfDate = date;
			dateReq.setValue(asOfDate);
		}
		if (selection == asOfDate) {
			context.setAttribute(INPUT_ATTR, "asOf");
			return date(context, "Enter asOf Date", asOfDate);
		}

		Record asOfDateRecord = new Record(asOfDate);
		asOfDateRecord.add("Name", "asOf");
		asOfDateRecord.add("Value", asOfDate.toString());
		list.add(asOfDateRecord);

		return null;
	}
}
