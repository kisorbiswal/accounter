package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class VATItemsListCommand extends AbstractCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ACTIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createVatItemsList(context);
		return result;
	}

	private Result createVatItemsList(Context context) {
		Result result = null;
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

		result = isActiveRequirement(context, selection);
		if (result != null) {
			return result;
		}

		Boolean isActive = (Boolean) get(ACTIVE).getValue();

		result = vatItemssList(context, isActive);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result vatItemssList(Context context, Boolean isActive) {
		ResultList list = new ResultList("values");
		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		if (last != null) {
			list.add(createVatItemRecord((TAXItem) last));
		}

		List<TAXItem> vatItems = getVatItems(context.getHibernateSession(),
				isActive);
		for (int i = 0; i < VALUES_TO_SHOW || i < vatItems.size(); i++) {
			TAXItem vatItemGroup = vatItems.get(i);
			if (vatItemGroup != last) {
				list.add(createVatItemRecord((TAXItem) vatItemGroup));
			}
		}
		Result result = new Result();

		int size = list.size();
		StringBuilder message = new StringBuilder();
		if (size == 0) {
			message.append("No records to show.");
			result.add(message.toString());
			return result;
		}

		String activeString = "";
		if (isActive) {
			activeString = "Active Vat Items";
		} else {
			activeString = "InActive Vat Items";
		}
		result.add(activeString);
		result.add(list);

		return result;
	}

	private List<TAXItem> getVatItems(Session hibernateSession, Boolean isActive) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createVatItemRecord(TAXItem last) {
		Record record = new Record(last);
		record.add("Active", last.isActive());
		record.add("Product", last.getName() != null ? last.getName() : "");
		TAXAgency agency = last.getTaxAgency();
		record.add("Vat Agency", agency != null ? agency.getName() : "");
		record.add("Description", last.getDescription());
		record.add("Rate", last.getTaxRate());
		return record;
	}

	private Result isActiveRequirement(Context context, Object selection) {
		Requirement isActiveReq = get(ACTIVE);
		Boolean isActive = (Boolean) isActiveReq.getValue();
		if (selection == isActive) {
			context.setAttribute(INPUT_ATTR, ACTIVE);
			isActive = !isActive;
			isActiveReq.setValue(isActive);
		}

		return null;
	}

}
