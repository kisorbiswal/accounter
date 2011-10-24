package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class NewCommand extends Command {

	@Override
	public Result run(Context context) {
		setDefaultValues();
		if (context.getAttribute("input") == null) {
			context.setAttribute("input", "");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getReadyToCreateMessage());
		ResultList list = new ResultList("values");
		ResultList actions = new ResultList("actions");

		makeResult.add(list);
		List<Requirement> allRequirements = getRequirements();
		for (Requirement req : allRequirements) {
			Result result = req.run(context, makeResult, list, actions);
			if (result != null) {
				return result;
			}
		}
		makeResult.add(actions);
		Object selection = context.getSelection("actions");
		if (selection != ActionNames.FINISH_COMMAND) {
			Record record = new Record(ActionNames.FINISH_COMMAND);
			record.add("", "Finish to create");
			actions.add(record);
			return makeResult;
		}
		Result result = onCompleteProcess(context);
		if (result != null) {
			return result;
		}
		makeResult = context.makeResult();
		makeResult.add(0, getSuccessMessage());
		markDone();
		return makeResult;
	}

	protected abstract String getReadyToCreateMessage();

	protected abstract void setDefaultValues();

	protected abstract Result onCompleteProcess(Context context);

	public abstract String getSuccessMessage();

}
