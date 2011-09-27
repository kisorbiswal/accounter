package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewShippingTermCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("shippingTerms", false, true));
		list.add(new Requirement("description", true, true));

	}

	@Override
	public Result run(Context context) {

		Result result = null;

		Object selection = context.getSelection(ACTIONS);
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		result = shippingTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private Result shippingTermRequirement(Context context, ResultList list,
			Object selection) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result createOptionalResult(Context context) {
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
				"description", "Enter the description");
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
		
		ShippingTerms newShippingTerm = new ShippingTerms();

		newShippingTerm.setName((String) get("name").getValue());
		newShippingTerm.setDescription((String) get("description").getValue());
		//TODO no description added in shipping Terms??		
		create(newShippingTerm, context);
	}

}
