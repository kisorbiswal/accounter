package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public abstract class AbstractTableRequirement<T extends IAccounterCore>
		extends AbstractRequirement<T> {
	private static final String PROCESS_ATR = "processAttr";
	private List<Requirement> requirements;
	private boolean isCreatable;
	protected static final String DUE_DATE = "BillDueDate";

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
			Result res = edit(currentValue, context);
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
		if (!isDone()
				|| (actionName != null && actionName.equals(getAddMoreString()))) {
			resetRequirementsValues();
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
			makeResult.add(getEmptyString() + getEnterString());
		}
		if (isCreatable || values.size() < getList().size()) {
			Record addMoreRecord = new Record(getAddMoreString());
			addMoreRecord.add("", getAddMoreString());
			actions.add(addMoreRecord);
		}
		return null;
	}

	protected abstract String getEmptyString();

	private Result showlist(Context context) {
		List<T> oldValues = getValue();
		if (isCreatable) {
			T newObj = getNewObject();
			oldValues.add(newObj);
			return edit(newObj, context);
		}

		ResultList list = new ResultList(getName() + "items");
		List<T> allValues = getList();

		for (T t : allValues) {
			if (!oldValues.contains(t)) {
				list.add(createFullRecord(t));
			}
		}
		Result result = new Result();
		if (list.size() > 0) {
			result.add(getEnterString());
			result.add(list);
		} else {
			result.add(getEmptyString());
		}

		return result;
	}

	protected T currentValue;

	private Result edit(T obj, Context context) {
		currentValue = obj;
		String process = (String) context.getAttribute(PROCESS_ATR);
		if (process == null || !process.equals(getName())) {
			resetRequirementsValues();
			setRequirementsDefaultValues(obj);
		}
		context.setAttribute(PROCESS_ATR, getName());
		Object selection = context.getSelection(ACTIONS);
		if (selection == ActionNames.FINISH) {
			context.removeAttribute(PROCESS_ATR);
			getRequirementsValues(obj);
			return null;
		}
		if (selection == ActionNames.DELETE) {
			List<T> values = getValue();
			values.remove(obj);
			context.removeAttribute(PROCESS_ATR);
			return null;
		}

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

	public void resetRequirementsValues() {
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
