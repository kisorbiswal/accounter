package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class ShippingMethodListCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result result = createShippingMethodsListReq(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result createShippingMethodsListReq(Context context) {

		Object selection = context.getSelection(ACTIONS);
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

		Result result = shippingMethods(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result shippingMethods(Context context) {
		ResultList shipingResultList = new ResultList("shippingmethodsList");

		Result result = context.makeResult();

		List<ShippingMethod> shippingList = getShippingMethodList(context);
		for (ShippingMethod shippingMethod : shippingList) {
			shipingResultList.add(createShippingRecord(shippingMethod));
		}
		result.add(shipingResultList);
		CommandList commandList = new CommandList();
		commandList.add("Add a New Shipping Method");
		commandList.add("Edit a New Shipping Method");
		commandList.add("Delete a New Shipping Method");
		result.add(commandList);

		return result;
	}

	private Record createShippingRecord(ShippingMethod shippingMethod) {
		Record record = new Record(shippingMethod);
		record.add("Name", shippingMethod.getName());
		record.add("Description", shippingMethod.getDescription());
		return record;
	}

	private List<ShippingMethod> getShippingMethodList(Context context) {
		return new ArrayList<ShippingMethod>(context.getCompany()
				.getShippingMethods());
	}

}
