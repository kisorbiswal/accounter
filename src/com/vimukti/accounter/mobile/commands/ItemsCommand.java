package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItem;

public class ItemsCommand extends AbstractTransactionCommand {

	private static final String ACTIVE = "active";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACTIVE, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createitemsListReq(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result createitemsListReq(Context context) {
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

		Result result = isActiveRequirement(context, selection);

		Boolean isActive = (Boolean) get(ACTIVE).getValue();
		result = itemsList(context, isActive);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result itemsList(Context context, Boolean isActive) {
		Result result = context.makeResult();
		ResultList itemResult = new ResultList("items");
		result.add("Items List");
		int num = 0;
		List<ClientItem> items = getItems(isActive);
		for (ClientItem item : items) {
			itemResult.add(createItemRecord(item));
			num++;
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		int size = itemResult.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Item");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(itemResult);
		result.add(commandList);
		result.add("Enter for Item");

		return result;

	}

	protected List<ClientItem> getItems(Boolean isActive) {
		ArrayList<ClientItem> items = new ArrayList<ClientItem>(
				getClientCompany().getItems());
		ArrayList<ClientItem> result = new ArrayList<ClientItem>();

		for (ClientItem item : items) {
			if (isActive) {
				if (item.isActive()) {
					result.add(item);
				}
			} else {
				result.add(item);
			}
		}

		return result;
	}

}
