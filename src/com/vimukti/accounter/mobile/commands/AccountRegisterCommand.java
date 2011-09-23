package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class AccountRegisterCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("bank Account", false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = createAccounterRegisterList(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createAccounterRegisterList(Context context) {

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
		selection = context.getSelection("accountRegisterList");
		if (selection != null) {
			CommandList commandList = new CommandList();
			commandList.add("void");
		}
		Result result = accountRegister(context);

		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Show");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result accountRegister(Context context) {

		Result result = context.makeResult();
		ResultList accountsList = new ResultList("accountRegisterList");
		result.add("AccountRegister List");

		Account account = (Account) context.getLast(RequirementType.ACCOUNT);
		if (account == null) {
			CommandList commandList = new CommandList();
			commandList.add("AccountRegister List");
		}
		ArrayList<AccountRegister> accountRegisters = null;
		try {
			accountRegisters = new FinanceTool().getAccountRegister(
					new FinanceDate(0), new FinanceDate(0), account.getID(),
					account.getCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		int num = 0;
		for (AccountRegister accountRegister : accountRegisters) {
			accountsList.add(createAccountRecord(accountRegister));
			num++;
			if (num == ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		result.add(accountsList);
		return result;

	}

	/**
	 * 
	 * @param accountRegister
	 * @return
	 */
	private Record createAccountRecord(AccountRegister accountRegister) {
		Record record = new Record(accountRegister);
		record.add("Name", "Account Register");
		record.add("value", accountRegister.getType());
		return record;
	}

}
