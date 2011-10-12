package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		markDone();
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
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

	/**
	 * 
	 * @param context
	 * @param isActive
	 * @return
	 */
	private Result accountsList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList accountsList = new ResultList("accountsList");
		result.add("Accounts List");
		int num = 0;
		Set<Account> accounts2 = context.getCompany().getAccounts();// getAccounts(isActive,context.getCompany());
		List<Account> accounts = new ArrayList<Account>(accounts2);
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
		commandList.add("Create Account");
		commandList.add("Edit");
		commandList.add("Delete");
		commandList.add("More");

		result.add(message.toString());
		result.add(accountsList);
		result.add(commandList);

		return result;

	}

	/**
	 * 
	 */
	protected Record createAccountRecord(Account account) {
		Record record = new Record(account);
		return record;
	}

}
