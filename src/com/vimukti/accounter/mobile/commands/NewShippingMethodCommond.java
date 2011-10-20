package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public class NewShippingMethodCommond extends AbstractCommand {
	private static final String SHIPPING_METHOD = "Shipping Mehtod";
	private static final String DESCRIPTION = "description";

	@Override
	public String getId() {
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

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().shippingMethod()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, SHIPPING_METHOD, getMessages()
				.pleaseEnter(
						getConstants().shippingMethod() + " "
								+ getConstants().name()), getConstants()
				.shippingMethod());
		if (result != null) {
			return result;
		}

		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		createShippingMethodObject(context);
		return result;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, getMessages().pleaseEnter(getConstants().description()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().createSuccessfully(getConstants().shippingMethod()));
		actions.add(finish);

		return makeResult;
	}

	private void setDefaultValues() {
		get(DESCRIPTION).setDefaultValue("");

	}

	private void createShippingMethodObject(Context context) {
		ClientShippingMethod method = new ClientShippingMethod();
		String name = get(SHIPPING_METHOD).getValue();
		method.setName(name);
		String desc = get(DESCRIPTION).getValue();
		method.setDescription(desc);
		create(method, context);
	}

}
