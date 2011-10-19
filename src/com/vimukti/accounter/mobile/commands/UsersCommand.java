package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientUserInfo;

public class UsersCommand extends AbstractTransactionCommand {
	private static final String USER_TYPE = "userType";
	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result result = createUsersList(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createUsersList(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case ACTIVE:
				context.setAttribute(USER_TYPE, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(USER_TYPE, false);
				break;
			case ALL:
				context.setAttribute(USER_TYPE, null);
				break;
			default:
				break;
			}
		}
		Result result = usersList(context,selection);
		return result;
	}

	private Result usersList(Context context, ActionNames selection) {
		Result result = context.makeResult();
		ResultList usersList = new ResultList("usersList");
		result.add("Users List");

		Boolean userType = (Boolean) context.getAttribute(USER_TYPE);
		List<ClientUserInfo> users = getUsers(userType); 

		ResultList actions = new ResultList("actions");

		List<ClientUserInfo> pagination = pagination(context, selection, actions,
				users, new ArrayList<ClientUserInfo>(), VALUES_TO_SHOW);

		for (ClientUserInfo user : pagination) {
			usersList.add(createUserRecord(user));
		}

		StringBuilder message = new StringBuilder();
		if (usersList.size() > 0) {
			message.append("Select an User");
		}

		result.add(message.toString());
		result.add(usersList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", "Active Users");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", "InActive Users");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Users");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Invite User");
		result.add(commandList);
		return result;

	}

	 
	private Record createUserRecord(ClientUserInfo user) {
		Record record = new Record(user);
		// record.add("First Name", user.getFirstName());
		// record.add("Last Name", user.getLastName());
		record.add("User Role", user.getUserRole());
		record.add("Email Id", user.getEmail());
		record.add("Status", user.isActive()?"active" :"inactive");
		return record;
	}

	private List<ClientUserInfo> getUsers(Boolean isActive) {

		ArrayList<ClientUserInfo> users = getClientCompany().getUsersList(); 
		if (isActive == null) {
			return users;
		}
		ArrayList<ClientUserInfo> result = new ArrayList<ClientUserInfo>();
		for (ClientUserInfo user : users) {
			if (user.isActive() == isActive) {
				result.add(user);
			}
		}
		return result;
	
	}

	 

}
