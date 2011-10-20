package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class VATItemsListCommand extends AbstractCommand {

	private static final String CURRENT_VIEW = "currentView";

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
		result = createVatItemsList(context);
		return result;
	}

	private Result createVatItemsList(Context context) {

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return null;
			case ACTIVE:
				context.setAttribute(CURRENT_VIEW, true);
				break;
			case IN_ACTIVE:
				context.setAttribute(CURRENT_VIEW, false);
				break;
			case ALL:
				context.setAttribute(CURRENT_VIEW, false);
				break;
			default:
				break;
			}
		}
		Result result = vatItemssList(context, selection);
		return result;
	}

	private Result vatItemssList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList taxItemsList = new ResultList("Tax Items List");
		result.add(getConstants().vatItemList());

		Boolean isActive = (Boolean) context.getAttribute(CURRENT_VIEW);
		List<TAXItem> taxItems = getVatItems(context.getCompany(), isActive);

		ResultList actions = new ResultList("actions");

		List<TAXItem> pagination = pagination(context, selection, actions,
				taxItems, new ArrayList<TAXItem>(), VALUES_TO_SHOW);

		for (TAXItem taxItem : pagination) {
			taxItemsList.add(createVatItemRecord(taxItem));
		}

		result.add(taxItemsList);

		Record inActiveRec = new Record(ActionNames.ACTIVE);
		inActiveRec.add("", getConstants().active());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.IN_ACTIVE);
		inActiveRec.add("", getConstants().inActive());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().newVATItem());
		result.add(commandList);
		return result;
	}

	private List<TAXItem> getVatItems(Company company, Boolean isActive) {
		List<TAXItem> result = new ArrayList<TAXItem>();
		Set<TAXItem> taxItems = company.getTaxItems();
		if (isActive == null) {
			return new ArrayList<TAXItem>(taxItems);
		}
		for (TAXItem taxCode : taxItems) {
			if (isActive) {
				if (taxCode.isActive()) {
					result.add(taxCode);
				}
			} else {
				if (!taxCode.isActive()) {
					result.add(taxCode);
				}
			}
		}
		return result;
	}

	private Record createVatItemRecord(TAXItem last) {
		Record record = new Record(last);
		record.add("Product", last.getName() != null ? last.getName() : "");
		TAXAgency agency = last.getTaxAgency();
		record.add("Vat Agency", agency != null ? agency.getName() : "");
		record.add("Description", last.getDescription());
		record.add("Rate", last.getTaxRate());
		return record;
	}

}
