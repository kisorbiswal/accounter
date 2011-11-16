package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;

public class NewClassCommand extends NewAbstractCommand {
	private static final String CLASS_NAME = "Class";
	ClientAccounterClass accounterClass;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(CLASS_NAME, getMessages().pleaseEnter(
				getConstants().className()), "class name", false, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		accounterClass.setClassName((String) get(CLASS_NAME).getValue());
		create(accounterClass, context);
		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Class to update.");
				return "Location list";
			}
			AccounterClass classByName = CommandUtils.getClassByName(
					context.getCompany(), string);
			if (classByName == null) {
				addFirstMessage(context, "Select a Class to update.");
				return "Location list " + string;
			}
			accounterClass = (ClientAccounterClass) CommandUtils
					.getClientObjectById(classByName.getID(),
							AccounterCoreType.ACCOUNTER_CLASS, context
									.getCompany().getId());
			get(CLASS_NAME).setValue(accounterClass.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(CLASS_NAME).setValue(string);
			}
			accounterClass = new ClientAccounterClass();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return accounterClass.getID() == 0 ? "New Class command is activated"
				: "Update Class command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return accounterClass.getID() == 0 ? "New class is ready with the following values"
				: "Class is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return accounterClass.getID() == 0 ? "New class is created successfully"
				: "Class updated successfully";
	}

}
