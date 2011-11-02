package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientUnit;

public class MeasurementUnitRequirement extends AbstractRequirement<ClientUnit> {



	private static final String PROCESS_ATTR = "processAttr";
	private static final String OLD_UNIT_ATTR = "oldUnit";
	private static final String UNIT_LINE_ATTR = "unitLineAttr";
	private static final String UNIT_ACTIONS = "unitActions";
	private static final String UNIT_NAME = "unitName";
	private static final String FACTOR = "factor";
	private static final String IS_DEFAULT = "isDefault";

	public MeasurementUnitRequirement(String requirementName) {
		super(requirementName, null, null, true, true);
		setDefaultValue(new ArrayList<ClientUnit>());
	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		String attribute = context.getSelection(VALUES);
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(getName())) {
				Result result = unitProcess(context);
				if (result != null) {
					return result;
				}
				attribute = getName();
			}
		}

		Object selection = context.getSelection(getName() + ACTIONS);
		if (selection == ActionNames.ADD_MORE_UNITS) {
			return unitProcess(context);
		} else if (selection == ActionNames.FINISH) {
			context.setAttribute(INPUT_ATTR, "");
			attribute = null;
		}

		ClientUnit contact = context.getSelection(getName());
		if (contact != null) {
			Result res = contact(context, contact);
			if (res != null) {
				return res;
			}
		}

		List<ClientUnit> clientUnit = getValue();
		String attr = (String) context.getAttribute(INPUT_ATTR);
		if (attr != getName()) {
			if (attribute == null || !attribute.equals(getName())) {
				Record e = new Record(getName());
				e.add("", getConstants().units());
				e.add("", clientUnit.size() + getConstants().units());
				list.add(e);
				return null;
			}
		}
		context.setAttribute(INPUT_ATTR, getName());
		Result result = new Result();

		ResultList unitsList = new ResultList(getName());
		if (clientUnit.isEmpty()) {
			addFirstMessage(context,
					getMessages().thereAreNo(getConstants().units()));
			// return contactProcess(context);
		} else {
			result.add(getMessages().all(getConstants().units()));
		}
		for (ClientUnit unit : clientUnit) {
			Record record = new Record(unit);
			record.add("", unit.toString());
			unitsList.add(record);
		}
		result.add(unitsList);
		ResultList actionsList = new ResultList(getName() + ACTIONS);
		Record e = new Record(ActionNames.ADD_MORE_UNITS);
		e.add("", getMessages().addNew(getConstants().unit()));
		actionsList.add(e);
		e = new Record(ActionNames.FINISH);
		e.add("", getConstants().finish());
		actionsList.add(e);
		result.add(actionsList);
		return result;
	}

	private Result contact(Context context, ClientUnit oldUnit) {
		context.setAttribute(PROCESS_ATTR, getName());
		context.setAttribute(OLD_UNIT_ATTR, oldUnit);

		String lineAttr = (String) context.getAttribute(UNIT_LINE_ATTR);
		if (lineAttr != null) {
			String input = context.getString();
			context.removeAttribute(UNIT_LINE_ATTR);
			if (lineAttr.equals(UNIT_NAME)) {
				if (oldUnit == null) {
					oldUnit = new ClientUnit();
					List<ClientUnit> unitName = getValue();
					unitName.add(oldUnit);
					context.setAttribute(OLD_UNIT_ATTR, oldUnit);
				}
				oldUnit.setType(input);
			} else if (lineAttr.equals(FACTOR)) {
				oldUnit.setFactor(Double.parseDouble(input));
			} 

		}

		Object selection = context.getSelection(UNIT_ACTIONS);

		if (selection == ActionNames.FINISH) {
			context.removeAttribute(PROCESS_ATTR);
			context.removeAttribute(OLD_UNIT_ATTR);
			context.removeAttribute(UNIT_LINE_ATTR);// No need
			return null;

		} else if (selection == ActionNames.DELETE_CONTACT) {
			List<ClientUnit> contacts = getValue();
			contacts.remove(oldUnit);
			context.removeAttribute(PROCESS_ATTR);
			context.removeAttribute(OLD_UNIT_ATTR);
			context.removeAttribute(UNIT_LINE_ATTR);// No need
			return null;
		} else {
			selection = context.getSelection(UNIT_LINE_ATTR);
			if (oldUnit == null) {
				selection = UNIT_NAME;
			}
			if (selection != null) {
				if (selection.equals(IS_DEFAULT)) {
					List<ClientUnit> clientContacts = getValue();
					for (ClientUnit cc : clientContacts) {
						cc.setDefault(false);
					}
					oldUnit.setDefault(!oldUnit.isDefault());
				} else if (selection == UNIT_NAME) {
					context.setAttribute(UNIT_LINE_ATTR, UNIT_NAME);
					String unitName = oldUnit != null ? oldUnit.getName()
							: null;
					return show(
							context,
							getMessages().pleaseEnter(
									getConstants().unitName()), unitName,
							unitName);
				} else if (selection == FACTOR) {
					context.setAttribute(UNIT_LINE_ATTR, FACTOR);
					Double factor = oldUnit != null ? oldUnit.getFactor()
							: null;
					return show(context,
							getMessages().pleaseEnter(getConstants().factor()),
							String.valueOf(factor), String.valueOf(factor));
				}
			}
		}

		ResultList list = new ResultList(UNIT_LINE_ATTR);
		Record record = new Record(IS_DEFAULT);
		record.add("", oldUnit.isDefault() ? getConstants().isDefault()
				: getConstants().notDefault());
		list.add(record);

		record = new Record(UNIT_NAME);
		record.add("", getConstants().unitName());
		record.add("", oldUnit.getName());
		list.add(record);

		record = new Record(FACTOR);
		record.add("", getConstants().factor());
		record.add("", oldUnit.getFactor());
		list.add(record);

		Result result = context.makeResult();
		result.add(list);

		ResultList finish = new ResultList(UNIT_ACTIONS);
		record = new Record(ActionNames.DELETE_UNIT);
		record.add("", getConstants().delete());
		finish.add(record);

		record = new Record(ActionNames.FINISH);
		record.add("", getConstants().finish());
		finish.add(record);

		result.add(finish);

		return result;
	}

	private Result unitProcess(Context context) {
		ClientUnit unit = (ClientUnit) context
				.getAttribute(OLD_UNIT_ATTR);
		return contact(context, unit);
	}


}
