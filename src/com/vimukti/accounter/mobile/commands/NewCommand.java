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
		setDefaultValues(context);
		if (getAttribute("input") == null) {
			setAttribute("input", "");
		}

		Result makeResult = context.makeResult();
		makeResult.add(getDetailsMessage());
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
			String finish = getFinishCommandString();
			if (finish != null) {
				Record record = new Record(ActionNames.FINISH_COMMAND);
				record.add("", finish);
				actions.add(record);
			}
			beforeFinishing(makeResult);
			return makeResult;
		}

		Result result = onCompleteProcess(context);
		if (result != null) {
			return result;
		}

		Result finishResult = context.makeResult();
		String success = getSuccessMessage();
		if (success != null) {
			finishResult.add(getSuccessMessage());
		}
		markDone();
		return finishResult;
	}

	protected Result onCompleteProcess(Context context) {
		return null;
	}

	public void beforeFinishing(Result makeResult) {
	}

	public String getFinishCommandString() {
		return "Finish";
	}

	protected abstract String getDetailsMessage();

	protected abstract void setDefaultValues(Context context);

	public abstract String getSuccessMessage();

}
