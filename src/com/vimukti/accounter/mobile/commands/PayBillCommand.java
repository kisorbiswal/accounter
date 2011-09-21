package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class PayBillCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VENDOR, false, true));
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(PAYMENT_MENTHOD, false, true));
		list.add(new Requirement(DATE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		result = vendorRequirement(context);
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
			selection = context.getSelection("values");
			ResultList list = new ResultList("values");

			Requirement vendorReq = get(VENDOR);
			Vendor vendor = (Vendor) vendorReq.getValue();
			Record vendorRecord = new Record(vendor);
			vendorRecord.add("Name", VENDOR);
			vendorRecord.add("Value", vendor.getName());

			list.add(vendorRecord);

			Result result = payFromRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = paymentMethodRequirement(context);

		}
		return null;
	}

}
