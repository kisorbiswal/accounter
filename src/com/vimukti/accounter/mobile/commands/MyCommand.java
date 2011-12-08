package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.MultiRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;

public class MyCommand extends NewAbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("NAME", "PLease enter name", "Name",
				false, true));
		list.add(new MultiRequirement<Address>("Address", "Please Enter Addr",
				"Address", true, true) {

			@Override
			protected void addRequirement(List<Requirement> list) {
				list.add(new StringRequirement("Streat", "Please enter Streat",
						"Streat", false, true));
				list.add(new StringRequirement("City", "Please enter City",
						"City", false, true));
				list.add(new StringRequirement("Village",
						"Please enter Village", "Village", true, true));
			}

			@Override
			protected void setDefaultValues() {

			}

			@Override
			protected String getDisplayValue() {
				return getRequirement("Streat").getValue() + "-"
						+ getRequirement("City").getValue() + "-"
						+ getRequirement("Village").getValue();
			}

			@Override
			protected Result finish(Context context) {
				return null;
			}

		});
	}

	@Override
	protected String getDetailsMessage() {
		return "MyCommand is ready to create";
	}

	@Override
	protected void setDefaultValues(Context context) {

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

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}
}
