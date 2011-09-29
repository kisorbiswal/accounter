package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.NetworkUser;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class AuthenticateCommand extends AbstractCommand {

	@Override
	public String getId() {
		return "authenticate";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("emailId", false, true));
		list.add(new Requirement("activationCode", false, true));
	}

	@Override
	public Result run(Context context) {

		Requirement requirement = get("emailId");
		if (!requirement.isDone()) {
			Result result = processEmail(context);
		}

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

	private Result processEmail(Context context) {
		String email = getEmailOfFrom(context);
		Result result = new Result();
		CommandList commands = new CommandList();
		if (email == null) {
			commands.add("Create New Account");
		} else {
			NetworkUser ntUser = getNetworkUser(email);
			if (ntUser == null) {
				commands.add("Register this device with Email ID Account");
			}
		}
		commands.add("Enter Accounter EmailID to Register this Device with Existing Account");
		result.add(commands);
		return result;
	}

	private NetworkUser getNetworkUser(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getEmailOfFrom(Context context) {
		NetworkUser from = context.getIOSession().getFrom();
		// TODO Auto-generated method stub
		return null;
	}

	private void askForEmail(Context context, String error) {
		// First get the emailID from the user
		// String email = getEmailFromUser(context.getIOSession().getFrom());
		// if (email != null) {
		// // We can show this default emailid
		// }
	}

	private boolean isValidEmail(String email) {
		return email
				.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");
	}

	private String getEmailFromUser(String from) {
		// TODO SPlit the from with '/' and see if first part is valid email
		// else return null
		return null;
	}

}
