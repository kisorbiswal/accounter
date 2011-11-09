package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ListFilter;

public abstract class AbstractVATCommand extends AbstractCommand {

	protected static final String TAX_AGENCY = "taxAgency";
	private static final String TAX_AGENCIES = "taxAgencies";

	protected static final String TAX_ITEM = "taxItem";
	private static final String TAX_ITEMS = "taxItems";
	protected static final String AMOUNT = "amount";
	protected static final String NAME = "name";

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	/**
	 * 
	 * @param context
	 * @param list
	 * @param requirementName
	 * @return
	 */
	protected Result taxAgencyRequirement(Context context, ResultList list,
			String requirementName) {

		Requirement taxAgencyReq = get(requirementName);
		TAXAgency taxAgency = context.getSelection(TAX_AGENCIES);

		if (taxAgency != null) {
			taxAgencyReq.setValue(taxAgency);
			context.setAttribute(INPUT_ATTR, "default");
		}
		TAXAgency value = taxAgencyReq.getValue();
		Object selection = context.getSelection("values");
		if (!taxAgencyReq.isDone() || (value == selection)) {
			return getTaxAgencyResult(context);
		}
		Record supplierRecord = new Record(value);
		supplierRecord.add("", requirementName);
		supplierRecord.add("", value.getName());
		list.add(supplierRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	protected Result getTaxAgencyResult(Context context) {
		Result result = context.makeResult();
		ResultList taxAgenciesList = new ResultList(TAX_AGENCIES);

		Object last = context.getLast(RequirementType.TAXAGENCY);
		List<TAXAgency> skipTaxAgency = new ArrayList<TAXAgency>();
		if (last != null) {
			taxAgenciesList.add(createTaxAgencyRecord((TAXAgency) last));
			skipTaxAgency.add((TAXAgency) last);
		}

		List<TAXAgency> taxAgencies = getTaxAgencies(context);

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<TAXAgency> pagination = pagination(context, selection, actions,
				taxAgencies, skipTaxAgency, VALUES_TO_SHOW);

		for (TAXAgency taxagencies : pagination) {
			taxAgenciesList.add(createTaxAgencyRecord(taxagencies));
		}

		int size = taxAgenciesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(getMessages().pleaseSelect(
					getConstants().taxAgency()));
		}

		CommandList commandList = new CommandList();
		commandList.add(getMessages().create(getConstants().taxAgency()));

		result.add(message.toString());
		result.add(taxAgenciesList);
		result.add(commandList);

		return result;
	}

	protected List<TAXAgency> getTaxAgencies(Context contex) {
		Set<TAXAgency> taxAgencies = contex.getCompany().getTaxAgencies();
		return new ArrayList<TAXAgency>(taxAgencies);
	}

	protected Record createTaxAgencyRecord(TAXAgency taxAgency) {
		Record record = new Record(taxAgency);
		record.add("Name", taxAgency.getName());
		return record;
	}

	protected Result taxItemRequirement(Context context, ResultList list,
			String requirementName, String displayString, String name,
			ListFilter<TAXItem> filter) {
		Requirement taxItemReq = get(requirementName);
		TAXItem taxItem = context.getSelection(requirementName);

		if (taxItem != null) {
			taxItemReq.setValue(taxItem);
			context.setAttribute(INPUT_ATTR, "default");
		}

		TAXItem value = taxItemReq.getValue();
		Object selection = context.getSelection("values");

		if (!taxItemReq.isDone() || (requirementName == selection)) {
			return getTaxItemResult(context, displayString, filter,
					requirementName);
		}

		Record record = new Record(requirementName);
		record.add("", name);
		record.add("", value.getName());
		list.add(record);

		return null;
	}

	protected Result getTaxItemResult(Context context, String displayString,
			ListFilter<TAXItem> filter, String requirementName) {

		Result result = context.makeResult();
		ResultList taxItemsList = new ResultList(requirementName);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);

		List<TAXItem> skipTAXItem = new ArrayList<TAXItem>();
		if (last != null) {
			taxItemsList.add(createTaxItemRecord((TAXItem) last));
			skipTAXItem.add((TAXItem) last);

		}
		List<TAXItem> taxAgencies = new ArrayList<TAXItem>();
		for (TAXItem obj : context.getCompany().getTaxItems()) {
			if (filter.filter(obj)) {
				taxAgencies.add(obj);
			}
		}

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<TAXItem> pagination = pagination(context, selection, actions,
				taxAgencies, skipTAXItem, VALUES_TO_SHOW);

		for (TAXItem taxagencies : pagination) {
			taxItemsList.add(createTaxItemRecord(taxagencies));
		}

		int size = taxItemsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(displayString);
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(taxItemsList);
		result.add(commandList);

		return result;
	}

	protected List<TAXItem> getTaxItems(Context context) {
		Set<TAXItem> taxItems = context.getCompany().getTaxItems();
		return new ArrayList<TAXItem>(taxItems);
	}

	protected Record createTaxItemRecord(TAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("Name", taxItem.getName());
		return record;
	}
}
