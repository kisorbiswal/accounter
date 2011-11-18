package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;

public abstract class MeasurementUnitRequirement extends
		AbstractTableRequirement<Unit> {

	private static final String UNIT_TYPE = "unitType";
	private static final String UNIT_FACTOR = "unitFactor";
	private static final String IS_DEFAULT = "isdefault";

	public MeasurementUnitRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
		setDefaultValue(new ArrayList<Unit>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(UNIT_TYPE, getMessages().pleaseEnter(
				getMessages().unitName()), getMessages().unitName(), false,
				true));

		list.add(new AmountRequirement(UNIT_FACTOR, getMessages().pleaseEnter(
				getMessages().factor()), getMessages().factor(), true, true));

		list.add(new BooleanRequirement(IS_DEFAULT, true) {

			@Override
			protected String getTrueString() {
				return "Unit default";
			}

			@Override
			protected String getFalseString() {
				return "Unit not default";
			}
		});
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().units());
	}

	@Override
	protected void getRequirementsValues(Unit obj) {
		String unitName = get(UNIT_TYPE).getValue();
		obj.setType(unitName);
		double factor = get(UNIT_FACTOR).getValue();
		obj.setFactor(factor);
		Boolean isDefault = get(IS_DEFAULT).getValue();
		obj.setDefault(isDefault);
	}

	@Override
	protected void setRequirementsDefaultValues(Unit obj) {
		get(UNIT_TYPE).setDefaultValue(obj.getType());
		get(UNIT_FACTOR).setDefaultValue(obj.getFactor());
		get(IS_DEFAULT).setDefaultValue(obj.isDefault());
	}

	@Override
	protected Unit getNewObject() {
		return new Unit();
	}

	@Override
	protected Record createFullRecord(Unit t) {
		Record record = new Record(t);
		record.add("", getMessages().unitName());
		record.add("", t.getType());
		record.add("", getMessages().factor());
		record.add("", t.getFactor());
		record.add("", getMessages().isDefault());
		record.add("", t.isDefault());
		return record;
	}

	@Override
	protected Record createRecord(Unit t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<Unit> values = getValue();
		return values.isEmpty() ? "Add Units" : getMessages().addMore(
				getMessages().units());
	}

}
