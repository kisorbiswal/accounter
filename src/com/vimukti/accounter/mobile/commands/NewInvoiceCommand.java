package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewInvoiceCommand extends Command {

	private static final int CUSTOMERS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("contac", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("shipTo", true, true));
		list.add(new Requirement("due", true, true));
		list.add(new Requirement("orderNo", true, true));
		list.add(new Requirement("memo", true, true));
	}

	@Override
	public Result run(Context context) {
		Requirement customerReq = get("customer");
		if (!customerReq.isDone()) {
			return customerResult(context);
		} else {
			// TODO
		}

		Requirement itemsReq = get("items");
		if (!itemsReq.isDone()) {
			// return
		}

		return null;
	}

	private Record createDateRecord(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result customerResult(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList();
		Object last = context.getLast(RequirementType.CUSTOMER);
		if (last != null) {
			customersList.add(createCustomerRecord((Customer) last));
		}
		List<Customer> customers = getCustomers(context.getSession());
		for (int i = 0; i < CUSTOMERS_TO_SHOW || i < customers.size(); i++) {
			Customer customer = customers.get(i);
			if (customer != last) {
				customersList.add(createCustomerRecord(customer));
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

	private List<Customer> getCustomers(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createCustomerRecord(Customer last) {
		// TODO Auto-generated method stub
		return null;
	}

}
