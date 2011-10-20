package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class MergeAccountCommand extends AbstractTransactionCommand {

	private static final String ACCOUNT_FROM = "accoountFromType";
	private static final String ACCOUNT_TO = "accountToType";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACCOUNT_FROM, false, true));
		list.add(new Requirement(ACCOUNT_TO, false, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;
		result = createOptionalResult(context);
		return result;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACCOUNT_FROM);

		ResultList list = new ResultList("merge");
		Result result = accountFromRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		selection = context.getSelection(ACCOUNT_TO);
		result = accountToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Account fromAccount = (Account) get(ACCOUNT_FROM).getValue();
		Account toAccount = (Account) get(ACCOUNT_TO).getValue();
		mergeAccount(context, fromAccount, toAccount);
		return result;

	}

	private void mergeAccount(Context context, Account fromAccount,
			Account toAccount) {
		// TODO Auto-generated method stub

	}

	private Result accountToRequirement(Context context, ResultList list,
			Object selection) {
		Object accountObj = context.getSelection(ACCOUNT_TO);
		Requirement accToRequirement = get(ACCOUNT_TO);
		ClientAccount account = (ClientAccount) accToRequirement.getValue();
		if (selection == account) {
			return accounts(context, account);
		}
		return null;
	}

	private Result accountFromRequirement(Context context, ResultList list,
			Object selection) {
		Object accountObj = context.getSelection(ACCOUNT_FROM);
		Requirement accFromRequirement = get(ACCOUNT_FROM);
		ClientAccount account = (ClientAccount) accFromRequirement.getValue();

		if (selection == account) {
			return accounts(context, account);

		}
		return null;
	}

	private Result accounts(Context context, ClientAccount account) {
		List<ClientAccount> accounts = getAccounts();
		Result result = context.makeResult();
		result.add("Select account");

		ResultList list = new ResultList("accountslist");
		int num = 0;
		if (account != null) {
			list.add(createAccountRecord(account));
			num++;
		}
		for (ClientAccount acc : accounts) {
			if (acc != account) {
				list.add(createAccountRecord(acc));
				num++;
			}
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		result.add(list);
		return result;
	}

}
