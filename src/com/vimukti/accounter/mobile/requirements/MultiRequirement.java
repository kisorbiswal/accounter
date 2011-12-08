package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class MultiRequirement<T> extends AbstractRequirement<T> {

	private List<Requirement> requirements;
	private T object;

	public MultiRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		requirements = new ArrayList<Requirement>();
		addRequirement(requirements);
	}

	protected abstract void addRequirement(List<Requirement> list);

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		Result result = new Result();
		ResultList list2 = new ResultList("");
		ResultList actions2 = new ResultList("");
		for (Requirement req : requirements) {
			Result process = req.process(context, result, list2, actions2);
			if (process != null) {
				return process;
			}
		}
		Result finish = finish(context, makeResult, list, actions);
		if (finish == null) {
			object = getObject();
		}
		return finish;
	}

	public void setObject(T object) {
		this.object = object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		setObject((T) value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getValue() {
		return object;
	}

	protected abstract T getObject();

	protected abstract Result finish(Context context, Result makeResult,
			ResultList list, ResultList actions);

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_NONE);
	}

}
