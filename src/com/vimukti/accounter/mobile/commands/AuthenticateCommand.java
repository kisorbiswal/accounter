package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class AuthenticateCommand extends AbstractCommand {

	@Override
	public String getId() {
		return "authenticate";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		String userEmail = context.getString();
		if (userEmail != null) {
			if (isValidEmail(userEmail)) {
				askForEmail(context, "Invalid Email address");
			} else {
				
			}
		} else {
			askForEmail(context, null);
		}

		return null;
	}

	private void askForEmail(Context context, String error) {
		// First get the emailID from the user
		String email = getEmailFromUser(context.getIOSession().getFrom());
		if (email != null) {
			// We can show this default emailid
		}
	}

	private boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	private String getEmailFromUser(String from) {
		// TODO SPlit the from with '/' and see if first part is valid email
		// else return null
		return null;
	}

}
