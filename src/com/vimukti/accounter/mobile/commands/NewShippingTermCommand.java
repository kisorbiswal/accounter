package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;

public class NewShippingTermCommand extends AbstractTransactionCommand {
	protected static final String SHIPPING_TERMNAME = "shippingTermsName";
	protected static final String DESCRIPTION = "description";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(SHIPPING_TERMNAME, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));

	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().shippingTerm()));

		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		Result result = nameRequirement(context, list, SHIPPING_TERMNAME,
				getConstants().shippingTerm(),
				getMessages().pleaseEnter(getConstants().shippingTerm()));
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		markDone();
		return completeProcess(context);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

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

		Result result = stringOptionalRequirement(context, list, selection,
				DESCRIPTION, getConstants().description(), getMessages()
						.pleaseEnter(getConstants().description()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().shippingTerm()));
		actions.add(finish);

		return makeResult;
	}

	private Result completeProcess(Context context) {

		ClientShippingTerms newShippingTerm = new ClientShippingTerms();
		newShippingTerm.setName((String) get(SHIPPING_TERMNAME).getValue());
		newShippingTerm.setDescription((String) get(DESCRIPTION).getValue());
		create(newShippingTerm, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().shippingTerm()));
		return result;

	}

}
