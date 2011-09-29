package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class ShippingMethodListCommand extends AbstractTransactionCommand {

	private static final int SHIPPINGMETHODS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public Result run(Context context) {
		Result result = createShippingMethodsListReq(context);
		if (result == null) {
			// TODO
		}
		return null;
	}

	private Result createShippingMethodsListReq(Context context) {

		Result result = shippingMethods(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result shippingMethods(Context context) {
		ResultList shipingResultList = new ResultList("locationList");

		Result result = context.makeResult();
		List<ShippingMethod> shippingList = getShippingMethodList(context);
		int record = 0;
		for (ShippingMethod shippingMethod : shippingList) {
			shipingResultList.add(createShippingRecord(shippingMethod));
			record++;
			if (record == SHIPPINGMETHODS_TO_SHOW) {
				break;
			}
		}
		result.add(shipingResultList);
		CommandList commandList = new CommandList();
		commandList.add("Create");
		// commandList.add("Remove");
		result.add(commandList);

		return result;
	}

	private Record createShippingRecord(ShippingMethod shippingMethod) {
		Record record = new Record(shippingMethod);
		record.add("Name", "shippingMethod");
		record.add("value", shippingMethod.getName());
		return record;
	}

	private List<ShippingMethod> getShippingMethodList(Context context) {
		return new ArrayList<ShippingMethod>(context.getCompany()
				.getShippingMethods());
	}

}
