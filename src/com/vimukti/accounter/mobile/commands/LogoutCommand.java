package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.MobileSession;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class LogoutCommand extends Command {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		MobileSession ioSession = context.getIOSession();
		ioSession.setAuthentication(false);
		ioSession.setExpired(true);
		Result result = new Result();
		result.add("You are successfully logged out.");
		result.setCookie("No Cookie");
		return result;
	}

}
