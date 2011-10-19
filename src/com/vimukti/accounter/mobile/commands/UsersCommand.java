package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class UsersCommand extends AbstractTransactionCommand {

	private static final int USERS_TO_SHOW = 5;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {

		Result result = optionalRequirements(context);
		return result;
	}

	private Result optionalRequirements(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		Result result = usersList(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result usersList(Context context) {
		Result result = context.makeResult();
		ResultList userList = new ResultList("users");
		int num = 0;
		List<User> users = getUsersList();
		for (User user : users) {
			userList.add(createUserRecord(user));
			num++;
			if (num == USERS_TO_SHOW) {
				break;
			}
		}

		result.add(userList);

		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(commandList);

		return result;
	}

	private List<User> getUsersList() {
		// TODO need to get list of Users
		return null;
	}

	private Record createUserRecord(User user) {
		Record record = new Record(user);
		// record.add("First Name", user.getFirstName());
		// record.add("Last Name", user.getLastName());
		record.add("User Role", user.getUserRole());
		record.add("Email Id", user.getClient().getEmailId());
		// record.add("Status", user.isActive());
		return record;
	}

}
