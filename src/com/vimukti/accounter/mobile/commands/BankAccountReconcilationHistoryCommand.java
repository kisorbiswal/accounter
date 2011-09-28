package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class BankAccountReconcilationHistoryCommand extends
		AbstractTransactionCommand {
	private static final int TYPE_BANK = 2;
	private static final int RECONCILATION_HISTORY_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("BankAccount", false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = bankAccountsRequirement(context);
		if (result == null) {
			// TODO
		}
		result = createreconcilationHistory(context);
		if (result == null) {

		}

		return null;

	}

	private Result createreconcilationHistory(Context context) {
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

		Result result = reconcilationList(context);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result reconcilationList(Context context) {
		Result result = context.makeResult();
		ResultList recomcilationList = new ResultList(
				"reconcilationHistorylist");
		result.add("reconcilationHistorylist");
		int num = 0;
		BankAccount bankAccount = context.getSelection("BankAccount");
		List<Reconciliation> reconciliations = getReconciliationsByBankAccountID(bankAccount
				.getID());
		for (Reconciliation reconciliation : reconciliations) {
			recomcilationList.add(createreReconcilationRecord(reconciliation));
			num++;
			if (num == RECONCILATION_HISTORY_TO_SHOW) {
				break;
			}
		}
		int size = recomcilationList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Account");
		}
		result.add(message.toString());
		result.add(recomcilationList);

		return result;

	}

	private Record createreReconcilationRecord(Reconciliation reconciliation) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Reconciliation> getReconciliationsByBankAccountID(long accountID) {
		// TODO
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
