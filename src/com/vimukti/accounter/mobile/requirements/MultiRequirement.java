package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class MultiRequirement<T> extends AbstractRequirement<T> {

	private List<Requirement> requirements;
	private boolean isDone;

	public MultiRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		requirements = new ArrayList<Requirement>();
		addRequirement(requirements);
		setDefaultValues();
		isDone = isOptional;
	}

	protected Requirement getRequirement(String name) {
		for (Requirement requirement : requirements) {
			if (requirement.getName().equals(name)) {
				return requirement;
			}
		}
		return null;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	protected abstract void setDefaultValues();

	protected abstract void addRequirement(List<Requirement> list);

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String process = (String) context.getAttribute(PROCESS_ATR);
		if (process != null && process.equals(getName())) {
			Result res = edit(context);
			if (res != null) {
				return res;
			}
		}

		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(getName())) {
			Result result = edit(context);
			if (result != null) {
				return result;
			}
			context.setAttribute(INPUT_ATTR, "");
		}

		Object selection = context.getSelection(VALUES);
		if (!isDone() || selection != null && selection.equals(getName())) {
			if (isEditable()) {
				addFirstMessage(context, getRecordName());
				Result result = edit(context);
				if (result != null) {
					return result;
				}
			} else {
				addFirstMessage(context,
						getMessages().youCantEdit(getRecordName()));
			}
		}

		Record record = new Record(getName());
		record.add(getRecordName(), getDisplayValue());
		list.add(record);
		return null;
	}

	private Result edit(Context context) {
		context.setAttribute(PROCESS_ATR, getName());
		Result result = new Result();
		ResultList list = new ResultList("values");
		list.setTitle(getRecordName());
		result.add(list);
		ResultList actions = new ResultList("actions");
		for (Requirement req : requirements) {
			Result process = req.process(context, result, list, actions);
			if (process != null) {
				return process;
			}
		}
		Record record = new Record(ActionNames.FINISH);
		record.add("Finish");
		actions.add(record);
		result.add(actions);
		Object selection = context.getSelection("actions");
		if (selection == ActionNames.FINISH) {
			Result finish = onFinish(context);
			if (finish != null) {
				return finish;
			}
			isDone = true;
			context.removeAttribute(PROCESS_ATR);
			return null;
		}
		context.removeSelection("actions");
		return result;
	}

	protected Result onFinish(Context context) {
		return null;
	}

	protected abstract String getDisplayValue();

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}

}
