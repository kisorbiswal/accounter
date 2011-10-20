package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;

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

	}

	@Override
	public Result run(Context context) {
		Result result = createAccountsList(context);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createAccountsList(Context context) {

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return null;
			case ACTIVE:
				context.setAttribute(ACCOUNT_TYPE, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(ACCOUNT_TYPE, false);
				break;
			case ALL:
				context.setAttribute(ACCOUNT_TYPE, null);
				break;
			default:
				break;
			}
		}
		Result result = accountsList(context, selection);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @param selection
	 * @param isActive
	 * @return
	 */
	private Result accountsList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList accountsList = new ResultList("accountsList");
		result.add(Global.get().Account() + "list");

		Boolean accountType = (Boolean) context.getAttribute(ACCOUNT_TYPE);
		List<Account> accounts = getAccounts(context.getCompany(), accountType);

		ResultList actions = new ResultList("actions");

		List<Account> pagination = pagination(context, selection, actions,
				accounts, new ArrayList<Account>(), ACCOUNTS_TO_SHOW);

		for (Account account : pagination) {
			accountsList.add(createAccountRecord(account));
		}

		StringBuilder message = new StringBuilder();
		if (accountsList.size() > 0) {
			message.append(getMessages().selectAccountsToAssign(
					Global.get().account()));
		}

		result.add(message.toString());
		result.add(accountsList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", getConstants().active() + Global.get().Account());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", getMessages().inActive(Global.get().Accounts()));
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getMessages().addanewAccount(Global.get().account()));
		result.add(commandList);
		return result;

	}

	private List<Account> getAccounts(Company company, Boolean isActive) {
		List<Account> list = new ArrayList<Account>();
		Set<Account> accounts = company.getAccounts();
		if (isActive == null) {
			return new ArrayList<Account>(accounts);
		}
		for (Account a : accounts) {
			if (a.getIsActive() == isActive) {
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
