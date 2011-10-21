package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CreditRatingListCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {

		Result result = optionalRequirements(context);
		return result;
	}

	private Result optionalRequirements(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ALL:
				return null;
			default:
				break;
			}
		}
		Result result = getCreditRatingResult(context, selection);

		return result;
	}

	private Result getCreditRatingResult(Context context, ActionNames selection) {

		ResultList resultList = new ResultList("creditRatingList");

		Result result = context.makeResult();
		List<CreditRating> creditRatingList = getCreditRatingList(context);

		ResultList actions = new ResultList(ACTIONS);
		List<CreditRating> pagination = pagination(context, selection, actions,
				creditRatingList, new ArrayList<CreditRating>(), VALUES_TO_SHOW);

		for (CreditRating creditRating : pagination) {
			resultList.add(createCreditRatingRecord(creditRating));
		}

		result.add(resultList);

		Record inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().creditRating());

		result.add(commandList);

		return result;
	}

	private Record createCreditRatingRecord(CreditRating creditRating) {
		Record record = new Record(creditRating);
		record.add("value", creditRating.getName());
		return record;
	}

	private List<CreditRating> getCreditRatingList(Context context) {
		return new ArrayList<CreditRating>(context.getCompany()
				.getCreditRatings());
	}

}
