package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

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
		ClientTAXAgency taxAgency = context.getSelection(TAX_AGENCIES);

		if (taxAgency != null) {
			taxAgencyReq.setValue(taxAgency);
			context.setAttribute(INPUT_ATTR, "default");
		}
		ClientTAXAgency value = taxAgencyReq.getValue();
		Object selection = context.getSelection("values");
		if (!taxAgencyReq.isDone() || (value == selection)) {
			return getTaxAgencyResult(context);
		}
		Record supplierRecord = new Record(value);
		supplierRecord.add("", requirementName);
		supplierRecord.add("", value);
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
		List<ClientTAXAgency> skipTaxAgency = new ArrayList<ClientTAXAgency>();
		if (last != null) {
			taxAgenciesList.add(createTaxAgencyRecord((ClientTAXAgency) last));
			skipTaxAgency.add((ClientTAXAgency) last);
		}

		List<ClientTAXAgency> taxAgencies = getTaxAgencies();

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientTAXAgency> pagination = pagination(context, selection,
				actions, taxAgencies, skipTaxAgency, VALUES_TO_SHOW);

		for (ClientTAXAgency taxagencies : pagination) {
			taxAgenciesList.add(createTaxAgencyRecord(taxagencies));
		}

		int size = taxAgenciesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Tax Agency");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(taxAgenciesList);
		result.add(commandList);
		result.add("Select the Tax Agency");

		return result;
	}

	protected List<ClientTAXAgency> getTaxAgencies() {
		ArrayList<ClientTAXAgency> taxAgencies = getClientCompany()
				.getTaxAgencies();
		return new ArrayList<ClientTAXAgency>(taxAgencies);
	}

	protected Record createTaxAgencyRecord(ClientTAXAgency taxAgency) {
		Record record = new Record(taxAgency);
		record.add("Name", taxAgency.getName());
		return record;
	}

	protected Result taxItemRequirement(Context context, ResultList list,
			String requirementName) {
		Requirement taxItemReq = get(requirementName);
		ClientTAXItem taxItem = context.getSelection(TAX_ITEMS);

		if (taxItem != null) {
			taxItemReq.setValue(taxItem);
			context.setAttribute(INPUT_ATTR, "default");
		}

		ClientTAXItem value = taxItemReq.getValue();
		Object selection = context.getSelection("values");

		if (!taxItemReq.isDone() || (value == selection)) {
			return getTaxItemResult(context);
		}

		Record record = new Record(value);
		record.add("", requirementName);
		record.add("", value.getName());
		list.add(record);

		return null;
	}

	protected Result getTaxItemResult(Context context) {

		Result result = context.makeResult();
		ResultList taxItemsList = new ResultList(TAX_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);

		List<ClientTAXItem> skipTAXItem = new ArrayList<ClientTAXItem>();
		if (last != null) {
			taxItemsList.add(createTaxItemRecord((ClientTAXItem) last));
			skipTAXItem.add((ClientTAXItem) last);

		}
		List<ClientTAXItem> taxAgencies = getTaxItems();

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<ClientTAXItem> pagination = pagination(context, selection,
				actions, taxAgencies, skipTAXItem, VALUES_TO_SHOW);

		for (ClientTAXItem taxagencies : pagination) {
			taxItemsList.add(createTaxItemRecord(taxagencies));
		}

		int size = taxItemsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Tax Item.");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(taxItemsList);
		result.add(commandList);
		result.add("Select the Tax Item");

		return result;
	}

	protected List<ClientTAXItem> getTaxItems() {
		ArrayList<ClientTAXItem> taxItems = getClientCompany().getTaxItems();
		return new ArrayList<ClientTAXItem>(taxItems);
	}

	protected Record createTaxItemRecord(ClientTAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("Name", taxItem.getName());
		return record;
	}

	protected int getCompanyType(Context context) {
		return context.getCompany().getAccountingType();
	}

}
