package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewShippingMethodCommond extends AbstractCommand {
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
		Result result = null;

		Object selection = context.getSelection(ACTIONS);
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		result = shppingMethodRequirment(context, list, (String) selection);
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

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, "Enter Description");
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add(" Item is ready to create with following values.");
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);
		result.add(actions);

		return result;

	}

	private void completeProcess(Context context) {
		ShippingMethod shippingMethod = new ShippingMethod();
		Requirement shippingRequirement = get(SHIPPING_METHOD);
		String shippingMethodType = shippingRequirement.getValue();
		shippingMethod.setName(shippingMethodType);
		// TODO need to set Description is not required??
		create(shippingMethod, context);
	}

	private Result shppingMethodRequirment(Context context, ResultList list,
			String selection) {
		Requirement req = get(SHIPPING_METHOD);
		String shippingMethod = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute != null) {
			if (attribute.equals(SHIPPING_METHOD)) {
				String order = context.getSelection(NUMBER);
				if (order == null) {
					order = context.getString();
				}
				shippingMethod = order;
				req.setValue(shippingMethod);
			}
		}

		if (selection == shippingMethod) {
			context.setAttribute(INPUT_ATTR, SHIPPING_METHOD);
			return number(context, "Enter Shipping Method", shippingMethod);
		}

		Record invoiceNoRec = new Record(shippingMethod);
		invoiceNoRec.add("Name", "Invoice Number");
		invoiceNoRec.add("Value", shippingMethod);
		list.add(invoiceNoRec);
		return null;

	}

}
