package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ItemsCommand extends NewAbstractCommand {

	private static final String ITEMS_TYPE = "itemsType";
	private boolean isBuy;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(ITEMS_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<Item>("items", "Please Select Item",
				20) {

			@Override
			protected String onSelection(Item value) {
				return "Update Item " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().itemList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(Item value) {
				return ItemsCommand.this.createRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				ItemsCommand.this.setCreateCommand(list);
			}

			@Override
			protected boolean filter(Item e, String name) {

				return e.getName().startsWith(name)
						|| String.valueOf(e.getID()).startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Item> getLists(Context context) {
				Set<Item> items = getItems(context);
				List<Item> result = new ArrayList<Item>();
				String type = get(ITEMS_TYPE).getValue();
				boolean isActive = type.equalsIgnoreCase("Active") ? true
						: false;
				for (Item item : items) {
					if (isActive == item.isActive()
							&& item.isIBuyThisItem() == isBuy) {
						result.add(item);
					}
				}
				return result;
			}

		});

	}

	protected Record createRecord(Item value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		record.add("Description", value.getPurchaseDescription());
		record.add("Type", value.getType());
		if (value.isIBuyThisItem()) {
			record.add("Purchase Price",
					getPreferences().getPrimaryCurrency().getSymbol()
							+ " " + value.getPurchasePrice());
		}
		if (value.isISellThisItem()) {
			record.add("Sales Price",
					getPreferences().getPrimaryCurrency().getSymbol()
							+ " " + value.getSalesPrice());
		}
		return record;
	}

	protected String getItemTypeString(int type) {
		if (type == Item.TYPE_SERVICE) {
			return getMessages().serviceItem();
		} else if (type == Item.TYPE_INVENTORY_PART) {
			return getMessages().inventoryItem();
		} else if (type == Item.TYPE_NON_INVENTORY_PART) {
			return getMessages().nonInventoryItem();
		}
		return getMessages().productItem();
	}

	protected void setCreateCommand(CommandList list) {
		if (!isBuy) {
			list.add(new UserCommand("Create New Service Item", "sell"));
			list.add(new UserCommand("Create New NonInventory Item", "sell"));
			list.add(new UserCommand("Create New Inventory Item", "sell"));
		} else {
			list.add(new UserCommand("Create New Service Item", "buy"));
			list.add(new UserCommand("Create New NonInventory Item", "buy"));
			list.add(new UserCommand("Create New Inventory Item", "buy"));
		}
	}

	protected Set<Item> getItems(Context context) {
		return context.getCompany().getItems();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (context.getString().equalsIgnoreCase("vendor")) {
			isBuy = true;
		}
		context.setString("");
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ITEMS_TYPE).setDefaultValue(getMessages().active());

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
