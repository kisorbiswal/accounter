package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientLocation;

public class NewLocationCommand extends NewAbstractCommand {

	private static final String LOCATION_NAME = "location name";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(LOCATION_NAME, getMessages()
				.pleaseEnter(getConstants().location()), getConstants()
				.location(), false, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientLocation location = new ClientLocation();
		location.setLocationName((String) get(LOCATION_NAME).getValue());
		create(location, context);
		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "New Location Commond is Activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "New Location commond is ready with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "New Location commond is created successfully";
	}

}
