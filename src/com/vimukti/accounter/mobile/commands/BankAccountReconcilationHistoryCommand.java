package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class BankAccountReconcilationHistoryCommand extends
		AbstractTransactionCommand {
	private static final int TYPE_BANK = 2;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("BankAccount", false, true));
		list.add(new Requirement(ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = bankAccountsRequirement(context);
		if (result == null) {
			// TODO
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result bankAccountsRequirement(Context context) {
		Requirement bankAccountReq = get("BankAccount");
		BankAccount bankAccount = context.getSelection("BankAccount");
		if (bankAccount != null) {
			bankAccountReq.setValue(bankAccount);
		}
		if (!bankAccountReq.isDone()) {
			return reconcilationBankAccounts(context);
		}

		return null;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */

	private Result reconcilationBankAccounts(Context context) {
		Result result = context.makeResult();
		ResultList bankAccountsList = new ResultList("BankAccount");

		Object last = context.getLast(RequirementType.BANK_ACCOUNT);
		int num = 0;
		if (last != null) {
			bankAccountsList.add(createBankAccoutRecord((BankAccount) last));
			num++;
		}
		ArrayList<Account> bankAccounts = getBankAccounts(new ArrayList<Account>(
				getCompany().getAccounts()));

		for (Account bankAccount : bankAccounts) {
			if (bankAccount != last) {
				bankAccountsList.add(createBankAccoutRecord(bankAccount));
				num++;
			}
			if (num == BANK_ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = bankAccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Bank account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(bankAccountsList);
		result.add(commandList);
		result.add("Type for Bank account");
		return result;
	}

	/**
	 * 
	 * @param accounts
	 * @return {@link ArrayList<Account>}
	 */
	private ArrayList<Account> getBankAccounts(ArrayList<Account> accounts) {
		ArrayList<Account> list = new ArrayList<Account>();
		for (Account account : accounts) {
			if (account.getType() == TYPE_BANK) {
				list.add(account);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param bankAccount
	 * @return
	 */
	private Record createBankAccoutRecord(Account bankAccount) {

		Record record = new Record(bankAccount);
		record.add("Name", "BankAccount");
		record.add("Value", bankAccount.getName());
		return record;

	}

}
