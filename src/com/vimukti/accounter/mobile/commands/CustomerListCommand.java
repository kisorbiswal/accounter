package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerListCommand extends AbstractTransactionCommand {

	private static final String ACTIVE = "active";

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

		Result result = createCustomerListReq(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createCustomerListReq(Context context) {
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
		result = customersList(context, isActive);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result customersList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList customerResult = new ResultList("vendors");
		result.add("customers List");
		int num = 0;
		List<Customer> customers = getCustomers(context.getCompany(), isActive);
		for (Customer customer : customers) {
			customerResult.add(createPayeeRecord(customer));
			num++;
			if (num == CUSTOMERS_TO_SHOW) {
				break;
			}
		}
		int size = customerResult.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Customer");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(customerResult);
		result.add(commandList);
		result.add("Enter for Customer");

		return result;

	}

}
