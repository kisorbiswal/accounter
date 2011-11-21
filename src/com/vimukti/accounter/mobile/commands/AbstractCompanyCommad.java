package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.server.AccountsTemplateManager;

public abstract class AbstractCompanyCommad extends NewAbstractCommand {
	protected static final String CUSTOMER_TERMINOLOGY = "customerterm";
	protected static final String SUPPLIER_TERMINOLOGY = "vendorterm";
	protected static final String ACCOUNT_TERMINOLOGY = "accountterm";

	protected static final String COMPANY_NAME = "companyName";
	protected static final String LEGAL_NAME = "legalName";
	protected static final String TAX_ID = "taxId";
	protected static final String STATE = "state";
	protected static final String FAX = "fax";
	protected static final String WEB_SITE = "website";
	protected static final String TIME_ZONE = "timezone";
	protected static final String VALUES = "values";
	protected static final String STATES = "statesList";
	protected static final String ADDRESS1 = "address1";
	protected static final String ADDRESS2 = "address2";
	protected static final String CITY = "city";
	protected static final String ZIPCODE = "zipcode";
	protected static final String TIME_ZONES = "timezoneslist";
	protected static final String INDUSTRY = "industry";
	protected static final String ORGANIZATION_REFER = "organizationrefer";
	protected static final String FISCAL_YEAR = "fiscalyear";
	protected static final String INDUSTRIES = "industrieslist";
	protected static final String ACCOUNTS = "defaultaccounts";
	protected static final String ORGANIZATION_TYPES = "orgtypes";
	protected static final String FISCAL_YEAR_LIST = "fiscalyearsList";

	List<AccountsTemplate> allAccounts = new ArrayList<AccountsTemplate>();

	protected Result numberListRequirement(Context context, String name,
			List<String> displayList, String selectionName,
			Requirement requirement, ResultList list) {
		Integer input = (Integer) context.getSelection(selectionName);
		if (input != null) {
			requirement.setValue(input + 1);
		}
		Object selection = context.getSelection("values");
		if (!requirement.isDone()) {
			return showList(context, name, null, displayList, selectionName,
					requirement);
		} else {
			if (requirement.getName() == INDUSTRY) {
				get(ACCOUNTS).setDefaultValue(
						getDefaultTemplateAccounts((Integer) get(INDUSTRY)
								.getValue()));
			}
		}

		Integer requirementvalue = requirement.getValue();
		if (selection != null && selection.equals(requirement.getName())) {
			return showList(context, name, requirementvalue, displayList,
					selectionName, requirement);
		}

		Record nameRecord = new Record(requirement.getName());
		nameRecord.add("", name);
		nameRecord.add("", getNameValue(requirement));
		list.add(nameRecord);
		return null;
	}

	protected Result listRequirement(Context context, String name,
			List<String> displayList, String selectionName,
			Requirement requirement, ResultList list) {
		String input = (String) context.getSelection(selectionName);
		if (input != null) {
			requirement.setValue(input);
		}
		Object selection = context.getSelection("values");
		if (!requirement.isDone()) {
			return showList(context, name, null, displayList, selectionName,
					requirement);
		} else {
			if (requirement.getName() == INDUSTRY) {
				get(ACCOUNTS).setDefaultValue(
						getDefaultTemplateAccounts((Integer) get(INDUSTRY)
								.getValue()));
			}
		}

		String requirementvalue = requirement.getValue();
		if (selection != null && selection.equals(requirement.getName())) {
			return showList(context, name, requirementvalue, displayList,
					selectionName, requirement);
		}

		Record nameRecord = new Record(requirement.getName());
		nameRecord.add("", name);
		nameRecord.add("", requirementvalue);
		list.add(nameRecord);
		return null;
	}

	protected Result emailRequirement(Context context, ResultList list,
			String reqName, String name, String displayString) {
		// Requirement requirement = get(reqName);
		// String input = (String) context.getAttribute(INPUT_ATTR);
		// if (input.equals(reqName)) {
		// input = context.getString();
		// if (isValidEmail(input)) {
		// requirement.setValue(input);
		// context.setAttribute(INPUT_ATTR, "");
		// }
		// }
		// if (!requirement.isDone()) {
		// context.setAttribute(INPUT_ATTR, reqName);
		// return text(context, displayString, null);
		// }
		//
		// Object selection = context.getSelection("values");
		// String emailId = requirement.getValue();
		// if (selection != null && selection.equals(reqName)) {
		// context.setAttribute(INPUT_ATTR, reqName);
		// return text(context, displayString, emailId);
		// }
		//
		// Record nameRecord = new Record(reqName);
		// nameRecord.add("", name);
		// nameRecord.add("", emailId);
		// list.add(nameRecord);
		return null;
	}

	protected List<TemplateAccount> getDefaultTemplateAccounts(int industryType) {
		// Integer industryType = get(INDUSTRY).getValue();
		AccountsTemplate accountsTemplate = allAccounts.get(industryType);
		List<TemplateAccount> accounts = accountsTemplate.getAccounts();
		if (accounts != null) {
			return accounts;
		}
		return Collections.emptyList();
	}

