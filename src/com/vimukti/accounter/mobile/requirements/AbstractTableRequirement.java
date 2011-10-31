package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractTableRequirement<T> extends
		AbstractRequirement<T> {
	private static final String PROCESS_ATR = "processAttr";
	private static final String TABLE_ROW = "tableRow";
	private List<Requirement> requirements;
	private boolean isCreatable;

	public AbstractTableRequirement(String requirementName, String enterString,
			String recordName, boolean isCreatable, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext);
		this.isCreatable = isCreatable;
		requirements = new ArrayList<Requirement>();
		addRequirement(requirements);
		setValue(new ArrayList<T>());
	}

	protected Requirement get(String name) {
		for (Requirement requirement : requirements) {
			if (requirement.getName().equals(name)) {
				return requirement;
			}
		}
		return null;
	}

	@Override
	public boolean isDone() {
		List<T> values = getValue();
		if (isOptional()) {
			return true;
		}
		return !values.isEmpty();
	}

	protected abstract void addRequirement(List<Requirement> list);

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String process = (String) context.getAttribute(PROCESS_ATR);
		if (process != null && process.equals(getName())) {
			T t = (T) context.getAttribute(TABLE_ROW);
			Result res = edit(t, context);
			if (res != null) {
				return res;
			}
		}
		List<T> values = getValue();

		T selected = context.getSelection(getName() + "items");
		if (selected != null) {
			values.add(selected);
			return edit(selected, context);
		}

		T selectedValue = context.getSelection(getName());
		if (selectedValue != null) {
			return edit(selectedValue, context);
		}
		Object actionName = context.getSelection(ACTIONS);
		if (!isDone() || actionName == getAddMoreString()) {
			return showlist(context);
		}
		if (!values.isEmpty()) {
			ResultList resultList = new ResultList(getName());
			makeResult.add(getRecordName());
			for (T t : values) {
				resultList.add(createRecord(t));
			}
			makeResult.add(resultList);
		} else {
			makeResult.add("There are no bills. " + getEnterString());
		}
		Record addMoreRecord = new Record(getAddMoreString());
		addMoreRecord.add("", getAddMoreString());
		actions.add(addMoreRecord);
		return null;
	}

	private Result showlist(Context context) {
		if (isCreatable) {
			T newObj = getNewObject();
			return edit(newObj, context);
		}

		ResultList list = new ResultList(getName() + "items");
		List<T> allValues = getList();

		List<T> oldValues = getValue();
		for (T t : allValues) {
			if (!oldValues.contains(t)) {
				list.add(createFullRecord(t));
			}
		}
		Result result = new Result();
		result.add(getEnterString());
		result.add(list);
		return result;
	}

	private Result edit(T obj, Context context) {
		context.setAttribute(PROCESS_ATR, getName());
		context.setAttribute(TABLE_ROW, obj);
		Object selection = context.getSelection(ACTIONS);
		if (selection == ActionNames.FINISH) {
			context.removeAttribute(TABLE_ROW);
			context.removeAttribute(PROCESS_ATR);
			getRequirementsValues(obj);
			return null;
		}
		if (selection == ActionNames.DELETE) {
			List<T> values = getValue();
			values.remove(obj);
			context.removeAttribute(TABLE_ROW);
			context.removeAttribute(PROCESS_ATR);
			resetRequirementsValues(obj);
			return null;
		}

		resetRequirementsValues(obj);
		setRequirementsDefaultValues(obj);
		Result makeResult = new Result();
		ResultList list = new ResultList(VALUES);
		ResultList actions = new ResultList(ACTIONS);
		for (Requirement req : requirements) {
			Result res = req.process(context, makeResult, list, actions);
			if (res != null) {
				return res;
			}
		}

		setOtherFields(list);

		Record record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		actions.add(record);

		Record record2 = new Record(ActionNames.DELETE);
		record2.add("", "Delete");
		actions.add(record2);

		makeResult.add(list);
		makeResult.add(actions);

		return makeResult;
	}

	public void resetRequirementsValues(T obj) {
		for (Requirement requirement : requirements) {
			requirement.setValue(null);
			requirement.setDefaultValue(null);
		}
	}

	public void setOtherFields(ResultList list) {

	}

	protected abstract void getRequirementsValues(T obj);

	protected abstract void setRequirementsDefaultValues(T obj);

	protected abstract T getNewObject();

	protected abstract Record createFullRecord(T t);

	protected abstract List<T> getList();

	protected abstract Record createRecord(T t);

	protected abstract String getAddMoreString();
}
