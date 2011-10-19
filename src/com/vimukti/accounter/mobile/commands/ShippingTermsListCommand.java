package com.vimukti.accounter.mobile.commands;

import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class ShippingTermsListCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		return null;
	}

	protected void addRequirements(List<Requirement> list) {
	}

	@Override
	public Result run(Context context) {

		Object selection = context.getSelection(INPUT_ATTR);

		if (selection != null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result result = createOptionalRequirement(context);
		return result;
	}

	private Result createOptionalRequirement(Context context) {

		Result result = context.makeResult();
		ResultList resultList = new ResultList("shippingTermsList");

		Object selection = context.getSelection("values");
		ActionNames actionNames;
		if (selection != null) {
			actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				markDone();
				return null;

			default:
				break;
			}
		}

		Set<ShippingTerms> list = getShippingTermsList(context);

		for (ShippingTerms shipTerms : list) {
			resultList.add(createShippingTerms(shipTerms));
		}

		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add("Add a Shipping Term");
		commandList.add("Edit a Shipping Term");
		commandList.add("Delete a Shipping Term");
		result.add(commandList);

		return result;
	}

	private Record createShippingTerms(ShippingTerms shipTerms) {
		Record record = new Record(shipTerms);
		record.add("Name", shipTerms.getName());
		record.add("Description", shipTerms.getDescription());
		return record;
	}

	private Set<ShippingTerms> getShippingTermsList(Context context) {
		return context.getCompany().getShippingTerms();
	}

}
