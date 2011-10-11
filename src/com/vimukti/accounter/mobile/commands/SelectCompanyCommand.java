package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

/**
 * Selects the Company
 * 
 * @author vimutki35
 * 
 */
public class SelectCompanyCommand extends Command {

	@Override
	public String getId() {
		return "selectCompany";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub.
		list.add(new Requirement("", false, true));

	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
