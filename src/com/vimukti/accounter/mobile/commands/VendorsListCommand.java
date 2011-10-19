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
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VendorsListCommand extends AbstractTransactionCommand {
	private static final String VENDOR_TYPE = "vendorType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// list.add(new Requirement(ACTIVE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = createVendorsList(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createVendorsList(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ACTIVE:
				context.setAttribute(VENDOR_TYPE, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(VENDOR_TYPE, false);
				break;
			case ALL:
				context.setAttribute(VENDOR_TYPE, null);
				break;
			default:
				break;
			}
		}
		Result result = vendorsList(context, selection);
		return result;
	}

	private Result vendorsList(Context context, ActionNames selection) {
		Result result = context.makeResult();
		ResultList vendorsList = new ResultList("vendorssList");
		result.add("Vendors List");

		Boolean vendorType = (Boolean) context.getAttribute(VENDOR_TYPE);
		List<ClientVendor> vendors = getVendors(vendorType);

		ResultList actions = new ResultList("actions");

		List<ClientVendor> pagination = pagination(context, selection, actions,
				vendors, new ArrayList<ClientVendor>(), VALUES_TO_SHOW);

		for (ClientVendor vendor : pagination) {
			vendorsList.add(createVendorRecord(vendor));
		}

		StringBuilder message = new StringBuilder();
		if (vendorsList.size() > 0) {
			message.append("Select an Vendor");
		}

		result.add(message.toString());
		result.add(vendorsList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", "Active Vendors");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", "InActive Vendors");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Vendors");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Add Vendor");
		result.add(commandList);
		return result;

	}

}
