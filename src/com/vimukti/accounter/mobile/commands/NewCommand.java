package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class NewCommand extends Command {

	@SuppressWarnings("unchecked")
	@Override
	public Result run(Context context) {
		context.setAttribute("firstMessage", new ArrayList<String>());
		setDefaultValues(context);
		Result makeResult = context.makeResult();
		if (getAttribute("input") == null) {
			String welcomeMessage = getWelcomeMessage();
			if (welcomeMessage != null) {
				((List<String>) context.getAttribute("firstMessage"))
						.add(welcomeMessage);
			}
			setAttribute("input", "");
		}

		makeResult.add(getDetailsMessage());
		ResultList list = new ResultList("values");
		ResultList actions = new ResultList("actions");

		makeResult.add(list);
		List<Requirement> allRequirements = getRequirements();
		for (Requirement req : allRequirements) {
			Result result = req.process(context, makeResult, list, actions);
			if (result != null) {
				List<String> first = (List<String>) context
						.getAttribute("firstMessage");
				for (String f : first) {
					result.add(0, f);
				}
				return result;
			}
		}
		makeResult.add(actions);
		String finish = getFinishCommandString();
		if (finish != null) {
			Record record = new Record(ActionNames.FINISH_COMMAND);
			record.add("", finish);
			actions.add(record);
		}
		Object selection = context.getSelection("actions");
		if (selection != ActionNames.FINISH_COMMAND) {
			beforeFinishing(context, makeResult);
			List<String> first = (List<String>) context
					.getAttribute("firstMessage");
			for (String f : first) {
				makeResult.add(0, f);
			}
			return makeResult;
		}

		Result result = onCompleteProcess(context);
		if (result != null) {
			List<Object> resultParts = makeResult.getResultParts();
			for (Object object : result.getResultParts()) {
				resultParts.add(0, object);
			}
			return makeResult;
		}

		Result finishResult = context.makeResult();
		String success = getSuccessMessage();
		if (success != null) {
			finishResult.add(success);
		}
		markDone();
		return finishResult;
	}

	protected abstract String getWelcomeMessage();

	protected Result onCompleteProcess(Context context) {
		return null;
	}

	public void beforeFinishing(Context context, Result makeResult) {
	}

	public String getFinishCommandString() {
		return "Finish";
	}

	protected abstract String getDetailsMessage();

	protected abstract void setDefaultValues(Context context);

	public abstract String getSuccessMessage();

}
