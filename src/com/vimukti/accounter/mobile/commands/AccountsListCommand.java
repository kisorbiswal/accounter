package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
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
public class AccountsListCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(ACTIVE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createAccountsList(context);
		return result;
	}

	private Result createAccountsList(Context context) {
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

		Result result = isActiveRequirement(context, selection);
		Boolean isActive = (Boolean) get(ACTIVE).getValue();
		result = accountsList(context, isActive);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result isActiveRequirement(Context context, Object selection) {
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
		return null;
	}

	private Result accountsList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList accountsList = new ResultList("accountsList");
		result.add("Accounts List");
		int num = 0;
		List<Account> accounts = getAccounts(isActive);
		for (Account account : accounts) {
			accountsList.add(createAccountRecord(account));
			num++;
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = accountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(accountsList);
		result.add(commandList);
		result.add("Type for Account");

		return result;

	}

	protected Record createAccountRecord(Account account) {
		Record record = new Record(account);
		record.add("Number", account.getNumber());
		record.add("Name", account.getName());
		record.add("Type", account.getType());
		record.add("Balance", account.getCurrentBalance());
		return record;
	}

}
