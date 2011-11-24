package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;

public class DeleteCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String inputString = context.getString();
		String[] split = inputString.split(" ");
		
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

}
