package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientLocation;

public class NewLocationCommand extends NewAbstractCommand {

	private static final String LOCATION_NAME = "location name";
	ClientLocation location;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(LOCATION_NAME, getMessages()
				.pleaseEnter(getMessages().location()), getMessages()
				.location(), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		location.setLocationName((String) get(LOCATION_NAME).getValue());
		create(location, context);
		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Location to update.");
				return "Location list";
			}
			Location locationByName = CommandUtils.getLocationByName(
					context.getCompany(), string);
			if (locationByName == null) {
				addFirstMessage(context, "Select a Location to update.");
				return "Location list " + string;
			}
			location = (ClientLocation) CommandUtils.getClientObjectById(
					locationByName.getID(), AccounterCoreType.LOCATION, context
							.getCompany().getId());
			get(LOCATION_NAME).setValue(location.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(LOCATION_NAME).setValue(string);
			}
			location = new ClientLocation();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return location.getID() == 0 ? "New Location Commond is Activated"
				: "Update Location command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return location.getID() == 0 ? "New Location commond is ready with the following values"
				: "Location is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return location.getID() == 0 ? "New Location commond is created successfully"
				: "Location updated successfully";
	}

}
