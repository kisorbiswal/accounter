package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;

public class BankAccountsListsCommands extends AbstractTransactionCommand {
	private static final String VIEW_TYPE = "viewType";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VIEW_TYPE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(VIEW_TYPE);

		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(VIEW_TYPE).getValue();
		result = bankAccountsList(context, viewType);
		return result;
	}

	private Result bankAccountsList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Bank Accounts");
		ResultList bankAccountsList = new ResultList("bankAccounts");
		int num = 0;
		List<ClientBankAccount> accounts = getBankAccounts();
		for (ClientBankAccount b : accounts) {
			bankAccountsList.add(createExpenseRecord(b));
			num++;
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = bankAccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Bank Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(bankAccountsList);
		result.add(commandList);
		result.add("Type for Expense");

		return result;
	}

	private Record createExpenseRecord(ClientBankAccount account) {
		Record record = new Record(account);
		record.add("No", account.getNumber());
		record.add("Name", account.getName());
		record.add("Type", account.getType());
		record.add("Balance", ((ClientAccount) account).getCurrentBalance());
		return record;
	}

}
