package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class ItemsCommand extends NewAbstractCommand {

	private static final String ITEMS_TYPE = "itemsType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ActionRequirement(ITEMS_TYPE, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().active());
				list.add(getConstants().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<Item>("items", "Please Select Item", 5) {

			@Override
			protected String onSelection(Item value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().itemList();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected Record createRecord(Item value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getPurchaseDescription());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create Product Item");
				list.add("Create Service Item");
			}

			@Override
			protected boolean filter(Item e, String name) {

				return e.getName().startsWith(name)
						|| e.getPurchaseDescription().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Item> getLists(Context context) {
				Set<Item> items = getItems(context);
				List<Item> result = new ArrayList<Item>();
				String type = get(ITEMS_TYPE).getValue();

				for (Item item : items) {

					if (type.equalsIgnoreCase("Active")) {
						if (item.isActive())
							result.add(item);

					}
					if (type.equalsIgnoreCase("In-Active")) {
						if (!item.isActive())
							result.add(item);
					}
				}
				return result;
			}

		});

	}

	private Set<Item> getItems(Context context) {
		return context.getCompany().getItems();

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
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
		get(ITEMS_TYPE).setDefaultValue(getConstants().active());

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
