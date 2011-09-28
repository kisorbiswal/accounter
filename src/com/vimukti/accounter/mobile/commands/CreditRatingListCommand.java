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

	private static final int RECORDS_TO_SHOW = 5;

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
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		Result result = getCreditRatingResult(context);

		return result;
	}

	private Result getCreditRatingResult(Context context) {

		ResultList resultList = new ResultList("creditRatingList");

		Result result = context.makeResult();
		List<CreditRating> creditRatingList = getCreditRatingList(context);
		int record = 0;
		for (CreditRating creditRating : creditRatingList) {
			resultList.add(createCreditRatingRecord(creditRating));
			record++;
			if (record == RECORDS_TO_SHOW) {
				break;
			}
		}

		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add("Create");

		commandList.add("Edit");

		commandList.add("Remove");

		result.add(commandList);

		return result;
	}

	private Record createCreditRatingRecord(CreditRating creditRating) {
		Record record = new Record(creditRating);
		record.add("Name", "Name");
		record.add("value", creditRating.getName());
		return record;
	}

	private List<CreditRating> getCreditRatingList(Context context) {
		return new ArrayList<CreditRating>(context.getCompany()
				.getCreditRatings());
	}

}
