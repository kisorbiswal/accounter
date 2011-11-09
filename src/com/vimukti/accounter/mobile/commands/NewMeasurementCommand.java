package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.MeasurementUnitRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientMeasurement;

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
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// if (measurement.getID() == 0) {
		return "Create Measurement command is activated.";
		// }
		// return "Update Measuremnt(" + measurement.getName()
		// + ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		// if (measurement.getID() == 0) {
		return "Measurement is ready to create with following details.";
		// } else {
		// return "Measurement is ready to update with following details.";
		// }
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		// if (measurement.getID() == 0) {
		return "Measurement is created succesfully.";
		// } else {
		// return "Measurement is updated successfully.";
		// }
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
		measurement.setName(name);
		measurement.setDesctiption(description);

		create(measurement, context);
		return null;
	}

}
