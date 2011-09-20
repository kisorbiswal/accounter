package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class DeleteFiscalYearCommand extends AbstractTransactionCommand{

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("startDate", false, true));
		list.add(new Requirement("endDate", false, true));
		list.add(new Requirement("status", true, true));
		
	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
