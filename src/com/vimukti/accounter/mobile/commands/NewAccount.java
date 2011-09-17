package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class NewAccount extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("Account Type", true, true));
		list.add(new Requirement("AccountName", false, true));
		list.add(new Requirement("AccountNumber", false, true));
		list.add(new Requirement("OpeningBalance", true, true));
		list.add(new Requirement("Active", true, true));
		list.add(new Requirement("AsOf", true, true));
		list.add(new Requirement("Comments", true, true));
		list.add(new Requirement("Consider As Cash Account", true, true));
	}

	@Override
	public Result run(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