	private String getNameValue(Requirement req) {
		String reqName = req.getName();
		Integer value = req.getValue();
		if (value == null) {
			return null;
		}
		value = value - 1;
		if (reqName == INDUSTRY) {
			return getIndustryList().get(value);
		} else if (reqName == CUSTOMER_TERMINOLOGY) {
			return getCustomerTerminologies().get(value);
		} else if (reqName == SUPPLIER_TERMINOLOGY) {
			return getSupplierTerminologies().get(value);
		} else if (reqName == ACCOUNT_TERMINOLOGY) {
			return getAccountTerminologies().get(value);
		} else if (reqName == ORGANIZATION_REFER) {
			return getOrganizationTypes().get(value);
		}
		return null;
	}

	protected List<String> getIndustryList() {
		List<String> industryList = new ArrayList<String>();
		if (allAccounts.size() == 0) {
			AccountsTemplateManager manager = new AccountsTemplateManager();
			try {
				allAccounts = manager.loadAccounts(ServerLocal.get()
						.getLanguage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (AccountsTemplate template : allAccounts) {
			industryList.add(template.getName());
		}
		return industryList;
	}

	protected Result showList(Context context, String displayString,
			Object olaCountryName, List<String> displayList,
			String selectionName, Requirement requirement) {
		ResultList list = new ResultList(selectionName);
		Result result = context.makeResult();
		result.add(list);

		for (int num = 0; num < displayList.size(); num++) {
			Record record = new Record(num);
			record.add("", displayList.get(num));
			list.add(record);
			// if (num == COUNTRIES_TO_SHOW) {
			// break;
			// }
		}
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(displayString));
		}

		return result;
	}

	protected List<String> getFiscalYearMonths() {
		String[] names = new String[] { getMessages().january(),
				getMessages().february(), getMessages().march(),
				getMessages().april(), getMessages().may(),
				getMessages().june(), getMessages().july(),
				getMessages().august(), getMessages().september(),
				getMessages().october(), getMessages().november(),
				getMessages().december() };
		List<String> fiscalYearMonths = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			fiscalYearMonths.add(names[i]);
		}
		return fiscalYearMonths;
	}

	protected List<String> getCustomerTerminologies() {
		List<String> customerTerms = new ArrayList<String>();
		customerTerms.add(getMessages().Customer());
		customerTerms.add(getMessages().Client());
		customerTerms.add(getMessages().Tenant());
		customerTerms.add(getMessages().Donar());
		customerTerms.add(getMessages().Guest());
		customerTerms.add(getMessages().Member());
		customerTerms.add(getMessages().Patient());
		return customerTerms;
	}

	protected List<String> getSupplierTerminologies() {
		List<String> supplierTerms = new ArrayList<String>();
		supplierTerms.add(getMessages().Supplier());
		supplierTerms.add(getMessages().Vendor());
		return supplierTerms;
	}

	protected List<String> getAccountTerminologies() {
		List<String> accountTerms = new ArrayList<String>();
		accountTerms.add(getMessages().Account());
		accountTerms.add(getMessages().Ledger());
		accountTerms.add(getMessages().Category());
		return accountTerms;
	}

	protected List<String> getOrganizationTypes() {
		List<String> orgTypes = new ArrayList<String>();
		orgTypes.add(getMessages().soleProprietorship());
		orgTypes.add(getMessages().partnershipOrLLP());
		orgTypes.add(getMessages().soleProprietorship());
		orgTypes.add(getMessages().partnershipOrLLP());
		orgTypes.add(getMessages().LLC());
		orgTypes.add(getMessages().corporation());
		orgTypes.add(getMessages().sCorporation());
		orgTypes.add(getMessages().nonProfit());
		orgTypes.add(getMessages().otherNone());
		return orgTypes;
	}

	protected List<String> getTimeZonesList() {
		return CoreUtils.getTimeZonesAsList();
	}

	protected List<String> getCountryList() {
		return CoreUtils.getCountriesAsList();
	}

	protected List<String> getStatesList(String country) {
		// String country = get(COUNTRY).getValue();
		List<String> statesList = new ArrayList<String>();
		if (country == null) {
			return statesList;
		}
		String[] statesForCountry = CoreUtils.getStatesForCountry(country);
		for (int i = 0; i < statesForCountry.length; i++) {
			statesList.add(statesForCountry[i]);
		}
		return statesList;
	}

	protected String getDefaultTzOffsetStr() {
		return "UTC+5:30 Asia/Calcutta";
	}

	protected void setStartDateOfFiscalYear(ClientCompanyPreferences preferences) {
		ClientFinanceDate currentDate = new ClientFinanceDate();
		int fiscalYearFirstMonth = preferences.getFiscalYearFirstMonth();
		ClientFinanceDate fiscalYearStartDate = new ClientFinanceDate(
				(int) currentDate.getYear(), fiscalYearFirstMonth, 1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(fiscalYearStartDate.getDateAsObject());
		endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 11);
		endCal.set(Calendar.DATE,
				endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		ClientFinanceDate fiscalYearEndDate = new ClientFinanceDate(
				endCal.getTime());

		preferences.setStartOfFiscalYear(fiscalYearStartDate.getDate());
		preferences.setEndOfFiscalYear(fiscalYearEndDate);
		preferences.setDepreciationStartDate(currentDate.getDate());
	}

	protected List<ClientCurrency> getCurrenciesList() {
		return CoreUtils.getCurrencies(new ArrayList<ClientCurrency>());
	}
}
