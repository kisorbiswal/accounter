package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public class NewTaxGroupCommand extends AbstractVATCommand {

	private static final String TAX_ITEMS_LIST = "taxItemsList";

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
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result makeResult = context.makeResult();
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		Result result = nameRequirement(
				context,
				list,
				NAME,
				getMessages().name(getConstants().taxGroup()),
				getMessages().pleaseEnter(
						getMessages().name(getConstants().taxGroup())));
		if (result != null) {
			return result;
		}

		result = taxItemsRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createTaxGroup(context);
	}

	private Result createTaxGroup(Context context) {
		String name = get(NAME).getValue();
		List<ClientTAXItem> taxItems = get(TAX_ITEMS_LIST).getValue();
		ClientTAXGroup taxGroup = new ClientTAXGroup();
		taxGroup.setName(name);
		taxGroup.setActive(true);
		taxGroup.setPercentage(true);
		taxGroup.setSalesType(true);
		taxGroup.setTaxItems(taxItems);
		ClientTAXItem itemByName = getClientCompany().getTaxItemByName(
				taxGroup.getName());
		ClientTAXGroup taxGroupByName = getClientCompany().getTaxGroupByName(
				taxGroup.getName());
		if (itemByName != null || taxGroupByName != null) {
			// Already exists;
		}
		create(taxGroup, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().taxGroup()));
		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().taxGroup()));
		actions.add(finish);
		return makeResult;
	}

	private Result taxItemsRequirement(Context context, ResultList list,
			ResultList actions, Result result) {
		Requirement itemsReq = get(TAX_ITEMS_LIST);
		List<ClientTAXItem> taxItems = itemsReq.getValue();
		if (taxItems == null) {
			taxItems = new ArrayList<ClientTAXItem>();
		}
		List<ClientTAXItem> transactionItems = context
				.getSelections(TAX_ITEMS_LIST);
		if (transactionItems != null && transactionItems.size() > 0) {
			for (ClientTAXItem clientTAXItem : transactionItems) {
				taxItems.add(clientTAXItem);
			}
		}
		if (taxItems.size() == 0) {
			return taxItems(context);
		}
		Object selection = context.getSelection("taxItems");
		if (selection != null) {
			ClientTAXItem selectedItem = (ClientTAXItem) selection;
			taxItems.remove(selectedItem);
		}
		itemsReq.setValue(taxItems);
		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ITEMS) {
			return taxItems(context);
		}

		result.add(getConstants().items());
		ResultList itemsList = new ResultList("taxItems");
		for (ClientTAXItem item : taxItems) {
			Record itemRec = new Record(item);
			itemRec.add("", item.getName());
			itemRec.add("", item.getTaxRate());
			itemsList.add(itemRec);
		}
		result.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", getMessages().addMore(getConstants().taxItemsList()));
		actions.add(moreItems);
		return null;
	}

	private Result taxItems(Context context) {
		Result result = context.makeResult();
		List<ClientTAXItem> items = getTaxItems();
		ResultList list = new ResultList(TAX_ITEMS_LIST);
		Object last = context.getLast(RequirementType.TAXITEM);
		int num = 0;
		if (last != null) {
			list.add(creatTaxItemRecord((ClientTAXItem) last));
			num++;
		}
		Requirement itemsReq = get(TAX_ITEMS_LIST);
		List<ClientTAXItem> transItems = itemsReq.getValue();
		if (transItems == null) {
			transItems = new ArrayList<ClientTAXItem>();
		}
		List<ClientTAXItem> availableItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem taxItem : transItems) {
			availableItems.add(taxItem);
		}
		for (ClientTAXItem item : items) {
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
			result.add(getMessages().pleaseSelect(getConstants().taxItem()));
		} else {
			result.add(getMessages().youDontHaveAny(
					getConstants().taxItemsList()));
		}

		result.add(list);
		return result;
	}

	private Record creatTaxItemRecord(ClientTAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("", getConstants().name());
		record.add("", taxItem.getName());
		record.add("", getConstants().currentRate());
		record.add("", taxItem.getTaxRate());
		return record;
	}
}
