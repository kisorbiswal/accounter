package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewClassCommond extends AbstractCommand {
	private static final String CLASS = "class";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CLASS, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		Object selection = context.getSelection(ACTIONS);
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");
		// result = shippingTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void completeProcess(Context context) {

	}

}
