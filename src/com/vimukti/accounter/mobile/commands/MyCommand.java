package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class MyCommand extends NewAbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// list.add(new TransactionItemTableRequirement("Items",
		// "Please Enter Item Name or number", getConstants().items(),
		// false, true));

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
