package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewTaxGroupCommand extends AbstractVATCommand {

	private static final String TAX_ITEMS_LIST = "TaxItemsList";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(TAX_ITEMS_LIST, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = nameRequirement(context);
		if (result != null) {
			return result;
		}

		result = taxItemsRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createTaxGroup(context);
	}

	private Result createTaxGroup(Context context) {
		TAXGroup taxGroup = new TAXGroup();
		String name = get(NAME).getValue();
		List<TAXItem> taxItems = get(TAX_ITEMS_LIST).getValue();
		taxGroup.setName(name);
		taxGroup.setTAXItems(taxItems);

		create(taxGroup, context);
		markDone();

		Result result = new Result();
		result.add("Tax Group was created successfully.");
		return result;
	}

	private Result taxItemsRequirement(Context context) {
		Requirement itemsReq = get(TAX_ITEMS_LIST);
		List<TAXItem> transactionItems = context.getSelections(TAX_ITEMS_LIST);
		if (!itemsReq.isDone()) {
			if (transactionItems.size() > 0) {
				itemsReq.setValue(transactionItems);
			} else {
				return taxItems(context);
			}
		}
		if (transactionItems != null && transactionItems.size() > 0) {
			List<TAXItem> items = itemsReq.getValue();
			items.addAll(transactionItems);
		}
		return null;
	}

	private Result taxItems(Context context) {
		Result result = context.makeResult();
		List<TAXItem> items = getTaxItems();
		ResultList list = new ResultList(TAX_ITEMS_LIST);
		Object last = context.getLast(RequirementType.TAXITEM);
		int num = 0;
		if (last != null) {
			list.add(creatTaxItemRecord((TAXItem) last));
			num++;
		}
		Requirement itemsReq = get(TAX_ITEMS_LIST);
		List<TAXItem> transItems = itemsReq.getValue();
		List<TAXItem> availableItems = new ArrayList<TAXItem>();
		for (TAXItem taxItem : transItems) {
			availableItems.add(taxItem);
		}
		for (TAXItem item : items) {
			if (item != last || !availableItems.contains(item)) {
				list.add(creatTaxItemRecord(item));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect an Tax Item(s).");
		} else {
			result.add("You don't have Tax Items.");
		}

		result.add(list);
		return result;
	}

	private Record creatTaxItemRecord(TAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("Name", taxItem.getName());
		record.add("Current Rate", taxItem.getTaxRate());
		return record;
	}

	private List<TAXItem> getTaxItems() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_TAX_ITEMS:
				return taxItems(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get(TAX_ITEMS_LIST);
		List<TAXItem> transItems = itemsReq.getValue();

		selection = context.getSelection("taxItemsList");
		if (selection != null) {
			Result result = taxItems(context);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement nameReq = get(NAME);
		String name = (String) nameReq.getValue();
		Record nameRecord = new Record(name);
		nameRecord.add("Name", "Selected Tax Group Item");
		nameRecord.add("Value", name);

		list.add(nameRecord);

		Result result = context.makeResult();
		result.add("Tax Group is ready to create with following values.");
		result.add(list);

		result.add("TaxItems:-");
		ResultList items = new ResultList(TAX_ITEMS_LIST);
		for (TAXItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getName());
			itemRec.add("Current Rate", item.getTaxRate());
			items.add(itemRec);
		}
		result.add(items);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_TAX_ITEMS);
		moreItems.add("", "Add more Tax items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Tax Group.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

}
