package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class UsersCommand extends AbstractTransactionCommand {

	private static final int USERS_TO_SHOW = 5;

	protected static final Object USER_ITEM_PROCESS = null;
	protected static final String OLD_USER_ITEM_ATTR = null;
	private static final Object USER_LIST_PROCESS = null;
	private static final String USER_DETAILS = null;
	private static final String USER_PROPERTY_ATTR = null;
	protected static final String OLD_USER_ATTR = null;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {

		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(USER_LIST_PROCESS)) {
				result = UserProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = optionalRequirements(context);
		return null;
	}

	private Result optionalRequirements(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return users(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement users = get("users");
		List<User> usersList = users.getValue();

		selection = context.getSelection("usersList");
		if (selection != null) {
			Result result = userItem(context, (User) selection);
			if (result != null) {
				return result;
			}
		}

		Result result = usersList(context);
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add("User is ready to create with following values.");
		result.add(list);

		result.add("User:-");
		ResultList userResult = new ResultList("UsersList");
		for (User user : usersList) {
			Record userRec = new Record(user);
			userRec.add("First Name", user.getFirstName());
			userRec.add("Last Name", user.getLastName());
			userRec.add("User Role", user.getUserRole());
			userRec.add("Email Id", user.getEmail());

			userResult.add(userRec);
		}
		result.add(userResult);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more Users");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create User.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result userItem(Context context, User user) {

		context.setAttribute(PROCESS_ATTR, USER_ITEM_PROCESS);
		context.setAttribute(OLD_USER_ITEM_ATTR, user);

		String lineAttr = (String) context.getAttribute(USER_PROPERTY_ATTR);
		if (lineAttr != null) {
			context.removeAttribute(USER_PROPERTY_ATTR);
			if (lineAttr.equals("First Name")) {
				user.setFirstName(context.getString());
			} else if (lineAttr.equals("Last Name")) {
				user.setLastName(context.getString());
			} else if (lineAttr.equals("User Role")) {
				user.setUserRole(context.getString());
			} else if (lineAttr.equals("Email Id")) {
				user.setEmail(context.getString());
			} else if (lineAttr.equals("Status")) {
				// TODO
			}
		} else {
			Object selection = context.getSelection(USER_DETAILS);
			if (selection != null) {
				if (selection.equals(user.getFirstName())) {
					context.setAttribute(USER_PROPERTY_ATTR, "First Name");
					return text(context, "Enter First Name",
							user.getFirstName());
				} else if (selection.equals(user.getLastName())) {
					context.setAttribute(USER_PROPERTY_ATTR, "Last Name");
					return text(context, "Enter Last Name", user.getLastName());
				} else if (selection.equals(user.getUserRole())) {
					context.setAttribute(USER_PROPERTY_ATTR, "User Role");
					return text(context, "Enter User Role", user.getUserRole());
				} else if (selection.equals(user.getEmail())) {
					context.setAttribute(USER_PROPERTY_ATTR, "Email Id");
					return text(context, "Enter Email", user.getEmail());
				} else if (selection.equals(user.isActive())) {
					// TODO
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_TRANSACTION_ITEM_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}

		ResultList list = new ResultList(USER_DETAILS);
		Record record = new Record(user.getFirstName());
		record.add("", "First Name");
		record.add("", user.getFirstName());
		list.add(record);

		record = new Record(user.getLastName());
		record.add("", "Last Name");
		record.add("", user.getLastName());
		list.add(record);

		record = new Record(user.getUserRole());
		record.add("", "User Role");
		record.add("", user.getUserRole());
		list.add(record);

		record = new Record(user.getEmail());
		record.add("", "Email Id");
		record.add("", user.getEmail());
		list.add(record);

		Result result = context.makeResult();
		result.add("User details");
		result.add("User Name :" + user.getFirstName());
		result.add(list);

		ResultList actions = new ResultList(ACTIONS);
		record = new Record(ActionNames.DELETE_ITEM);
		record.add("", "Delete");
		actions.add(record);
		record = new Record(ActionNames.FINISH);
		record.add("", "Finish");
		actions.add(record);
		result.add(actions);
		return result;

	}

	private Result usersList(Context context) {
		return null;
	}

	protected Result users(Context context) {
		Result result = context.makeResult();

		ResultList list = new ResultList("users");
		Object last = context.getLast(RequirementType.USER);
		int num = 0;
		if (last != null) {
			list.add(creatItemRecord((Item) last));
			num++;
		}
		Requirement itemsReq = get("users");
		List<User> userItem = itemsReq.getValue();

		for (User item : userItem) {
			Record record = new Record(item);
			list.add(record);
			num++;

			if (num == USERS_TO_SHOW) {
				break;
			}
		}

		result.add(list);
		CommandList commands = new CommandList();
		commands.add("Create New User");
		return result;
	}

	protected Result UserProcess(Context context) {
		User user = (User) context.getAttribute(OLD_USER_ATTR);
		Result result = userItem(context, user);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement usersReq = get("users");
				List<TransactionItem> transItems = usersReq.getValue();
				transItems.remove(user);
				context.removeAttribute(OLD_USER_ATTR);

			}
		}
		return result;
	}
}
