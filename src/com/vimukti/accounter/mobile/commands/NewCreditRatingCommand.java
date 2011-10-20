package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCreditRating;

public class NewCreditRatingCommand extends AbstractTransactionCommand {

	private static final String CREDIT_RATING_NAME = "CreditRationgName";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CREDIT_RATING_NAME, false, true));
	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();
		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().creditRating()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, CREDIT_RATING_NAME,
				getMessages().pleaseEnter(getConstants().creditRatingName()));
		if (result != null) {
			return result;
		}
		return completeProcess(context);
	}

	private Result completeProcess(Context context) {
		ClientCreditRating creditRating = new ClientCreditRating();
		creditRating.setName(get(CREDIT_RATING_NAME).getValue().toString());
		create(creditRating, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(
				getConstants().creditRating()));
		return result;
	}
}
