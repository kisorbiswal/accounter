package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public class NewShippingMethodCommond extends AbstractCommand {
	private static final String SHIPPING_METHOD_NAME = "Shipping Mehtod";
	private static final String DESCRIPTION = "description";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(SHIPPING_METHOD_NAME, false, true));
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

		result = nameRequirement(
				context,
				list,
				SHIPPING_METHOD_NAME,
				getConstants().shippingMethod(),
				getMessages().pleaseEnter(
						getConstants().shippingMethod() + " "
								+ getConstants().name()));
		if (result != null) {
			return result;
		}

		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		return createShippingMethodObject(context);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				if (validate(context)) {
					makeResult
							.add(getConstants().shippingMethodAlreadyExists());
				} else {
					return null;
				}
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, getConstants().description(), getMessages()
						.pleaseEnter(getConstants().description()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().shippingMethod()));
		actions.add(finish);

		return makeResult;
	}

	private boolean validate(Context context) {
		Session session = context.getHibernateSession();
		String name = get(SHIPPING_METHOD_NAME).getValue();
		Query query = session.getNamedQuery("getShippingmethod.by.Name")
				.setString("name", name)
				.setEntity("company", context.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	private void setDefaultValues() {
		get(DESCRIPTION).setDefaultValue("");

	}

	private Result createShippingMethodObject(Context context) {
		ClientShippingMethod method = new ClientShippingMethod();
		String name = get(SHIPPING_METHOD_NAME).getValue();
		method.setName(name);
		String desc = get(DESCRIPTION).getValue();
		method.setDescription(desc);
		Result result;

		create(method, context);
		result = new Result(getMessages().createSuccessfully(
				getConstants().shippingMethod()));
		return result;
	}

}
