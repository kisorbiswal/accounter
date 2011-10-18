package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCustomer;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerListCommand extends AbstractTransactionCommand {

	private static final String CUSTOMER_TYPE = "customerType";

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

		Result result = createCustomerListReq(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createCustomerListReq(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ACTIVE:
				context.setAttribute(CUSTOMER_TYPE, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(CUSTOMER_TYPE, false);
				break;
			case ALL:
				context.setAttribute(CUSTOMER_TYPE, false);
				break;
			default:
				break;
			}
		}
		Result result = customersList(context, selection);
		return result;
	}

	private Result customersList(Context context, ActionNames selection) {
		Result result = context.makeResult();
		ResultList customerslist = new ResultList("customersList");
		result.add("customers List");

		Boolean accountType = (Boolean) context.getAttribute(CUSTOMER_TYPE);
		List<ClientCustomer> customers = getCustomers(accountType);

		ResultList actions = new ResultList("actions");

		List<ClientCustomer> pagination = pagination(context, selection,
				actions, customers, new ArrayList<ClientCustomer>(),
				VALUES_TO_SHOW);

		for (ClientCustomer customer : pagination) {
			customerslist.add(createCustomerRecord(customer));
		}

		StringBuilder message = new StringBuilder();
		if (customerslist.size() > 0) {
			message.append("Select an Account");
		}

		result.add(message.toString());
		result.add(customerslist);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", "Active Customers");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", "InActive Customers");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Accounts");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Add Customer");
		result.add(commandList);
		return result;

	}
}
