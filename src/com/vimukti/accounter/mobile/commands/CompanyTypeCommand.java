package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class CompanyTypeCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result makeResult = context.makeResult();
		Object selection = context.getSelection("actions");
		if (selection == null) {
			makeResult
					.add("Select 'Full Setup' to enter the Setup wizard to add all the necessary information for company or else select 'Partial Setup' to skip this wizard and add later.");
			ResultList list = new ResultList("actions");
			Record start = new Record(ActionNames.FULL_SETUP);
			start.add("", "Full Setup");
			list.add(start);

			Record skip = new Record(ActionNames.PARTIAL_SETUP);
			skip.add("", "Partial Setup");
			list.add(skip);
			makeResult.add(list);
			return makeResult;
		}
		markDone();
		if (selection == ActionNames.FULL_SETUP) {
			makeResult.setNextCommand("Full Company Setup");
		}
		if (selection == ActionNames.PARTIAL_SETUP) {
			makeResult.setNextCommand("Partial Company Setup");
		}
		return makeResult;
	}

}
