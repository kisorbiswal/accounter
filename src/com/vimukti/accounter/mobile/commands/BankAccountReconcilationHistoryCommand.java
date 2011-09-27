package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class BankAccountReconcilationHistoryCommand extends
		AbstractTransactionCommand {

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

	private Result bankAccountsRequirement(Context context) {
		Requirement customerReq = get("BankAccount");
		Customer customer = context.getSelection("BankAccount");
		if (customer != null) {
			customerReq.setValue(customer);
		}
		if (!customerReq.isDone()) {
			return reconcilationBankAccounts(context);
		}
		return null;
	}

	private Result reconcilationBankAccounts(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList("BankAccount");

		Object last = context.getLast(RequirementType.BANK_ACCOUNT);
		int num = 0;
		if (last != null) {
			customersList.add(createCustomerRecord((Customer) last));
			num++;
		}
		List<Customer> customers = context.getCompany().getCustomers();
		for (Customer customer : customers) {
			if (customer != last) {
				customersList.add(createCustomerRecord(customer));
				num++;
			}
			if (num == BANK_ACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = customersList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Customer");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(customersList);
		result.add(commandList);
		result.add("Type for Customer");
		return result;
	}

}
