package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public abstract class AbstractVATCommand extends AbstractCommand {

	protected static final String TAX_AGENCY = "taxAgency";
	private static final String TAX_AGENCIES = "taxAgencies";

	protected static final String TAX_ITEM = "taxItem";
	private static final String TAX_ITEMS = "taxItems";
	// protected static final String ACCOUNT = "account";
	private static final String ACCOUNTS = "accounts";
	protected static final String AMOUNT = "amount";
	protected static final String NAME = "name";

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	protected Result taxAgencyRequirement(Context context) {
		Requirement taxAgencyReq = get(TAX_AGENCY);
		ClientTAXAgency taxAgency = context.getSelection(TAX_AGENCIES);
		if (taxAgency != null) {
			taxAgencyReq.setValue(taxAgency);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!taxAgencyReq.isDone()) {
			return getTaxAgencyResult(context);
		}
		return null;
	}

	protected Result getTaxAgencyResult(Context context) {
		Result result = context.makeResult();
		ResultList taxAgenciesList = new ResultList(TAX_AGENCIES);

		Object last = context.getLast(RequirementType.TAXAGENCY);
		if (last != null) {
			taxAgenciesList.add(createTaxAgencyRecord((ClientTAXAgency) last));
		}

		List<ClientTAXAgency> taxAgencies = getTaxAgencies();
		for (int i = 0; i < VALUES_TO_SHOW && i < taxAgencies.size(); i++) {
			ClientTAXAgency taxAgency = taxAgencies.get(i);
			if (taxAgency != last) {
				taxAgenciesList
						.add(createTaxAgencyRecord((ClientTAXAgency) taxAgency));
			}
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

	protected Result taxItemRequirement(Context context) {
		Requirement taxItemReq = get(TAX_ITEM);
		ClientTAXItem taxItem = context.getSelection(TAX_ITEMS);
		if (taxItem != null) {
			taxItemReq.setValue(taxItem);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!taxItemReq.isDone()) {
			return getTaxItemResult(context);
		}
		return null;
	}

	protected Result getTaxItemResult(Context context) {
		Result result = context.makeResult();
		ResultList taxItemsList = new ResultList(TAX_ITEMS);

		Object last = context.getLast(RequirementType.TAXITEM_GROUP);
		if (last != null) {
			taxItemsList.add(createTaxItemRecord((ClientTAXItem) last));
		}

		List<ClientTAXItem> taxItems = getTaxItems();
		for (int i = 0; i < VALUES_TO_SHOW && i < taxItems.size(); i++) {
			ClientTAXItem vatItem = taxItems.get(i);
			if (vatItem != last) {
				taxItemsList.add(createTaxItemRecord((ClientTAXItem) vatItem));
			}
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

	protected Record createAccountRecord(ClientAccount account) {
		Record record = new Record(account);
		record.add("Number", account.getNumber());
		record.add("Name", account.getName());
		record.add("Type", Utility.getAccountTypeString(account.getType()));
		return record;
	}

	protected Result accountRequirement(Context context, String name) {
		Requirement accountReq = get(name);
		ClientAccount account = context.getSelection(ACCOUNTS);
		if (account != null) {
			accountReq.setValue(account);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!accountReq.isDone()) {
			return getAccountResult(context);
		}
		return null;
	}

	protected Result getAccountResult(Context context) {
		Result result = context.makeResult();
		ResultList accountsList = new ResultList(ACCOUNTS);

		Object last = context.getLast(RequirementType.ACCOUNT);
		if (last != null) {
			accountsList.add(createAccountRecord((ClientAccount) last));
		}

		List<ClientAccount> accounts = getAccounts();
		for (int i = 0; i < VALUES_TO_SHOW && i < accounts.size(); i++) {
			ClientAccount salesAccount = accounts.get(i);
			if (salesAccount != last) {
				accountsList
						.add(createAccountRecord((ClientAccount) salesAccount));
			}
		}

		int size = accountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Please Select the Account");
		}

		CommandList commandList = new CommandList();
		commandList.add("create");

		result.add(message.toString());
		result.add(accountsList);
		result.add(commandList);
		result.add("Select the Account");

		return result;
	}

	protected List<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> accounts = getClientCompany().getAccounts();
		return Utility.filteredList(new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				return e.getIsActive();
			}
		}, new ArrayList<ClientAccount>(accounts));
	}

	protected Result amountRequirement(Context context) {

		Requirement taxRateReq = get(AMOUNT);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(AMOUNT)) {
			double amount = Double.parseDouble(context.getNumber());
			taxRateReq.setValue(amount);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!taxRateReq.isDone()) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return amount(context, "Please Enter the Amount", null);
		}

		return null;
	}

	protected int getCompanyType(Context context) {
		return context.getCompany().getAccountingType();
	}

}
