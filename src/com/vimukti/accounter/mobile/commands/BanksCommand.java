package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Bank;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class BanksCommand extends AbstractCommand {

	protected static final String ACTIVE = "isActive";
	private static final int BANKS_TO_SHOW = 5;

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
		Result result = createBanksListReq(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createBanksListReq(Context context) {
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
		result = banksList(context, isActive);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result banksList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList banksResult = new ResultList("banks");
		result.add("Banks List");
		int num = 0;
		List<Bank> banks = getBanks(context.getCompany(), isActive);
		for (Bank bank : banks) {
			banksResult.add(createBankRecord(bank));
			num++;
			if (num == BANKS_TO_SHOW) {
				break;
			}
		}
		int size = banksResult.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Bank");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(banksResult);
		result.add(commandList);
		result.add("Enter for Bank");

		return result;

	}

	private Record createBankRecord(Bank bank) {
		Record record = new Record(bank);
		record.add("name", bank.getName());
		return record;

	}

	private List<Bank> getBanks(Company company, Boolean isActive) {
		return company.getBanks();
	}

}
