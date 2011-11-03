package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientUnit;

public abstract class MeasurementUnitRequirement extends
		AbstractTableRequirement<ClientUnit> {

	private static final String UNIT_TYPE = "unitType";
	private static final String UNIT_FACTOR = "unitFactor";
	private static final String IS_DEFAULT = "isdefault";

	public MeasurementUnitRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
		setDefaultValue(new ArrayList<ClientUnit>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(UNIT_TYPE, getMessages().pleaseEnter(
				getConstants().unitName()), getConstants().unitName(), false,
				true));

		list.add(new AmountRequirement(UNIT_FACTOR, getMessages().pleaseEnter(
				getConstants().factor()), getConstants().factor(), true, true));

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
		return getMessages().youDontHaveAny(getConstants().units());
	}

	@Override
	protected void getRequirementsValues(ClientUnit obj) {
		String unitName = get(UNIT_TYPE).getValue();
		obj.setType(unitName);
		double factor = get(UNIT_FACTOR).getValue();
		obj.setFactor(factor);
		Boolean isDefault = get(IS_DEFAULT).getValue();
		obj.setDefault(isDefault);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientUnit obj) {
		get(UNIT_TYPE).setDefaultValue(obj.getName());
		get(UNIT_FACTOR).setDefaultValue(obj.getFactor());
		get(IS_DEFAULT).setDefaultValue(obj.isDefault());
	}

	@Override
	protected ClientUnit getNewObject() {
		return new ClientUnit();
	}

	@Override
	protected Record createFullRecord(ClientUnit t) {
		Record record = new Record(t);
		record.add("", getConstants().unitName());
		record.add("", t.getName());
		record.add("", getConstants().factor());
		record.add("", t.getFactor());
		record.add("", getConstants().isDefault());
		record.add("", t.isDefault());
		return record;
	}

	@Override
	protected Record createRecord(ClientUnit t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().units());
	}

}
