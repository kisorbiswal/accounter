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
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
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
			case IN_ACTIVE_ACCOUNTS:
				return accountsList(context, false);

			case ALL_ACCOUNTS:
				return accountsList(context, false);
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		Result result = activeRequirement(context, selection, list);
		if (result != null) {
			return result;
		}

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
		List<Account> accounts = getAccounts(accounts2, isActive);
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
		commandList.add("Add Account");

		result.add(message.toString());
		result.add(accountsList);
		result.add(commandList);

		ResultList actions = new ResultList("actions");
		Record inActiveRec = new Record(ActionNames.IN_ACTIVE_ACCOUNTS);
		inActiveRec.add("", "InActive Accounts");
		Record finish = new Record(ActionNames.ALL_ACCOUNTS);
		finish.add("", "All  Accounts");
		actions.add(inActiveRec);
		actions.add(finish);
		result.add(actions);

		return result;

	}

	private List<Account> getAccounts(Set<Account> accounts2, Boolean isActive) {
		List<Account> list = new ArrayList<Account>();

		for (Account a : accounts2) {
			if (isActive) {
				if (a.getIsActive()) {
					list.add(a);
				}
			} else {
				if (!a.getIsActive())
					list.add(a);
			}
		}

		return list;
	}

	/**
	 * 
	 */
	protected Record createAccountRecord(Account account) {
		Record record = new Record(account);
		record.add("Name:", account.getName());
		record.add("Balance", account.getTotalBalance());

		return record;
	}

}
