package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;

public class EditFiscalYearCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement("startDate", getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), false, true));
		list.add(new DateRequirement("endDate", getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), false, true));
		list.add(new NumberRequirement("status", getMessages().pleaseEnter(
				getMessages().status()), getMessages().status(), true, true));

	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
