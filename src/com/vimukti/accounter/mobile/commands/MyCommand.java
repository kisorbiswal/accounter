package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;

public class MyCommand extends NewCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("FirstName", "Enter Fist Name",
				"First Name", false, true));

		list.add(new NameRequirement("LastName", "Enter Last Name",
				"Last Name", true, true));
	}

	@Override
	protected String getReadyToCreateMessage() {
		return "Command is ready to create";
	}

	@Override
	protected void setDefaultValues() {
		get("LastName").setDefaultValue("");
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return "Success Command";
	}
}
