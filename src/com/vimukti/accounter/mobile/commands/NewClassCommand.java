package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;

public class NewClassCommand extends NewAbstractCommand {
	private static final String CLASS_NAME = "Class";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringRequirement(CLASS_NAME, getMessages().pleaseEnter(
				getConstants().className()), "class name", false,
				true));

	}
	
	@Override
	protected Result onCompleteProcess(Context context) {
		ClientAccounterClass accounterClass = new ClientAccounterClass();
		accounterClass.setClassName((String)get(CLASS_NAME).getValue());
		create(accounterClass, context);
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
		return "New Class commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "New class commond is ready with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return "New class commond is created successfully";
	}

}
