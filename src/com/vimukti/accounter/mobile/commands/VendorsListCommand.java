package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VendorsListCommand extends AbstractTransactionCommand {

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
		List<Vendor> vendors = getVendors(isActive);
		for (Vendor vendor : vendors) {
			vendorsResult.add(createPayeeRecord(vendor));
			num++;
			if (num == VENDORS_TO_SHOW) {
				break;
			}
		}
		result.add(vendorsResult);
		return result;

	}

	

	private Result isActiveRequirement(Context context, Object selection) {
		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}
		String activeString = "";
		if (isActive) {
			activeString = "This vendors is Active";
		} else {
			activeString = "This vendor is InActive";
		}
		return null;
	}

}
