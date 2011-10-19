package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItem;

public class ItemsCommand extends AbstractTransactionCommand {

	private static final String ITEMS_TYPE = "itemsType";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

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
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return null;
			case SERVICE_ITEM:
				context.setAttribute(ITEMS_TYPE, Item.TYPE_SERVICE);
				break;
			case PRODUCT_ITEM:
				context.setAttribute(ITEMS_TYPE, Item.TYPE_INVENTORY_PART);
				break;
			case ALL:
				context.setAttribute(ITEMS_TYPE, null);
				break;
			default:
				break;
			}
		}
		Result result = context.makeResult();
		result = itemsList(context, selection);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result itemsList(Context context, ActionNames selection) {
		Result result = context.makeResult();
		ResultList itemResult = new ResultList("items");
		result.add("Items List");

		Integer currentView = (Integer) context.getAttribute(ITEMS_TYPE);
		List<Item> items = getItems(context, currentView);

		ResultList actions = new ResultList("actions");

		List<Item> pagination = pagination(context, selection, actions, items,
				new ArrayList<Item>(), VALUES_TO_SHOW);
		for (Item item : pagination) {
			itemResult.add(createItemRecord(item));
		}

		result.add(itemResult);

		Record inActiveRec = new Record(ActionNames.SERVICE_ITEM);
		inActiveRec.add("", "Service Items");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.PRODUCT_ITEM);
		inActiveRec.add("", "Product Items");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Items");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");

		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Create New Item");
		result.add(commandList);

		return result;

	}

	private Record createItemRecord(Item item) {
		Record record = new Record(item);
		record.add("", item.getName());
		record.add("", item.getSalesPrice());
		return record;
	}

	private List<Item> getItems(Context context, Integer currentView) {
		Set<Item> items = context.getCompany().getItems();
		if (currentView == null) {
			return new ArrayList<Item>(items);
		}
		List<Item> result = new ArrayList<Item>();
		for (Item item : items) {
			if (item.getType() == currentView) {
				result.add(item);
			}
		}
		return result;
	}

	protected List<ClientItem> getItems(Boolean isActive) {
		ArrayList<ClientItem> items = new ArrayList<ClientItem>(
				getClientCompany().getItems());
		ArrayList<ClientItem> result = new ArrayList<ClientItem>();

		for (ClientItem item : items) {
			if (isActive) {
				if (item.getType() == Item.TYPE_SERVICE) {
					result.add(item);
				}
			} else {
				result.add(item);
			}
		}

		return result;
	}

}
