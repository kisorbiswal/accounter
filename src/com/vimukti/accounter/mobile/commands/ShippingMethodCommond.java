package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class ShippingMethodCommond extends AbstractCommand {
	private static final String SHIPPING_METHOD = "Shipping Mehtod";
	private static final String DESCRIPTION = "description";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(SHIPPING_METHOD, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = shppingMethodRequirment(context);
		if (result != null) {
			return result;
		}
		result = createOptionalReq(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private Result createOptionalReq(Context context) {
		ResultList list = new ResultList(DESCRIPTION);
		Object selection = context.getSelection(DESCRIPTION);
		Result stringOptionalRequirement = stringOptionalRequirement(context,
				list, selection, "description", DESCRIPTION);
		return stringOptionalRequirement;
	}

	private void completeProcess(Context context) {
		ShippingMethod shippingMethod = new ShippingMethod();
		Requirement shippingRequirement = get(SHIPPING_METHOD);
		String shippingMethodType = shippingRequirement.getValue();
		shippingMethod.setName(shippingMethodType);
		create(shippingMethod, context);
	}

	private Result shppingMethodRequirment(Context context) {
		Requirement requirement = get(SHIPPING_METHOD);
		String customerName = context.getSelection(TEXT);
		if (!requirement.isDone()) {
			if (customerName != null) {
				requirement.setValue(customerName);
			} else {
				return text(context, "Please enter the  Shipping Method", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(SHIPPING_METHOD)) {
			requirement.setValue(input);
		}
		return null;
	}

}
