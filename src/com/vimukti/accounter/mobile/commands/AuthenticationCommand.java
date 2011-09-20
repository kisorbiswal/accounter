/**
 * 
 */
package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AuthenticationCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("userName", false, true));
		list.add(new Requirement("password", false, true));
		list.add(new Requirement("companyName", false, true));
		list.add(new Requirement("", false, true));
		list.add(new Requirement("", false, true));

	}

	@Override
	public Result run(Context context) {
		Result result = userNameRerquirement(context);
		if (result != null) {
			return result;
		}

		result = passwordRequirement(context);

		if (result != null) {
			return result;
		}

		return null;
	}

	/**
	 * @param context
	 * @return
	 */
	private Result passwordRequirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param context
	 * @return
	 */
	private Result userNameRerquirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
