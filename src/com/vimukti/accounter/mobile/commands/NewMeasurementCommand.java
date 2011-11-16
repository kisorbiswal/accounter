package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.MeasurementUnitRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;

public class NewMeasurementCommand extends NewAbstractCommand {

	private static final String MEASUREMENT_NAME = "Measurement Name";
	private static final String MEASUREMENT_DESCRIPTION = "Measurement Description";
	private static final String UNIT = "Unit";

	private ClientMeasurement measurement;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "";
			}
			ClientMeasurement clientmeasurement = CommandUtils.getMeasurement(
					context.getCompany(), string);
			if (clientmeasurement == null) {
				return " " + string;
			}
			measurement = clientmeasurement;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(MEASUREMENT_NAME).setValue(string);
			}
			measurement = new ClientMeasurement();
		}
		return null;
	}

	private void setValues() {
		get(MEASUREMENT_NAME).setValue(measurement.getName());
		get(MEASUREMENT_DESCRIPTION).setValue(measurement.getDesctiption());
		List<Unit> units = new ArrayList<Unit>();
		List<ClientUnit> clientUnits = measurement.getUnits();
		for (ClientUnit unit : clientUnits) {
			units.add(new Unit(unit.getType(), unit.getFactor()));
		}
		get(UNIT).setValue(units);
	}

	@Override
	protected String getWelcomeMessage() {
		if (measurement.getID() == 0) {
			return "Create Measurement command is activated.";
		}
		return "Update Measuremnt(" + measurement.getName()
				+ ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		if (measurement.getID() == 0) {
			return "Measurement is ready to create with following details.";
		} else {
			return "Measurement is ready to update with following details.";
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (measurement.getID() == 0) {
			return "Measurement is created succesfully.";
		} else {
			return "Measurement is updated successfully.";
		}
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(MEASUREMENT_NAME,
				"Please Enter Measurement Name", "Name", false, true));

		list.add(new StringRequirement(MEASUREMENT_DESCRIPTION,
				"Please Enter Measurement Description", "Description", true,
				true));

		list.add(new MeasurementUnitRequirement(UNIT, getMessages()
				.pleaseSelect(getConstants().unit()), getConstants().unit(),
				true, false, true) {

			@Override
			protected List<Unit> getList() {
				return null;
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		measurement = new ClientMeasurement();

		String name = get(MEASUREMENT_NAME).getValue();
		String description = get(MEASUREMENT_DESCRIPTION).getValue();
		List<Unit> units = get(UNIT).getValue();
		List<ClientUnit> clientUnits = new ArrayList<ClientUnit>();
		for (Unit unit : units) {
			clientUnits.add(new ClientUnit(unit.getType(), unit.getFactor()));
		}
		measurement.setUnits(clientUnits);
		measurement.setName(name);
		measurement.setDesctiption(description);

		create(measurement, context);
		return null;
	}
}
