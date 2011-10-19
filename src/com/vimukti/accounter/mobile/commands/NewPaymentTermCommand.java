package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;

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

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;

		// Preparing result
		Result makeResult = context.makeResult();
		makeResult
				.add(" Payment Term is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, PAYMENT_TERMS,
				"Please Enter Payment Term ");
		if (result != null) {
			return result;
		}

		setDefaultValues();

		result = optionalRequirements(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return completeProcess(context);
	}

	private Result completeProcess(Context context) {

		ClientPaymentTerms paymentTerms = new ClientPaymentTerms();

		String paymnetTermName = get(PAYMENT_TERMS).getValue();
		paymentTerms.setName(paymnetTermName);

		String description = get(DESCRIPTION).getValue();
		paymentTerms.setDescription(description);

		Integer dueDays = Integer.parseInt((String) get(DUE_DAYS).getValue());
		paymentTerms.setDueDays(dueDays);

		create(paymentTerms, context);

		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().paymentTerms()));

		return result;
	}

	private void setDefaultValues() {

		get(DESCRIPTION).setDefaultValue("");
		get(DUE_DAYS).setDefaultValue("0");

	}

	private Result optionalRequirements(Context context, ResultList list,
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
				DESCRIPTION, "Please Enter Description");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, DUE_DAYS,
				"Please enter Due days");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish Payment Terms.");
		actions.add(finish);

		return makeResult;
	}

}
