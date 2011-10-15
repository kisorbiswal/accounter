package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VendorsListCommand extends AbstractTransactionCommand {

	private static final String ACTIVE = "active";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACTIVE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createVendorsList(context);
		return result;
	}

	private Result createVendorsList(Context context) {
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
		result = vendorsList(context, isActive);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result vendorsList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList vendorsResult = new ResultList("vendors");
		result.add("Vendors List");
		int num = 0;
		List<ClientVendor> vendors = null;// getVendors(isActive,
		// context.getCompany());
		for (ClientVendor vendor : vendors) {
			vendorsResult.add(createPayeeRecord(vendor));
			num++;
			if (num == VENDORS_TO_SHOW) {
				break;
			}
		}
		int size = vendorsResult.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Vendor");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(vendorsResult);
		result.add(commandList);
		result.add("Type for Vendor");

		return result;

	}

}
