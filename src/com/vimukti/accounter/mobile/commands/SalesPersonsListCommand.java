package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class SalesPersonsListCommand extends AbstractCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createSalesPersonsList(context);
		return result;
	}

	private Result createSalesPersonsList(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
