package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
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
		TAXAgency taxAgency = context.getSelection(TAX_AGENCIES);
		if (taxAgency != null) {
			taxAgencyReq.setValue(taxAgency);
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
			taxAgenciesList.add(createTaxAgencyRecord((TAXAgency) last));
		}

		List<TAXAgency> taxAgencies = getTaxAgencies(context);
		for (int i = 0; i < VALUES_TO_SHOW && i < taxAgencies.size(); i++) {
			TAXAgency taxAgency = taxAgencies.get(i);
			if (taxAgency != last) {
				taxAgenciesList
						.add(createTaxAgencyRecord((TAXAgency) taxAgency));
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

	protected List<TAXAgency> getTaxAgencies(Context context) {
		Company company = context.getCompany();
		Set<TAXAgency> taxAgencies = company.getTaxAgencies();
		return new ArrayList<TAXAgency>(taxAgencies);
	}

	protected Record createTaxAgencyRecord(TAXAgency taxAgency) {
		Record record = new Record(taxAgency);
		record.add("Name", taxAgency.getName());
		return record;
	}

	protected Result taxItemRequirement(Context context) {
		Requirement taxItemReq = get(TAX_ITEM);
		TAXItem taxItem = context.getSelection(TAX_ITEMS);
		if (taxItem != null) {
			taxItemReq.setValue(taxItem);
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
			taxItemsList.add(createTaxItemRecord((TAXItem) last));
		}

		List<TAXItem> taxItems = getTaxItems(context.getHibernateSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < taxItems.size(); i++) {
			TAXItem vatItem = taxItems.get(i);
			if (vatItem != last) {
				taxItemsList.add(createTaxItemRecord((TAXItem) vatItem));
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

	protected List<TAXItem> getTaxItems(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Record createTaxItemRecord(TAXItem taxItem) {
		Record record = new Record(taxItem);
		record.add("Name", taxItem.getName());
		return record;
	}

	protected Record createAccountRecord(Account account) {
		Record record = new Record(account);
		record.add("Number", account.getNumber());
		record.add("Name", account.getName());
		record.add("Type", Utility.getAccountTypeString(account.getType()));
		return record;
	}

	protected Result accountRequirement(Context context, String string) {
		Requirement accountReq = get(string);
		Account account = context.getSelection(ACCOUNTS);
		if (account != null) {
			accountReq.setValue(account);
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
			accountsList.add(createAccountRecord((Account) last));
		}

		List<Account> accounts = getAccounts(context.getHibernateSession());
		for (int i = 0; i < VALUES_TO_SHOW || i < accounts.size(); i++) {
			Account salesAccount = accounts.get(i);
			if (salesAccount != last) {
				accountsList.add(createAccountRecord((Account) salesAccount));
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

	protected List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Result amountRequirement(Context context) {

		Requirement taxRateReq = get(AMOUNT);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(AMOUNT)) {
			double amount = context.getInteger();
			taxRateReq.setValue(amount);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!taxRateReq.isDone()) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return amount(context, "Please Enter the Tax Rate.", null);
		}

		return null;
	}

	protected Result nameRequirement(Context context) {
		Requirement nameReq = get(NAME);
		String input = (String) context.getAttribute("input");
		if (input != null && input.equals(NAME)) {
			input = context.getString();
			nameReq.setValue(input);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!nameReq.isDone()) {
			context.setAttribute(INPUT_ATTR, NAME);
			return text(context, "Please Enter the Name.", null);
		}

		return null;
	}

	protected int getCompanyType(Context context) {
		return context.getCompany().getAccountingType();
	}

}
