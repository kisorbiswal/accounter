package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewPaymentTermCommand extends AbstractTransactionCommand {

	private final static String PAYMENT_TERMS = "Payment Terms";
	private final static String DESCRIPTION = "Description";
	private final static String DUE_DAYS = "Due Days";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(PAYMENT_TERMS, false, true));
		list.add(new Requirement(DESCRIPTION, true, true));
		list.add(new Requirement(DUE_DAYS, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = paymentTermNameRequirement(context);
		if (result != null) {
			return result;
		}
		result = optionalRequirements(context);
		return result;
	}

	private Result optionalRequirements(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {

			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		ResultList list = new ResultList("values");
		selection = context.getSelection("values");

		String description = (String) get(DESCRIPTION).getValue();
		Record descriptionRecord = new Record(description);
		descriptionRecord.add("Name", DESCRIPTION);
		descriptionRecord.add("Value", description);
		list.add(descriptionRecord);

		int dueDays = get(DUE_DAYS).getValue();
		Record dueDaysRec = new Record(dueDays);
		dueDaysRec.add("Name", DUE_DAYS);
		dueDaysRec.add("Value", dueDays);
		list.add(dueDaysRec);

		Result result = context.makeResult();
		result.add("Payment Terms is ready to create with following values");
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish Payment Terms.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	private Result paymentTermNameRequirement(Context context) {
		Requirement requirement = get(PAYMENT_TERMS);
		if (!requirement.isDone()) {
			String paymentTerm = context.getSelection(TEXT);
			if (paymentTerm != null) {
				requirement.setValue(paymentTerm);
			} else {
				return text(context, "Please enter the Payment term name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(PAYMENT_TERMS)) {
			requirement.setValue(input);
		}
		return null;
	}
}
