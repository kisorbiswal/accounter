package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class ReportCommand extends AbstractCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public Result run(Context context) {
		Result result = context.makeResult();
		
		return result;
	}

}
