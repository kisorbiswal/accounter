package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCustomer;

public abstract class CustomerRequirement extends
		AbstractRequirement<ClientCustomer> {

	private static final int CUSTOMERS_TO_SHOW = 5;
	private ChangeListner<ClientCustomer> listner;

	public CustomerRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional2,
			boolean isAllowFromContext2, ChangeListner<ClientCustomer> listner) {
		super(requirementName, displayString, recordName, isOptional2,
				isAllowFromContext2);
		this.listner = listner;
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Object selection = context.getSelection(VALUES);

		Object customerObj = context.getSelection(getName());
		if (customerObj instanceof ActionNames) {
			customerObj = null;
			selection = getName();
		}

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(getName())) {
			if (customerObj != null) {
				setValue(customerObj);
				listner.onSelection((ClientCustomer) customerObj);
			}
		}

		if (!isDone()) {
			selection = getName();
		}

		if (selection != null && selection.equals(getName())) {
			return newCustomers(context);
		}

		ClientCustomer value = getValue();
		Record customerRecord = new Record(getName());
		customerRecord.add("", getRecordName());
		customerRecord.add("", getDisplayValue(value));
		list.add(customerRecord);

		return null;
	}

	public Result newCustomers(Context context) {
		Result result = context.makeResult();

		String name = null;
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute.equals(getName())) {
			name = context.getString();
		}

		Object selection = context.getSelection(ACTIONS);
		List<ClientCustomer> customers = null;
		if (selection == ActionNames.ALL) {
			customers = getCustomers(context);
			result.add("All Customers");
		} else if (name != null) {
			customers = getCustomers(context, name);
			if (customers.size() != 0) {
				result.add("Found " + customers.size() + " record(s)");
			}
		}

		if (customers.size() != 0) {
			return displayRecords(context, customers, result, CUSTOMERS_TO_SHOW);
		}

		if (name != null) {
			result.add("Did not get any records with '" + name + "'.");
		}

		result.add("Please Enter Customer name or number.");
		ResultList actions = new ResultList(ACTIONS);
		Record record = new Record(ActionNames.ALL);
		record.add("", "Show All Customers");

		actions.add(record);
		result.add(actions);

		context.setAttribute(INPUT_ATTR, getName());
		return result;
	}

	protected abstract List<ClientCustomer> getCustomers(Context context,
			String name);

	protected abstract String getDisplayValue(ClientCustomer value);

	protected abstract List<ClientCustomer> getCustomers(Context context);
}
