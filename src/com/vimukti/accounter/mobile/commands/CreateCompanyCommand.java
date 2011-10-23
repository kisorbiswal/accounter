package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;
import com.vimukti.accounter.web.server.AccountsTemplateManager;

public class CreateCompanyCommand extends AbstractCommand {

	private static final String NAME = "companyname";
	private static final String LEGAL_NAME = "legalName";
	private static final String TAX_ID = "taxId";
	private static final String STATE = "state";
	private static final String FAX = "fax";
	private static final String WEB_SITE = "website";
	private static final String TIME_ZONE = "timezone";
	private static final String VALUES = "values";
	private static final String STATES = "statesList";
	private static final String ADDRESS1 = "address1";
	private static final String ADDRESS2 = "address1";
	private static final String CITY = "city";
	private static final String ZIPCODE = "zipcode";
	private static final String TIME_ZONES = "timezoneslist";
	private static final String INDUSTRY = "industry";
	private static final String INDUSTRIES = "industrieslist";
	private static final String ORGANIZATION_REFER = "organizationrefer";
	private static final String ORGANIZATION_TYPES = "orgtypes";
	private static final String CUSTOMER_TERMINOLOGY = "customerterm";
	private static final String SUPPLIER_TERMINOLOGY = "vendorterm";
	private static final String ACCOUNT_TERMINOLOGY = "accountterm";
	private static final String ACCOUNT_TERMINOLOGIES = "accountterms";
	private static final String SUPPLIER_TERMINOLOGIES = "supplierterms";
	private static final String CUSTOMER_TERMINOLOGIES = "customerterms";
	private static final String SERVICE_PRODUCTS = "serproboth";
	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
	private static final String TRACK_TAX = "trackTax";
	private static final String TRACK_TAX_PAD = "tracktaxpad";
	private static final String ONE_PER_TRANSACTION = "onepertrans";
	private static final String CREATE_ESTIMATES = "createestimates";
	private static final String SELECT_CURRENCY = "selectcurrency";
	private static final String CURRENCIES = "selectcurrencies";
	private static final String MANAGE_BILLS_OWE = "managebills";
	private static final String FISCAL_YEAR = "fiscalyear";
	private static final String FISCAL_YEAR_LIST = "fiscalyearsList";
	private static final String ACCOUNTS = "defaultaccounts";
	private static final String ACCOUNTS_LIST = "accountslist";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(LEGAL_NAME, true, true));
		list.add(new Requirement(TAX_ID, true, true));
		list.add(new Requirement(COUNTRY, true, true));
		list.add(new Requirement(STATE, true, true));
		list.add(new Requirement(ADDRESS1, true, true));
		list.add(new Requirement(ADDRESS2, true, true));
		list.add(new Requirement(CITY, true, true));
		list.add(new Requirement(ZIPCODE, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEB_SITE, true, true));
		list.add(new Requirement(TIME_ZONE, true, true));
		list.add(new Requirement(INDUSTRY, false, true));
		list.add(new Requirement(ORGANIZATION_REFER, true, true));
		list.add(new Requirement(CUSTOMER_TERMINOLOGY, true, true));
		list.add(new Requirement(SUPPLIER_TERMINOLOGY, true, true));
		list.add(new Requirement(ACCOUNT_TERMINOLOGY, true, true));
		list.add(new Requirement(SERVICE_PRODUCTS, true, true));
		list.add(new Requirement(TRACK_TAX, true, true));
		list.add(new Requirement(ONE_PER_TRANSACTION, true, true));
		list.add(new Requirement(TRACK_TAX_PAD, true, true));
		list.add(new Requirement(CREATE_ESTIMATES, true, true));
		list.add(new Requirement(SELECT_CURRENCY, true, true));
		list.add(new Requirement(MANAGE_BILLS_OWE, true, true));
		list.add(new Requirement(FISCAL_YEAR, true, true));
		list.add(new Requirement(ACCOUNTS, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().companyPreferences()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		result = nameRequirement(context, list, NAME, getConstants()
				.companyName(),
				getMessages().pleaseEnter(getConstants().companyName()));
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context, getConstants().industry(),
				getIndustryList(), INDUSTRIES, get(INDUSTRY), list);
		if (result != null) {
			return result;
		}
		result = createOptionalRequirement(context, list, actions, makeResult);
		makeResult.add(actions);
		if (result != null) {
			return result;
		}
		try {
			createCompany(context);
			markDone();
		} catch (AccounterException e) {
			e.printStackTrace();
			return new Result(e.getMessage());

		}
		result = new Result(getMessages().createSuccessfully(
				getConstants().companyPreferences()));
		return result;
	}

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
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
		selection = context.getSelection(VALUES);

		Result result = nameRequirement(context, list, LEGAL_NAME,
				getConstants().legalCompanyName(),
				getMessages().pleaseEnter(getConstants().legalCompanyName()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, TAX_ID, getConstants().taxId(),
				getMessages().pleaseEnter(getConstants().taxId()));
		if (result != null) {
			return result;
		}
		result = listRequirement(context, getConstants().state(),
				getStatesList(), COUNTRIES, get(COUNTRY), list);
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().state(),
				getCountryList(), STATES, get(STATE), list);
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, ADDRESS1, getConstants()
				.address1(),
				getMessages().pleaseEnter(getConstants().address1()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, ADDRESS2, getConstants()
				.address2(),
				getMessages().pleaseEnter(getConstants().address2()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, CITY, getConstants().city(),
				getMessages().pleaseEnter(getConstants().city()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, ZIPCODE, getConstants()
				.zipCode(), getMessages().pleaseEnter(getConstants().zipCode()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, PHONE, getConstants()
				.phoneNumber(),
				getMessages().pleaseEnter(getConstants().phoneNumber()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, FAX,
				getConstants().faxNumber(),
				getMessages().pleaseEnter(getConstants().faxNumber()));
		if (result != null) {
			return result;
		}

		result = emailRequirement(context, list, EMAIL, getConstants().email(),
				getMessages().pleaseEnter(getConstants().email()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(
				context,
				list,
				WEB_SITE,
				getConstants().webSite(),
				getMessages().pleaseEnter(
						getMessages().name(getConstants().webSite())));
		if (result != null) {
			return result;
		}

		result = listRequirement(context, getConstants().timezone(),
				getTimeZonesList(), TIME_ZONES, get(TIME_ZONE), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context, getConstants().organisation(),
				getOrganizationTypes(), ORGANIZATION_TYPES,
				get(ORGANIZATION_REFER), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context,
				getMessages().terminology(getConstants().Customer()),
				getCustomerTerminologies(), CUSTOMER_TERMINOLOGIES,
				get(CUSTOMER_TERMINOLOGY), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context,
				getMessages().terminology(getConstants().Supplier()),
				getSupplierTerminologies(), SUPPLIER_TERMINOLOGIES,
				get(SUPPLIER_TERMINOLOGY), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context,
				getMessages().terminology(getConstants().account()),
				getAccountTerminologies(), ACCOUNT_TERMINOLOGIES,
				get(ACCOUNT_TERMINOLOGY), list);
		if (result != null) {
			return result;
		}

		result = numberListRequirement(context, "Services/Products",
				getServiceProductBothList(), SERVICE_PRODUCTS_BOTH,
				get(SERVICE_PRODUCTS), list);
		if (result != null) {
			return result;
		}
		selection = context.getSelection(VALUES);
		booleanOptionalRequirement(context, selection, list, TRACK_TAX,
				"Track Tax Enabled", "Track Tax Disabled");

		Boolean isEnabled = get(TRACK_TAX).getValue();
		if (isEnabled) {
			booleanOptionalRequirement(context, selection, list,
					ONE_PER_TRANSACTION, "One per transaction",
					"One per detail line");
			booleanOptionalRequirement(context, selection, list, TRACK_TAX_PAD,
					"Enabled Tracking Tax Paid", "Disabled Tracking Tax Paid");
		}

		booleanOptionalRequirement(context, selection, list, CREATE_ESTIMATES,
				"Yes,want to create estimates",
				"No,don't want to create estimates");

		result = currencyRequirement(context, list);
		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, selection, list, MANAGE_BILLS_OWE,
				"Manage Bills", "Don't Manage Bills");

		result = listRequirement(context, getConstants().fiscalYear(),
				getFiscalYearMonths(), FISCAL_YEAR_LIST, get(FISCAL_YEAR), list);
		if (result != null) {
			return result;
		}

		if (get(INDUSTRY).isDone()) {
			result = defaultAccountsRequirement(context, actions, makeResult);
			if (result != null) {
				return result;
			}
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add(
				"",
				getMessages().finishToCreate(
						getConstants().companyPreferences()));
		actions.add(finish);
		return makeResult;
	}

	private Result numberListRequirement(Context context, String name,
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
				get(ACCOUNTS).setDefaultValue(getDefaultTemplateAccounts());
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

	private Result currencyRequirement(Context context, ResultList list) {
		Requirement requirement = get(SELECT_CURRENCY);
		ClientCurrency input = (ClientCurrency) context
				.getSelection(CURRENCIES);
		if (input != null) {
			requirement.setValue(input);
		}
		if (!requirement.isDone()) {
			return showCurrenciesList(context);
		}

		Object selection = context.getSelection("values");
		ClientCurrency requirementvalue = requirement.getValue();
		if (selection != null && selection.equals(requirementvalue)) {
			return showCurrenciesList(context);
		}

		Record nameRecord = new Record(requirementvalue);
		nameRecord.add("", getConstants().currency());
		nameRecord.add("", requirementvalue.getFormalName());
		nameRecord.add("", requirementvalue.getDisplayName());
		list.add(nameRecord);
		return null;
	}

	private Result showCurrenciesList(Context context) {
		Requirement requirement = get(SELECT_CURRENCY);
		Result makeResult = context.makeResult();
		ResultList list = new ResultList(CURRENCIES);
		int num = 0;
		ClientCurrency countryValue = requirement.getValue();
		for (ClientCurrency str : getCurrenciesList()) {
			if (!str.equals(countryValue)) {
				Record record = new Record(str);
				record.add("", str.getFormalName());
				record.add("", str.getDisplayName());
				list.add(record);
				num++;
			}
			if (num == COUNTRIES_TO_SHOW) {
				break;
			}
		}
		if (list.size() > 0) {
			makeResult.add(getMessages()
					.pleaseSelect(getConstants().currency()));
		}
		makeResult.add(list);
		return makeResult;
	}

	private Result defaultAccountsRequirement(Context context,
			ResultList actions, Result makeResult) {
		Requirement accountsReq = get(ACCOUNTS);
		List<TemplateAccount> accountsReqValue = accountsReq.getValue();
		if (accountsReqValue == null) {
			accountsReqValue = new ArrayList<TemplateAccount>();
		}
		List<TemplateAccount> accounts = context.getSelections(ACCOUNTS);
		if (accounts != null && accounts.size() > 0) {
			for (TemplateAccount account : accounts) {
				accountsReqValue.add(account);
			}
		}

		Object selection = context.getSelection(ACCOUNTS_LIST);
		if (selection != null) {
			accountsReqValue.remove(selection);
		}

		if (accountsReqValue.size() == 0) {
			return accounts(context);
		}
		selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName != null && actionName == ActionNames.ADD_MORE_ACCOUNTS) {
			return accounts(context);
		}
		makeResult.add(getConstants().Accounts());
		ResultList itemsList = new ResultList(ACCOUNTS_LIST);
		for (TemplateAccount account : accountsReqValue) {
			Record itemRec = new Record(account);
			itemRec.add("", account.getName());
			itemRec.add("", account.getType());
			itemsList.add(itemRec);
		}
		makeResult.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreItems.add("", getMessages().addMore(getConstants().Accounts()));
		actions.add(moreItems);
		return null;
	}

	private Result accounts(Context context) {
		Result result = context.makeResult();
		List<TemplateAccount> defaultAcc = getDefaultAccounts();
		ResultList list = new ResultList(ACCOUNTS);
		int num = 0;
		Requirement defaultAccReq = get(ACCOUNTS);
		List<TemplateAccount> reqDefaultAccs = defaultAccReq.getValue();
		if (reqDefaultAccs == null) {
			reqDefaultAccs = new ArrayList<TemplateAccount>();
		}
		List<TemplateAccount> availableaccs = new ArrayList<TemplateAccount>();
		for (TemplateAccount templateacc : reqDefaultAccs) {
			availableaccs.add(templateacc);
		}
		for (TemplateAccount account : defaultAcc) {
			if (!availableaccs.contains(account)) {
				list.add(creatTemplateAccountRecord(account));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(getConstants().item()));
		}
		result.add(list);
		return result;
	}

	private Record creatTemplateAccountRecord(TemplateAccount account) {
		Record itemRec = new Record(account);
		itemRec.add("", account.getName());
		itemRec.add("", account.getType());
		return itemRec;
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Services Only");
		arrayList.add("Products Only");
		arrayList.add("Both");
		return arrayList;
	}

	private Result listRequirement(Context context, String name,
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
				get(ACCOUNTS).setDefaultValue(getDefaultTemplateAccounts());
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

	private Result showList(Context context, String displayString,
			Object olaCountryName, List<String> displayList,
			String selectionName, Requirement requirement) {
		ResultList list = new ResultList(selectionName);
		Result result = context.makeResult();
		result.add(list);

		for (int num = 0; num < displayList.size(); num++) {
			Record record = new Record(num);
			record.add("", displayList.get(num));
			list.add(record);
			if (num == COUNTRIES_TO_SHOW) {
				break;
			}
		}
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(displayString));
		}

		return result;
	}

	private String getDefaultTzOffsetStr() {
		return "UTC+5:30 Asia/Calcutta";
	}

	private void setDefaultValues() {
		get(LEGAL_NAME).setDefaultValue("");
		get(TAX_ID).setDefaultValue("");
		get(COUNTRY).setDefaultValue("United Kingdom");
		get(STATE).setDefaultValue("Buckinghamshire");
		get(PHONE).setDefaultValue("");
		get(FAX).setDefaultValue("");
		get(EMAIL).setDefaultValue("");
		get(WEB_SITE).setDefaultValue("");
		get(TIME_ZONE).setDefaultValue(getDefaultTzOffsetStr());
		get(ADDRESS1).setDefaultValue("");
		get(ADDRESS2).setDefaultValue("");
		get(CITY).setDefaultValue("");
		get(ZIPCODE).setDefaultValue("");
		get(ORGANIZATION_REFER).setDefaultValue(1);
		get(CUSTOMER_TERMINOLOGY).setDefaultValue(1);
		get(SUPPLIER_TERMINOLOGY).setDefaultValue(1);
		get(ACCOUNT_TERMINOLOGY).setDefaultValue(1);
		get(SERVICE_PRODUCTS).setDefaultValue(1);
		get(TRACK_TAX).setDefaultValue(false);
		get(ONE_PER_TRANSACTION).setDefaultValue(true);
		get(TRACK_TAX_PAD).setDefaultValue(false);
		get(CREATE_ESTIMATES).setDefaultValue(false);
		get(SELECT_CURRENCY).setDefaultValue(new ClientCurrency());
		get(MANAGE_BILLS_OWE).setDefaultValue(false);
		get(FISCAL_YEAR).setDefaultValue("January");
	}

	private Result emailRequirement(Context context, ResultList list,
			String reqName, String name, String displayString) {
		Requirement requirement = get(reqName);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(reqName)) {
			input = context.getString();
			if (isValidEmail(input)) {
				requirement.setValue(input);
				context.setAttribute(INPUT_ATTR, "");
			}
		}
		if (!requirement.isDone()) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, null);
		}

		Object selection = context.getSelection("values");
		String emailId = requirement.getValue();
		if (selection != null && selection.equals(reqName)) {
			context.setAttribute(INPUT_ATTR, reqName);
			return text(context, displayString, emailId);
		}

		Record nameRecord = new Record(reqName);
		nameRecord.add("", name);
		nameRecord.add("", emailId);
		list.add(nameRecord);
		return null;
	}

	private List<String> getStatesList() {
		String country = get(COUNTRY).getValue();
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

	private List<String> getCountryList() {
		return CoreUtils.getCountriesAsList();
	}

	List<AccountsTemplate> allAccounts = new ArrayList<AccountsTemplate>();
	HashMap<String, Integer> industryMap = new HashMap<String, Integer>();

	private List<String> getIndustryList() {
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

	private void createCompany(Context context) throws AccounterException {
		String companyName = get(NAME).getValue();
		Integer industryType = get(INDUSTRY).getValue();
		String legalName = get(LEGAL_NAME).getValue();
		String taxId = get(TAX_ID).getValue();
		String countryName = get(COUNTRY).getValue();
		String state = get(STATE).getValue();
		String phoneNum = get(PHONE).getValue();
		String fax = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webSite = get(WEB_SITE).getValue();
		String timeZone = get(TIME_ZONE).getValue();
		String address1 = get(ADDRESS1).getValue();
		String address2 = get(ADDRESS2).getValue();
		String city = get(CITY).getValue();
		String zipCode = get(ZIPCODE).getValue();
		Boolean trackTax = get(TRACK_TAX).getValue();
		Boolean onePerTrans = get(ONE_PER_TRANSACTION).getValue();
		Boolean trackTaxPad = get(TRACK_TAX_PAD).getValue();
		Boolean createEstimates = get(CREATE_ESTIMATES).getValue();
		ClientCurrency currency = get(SELECT_CURRENCY).getValue();
		Boolean manageBills = get(MANAGE_BILLS_OWE).getValue();
		List<TemplateAccount> accounts = get(ACCOUNTS).getValue();
		ClientCompanyPreferences preferences = new ClientCompanyPreferences();
		preferences.setPrimaryCurrency(currency);

		ICountryPreferences countryPreferences = CountryPreferenceFactory
				.get(countryName);
		if (countryPreferences != null) {
			// preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
			// countryPreferences.getDefaultFiscalYearStartingMonth()));
		}

		ClientAddress address = new ClientAddress();
		preferences.setTradingName(companyName);
		preferences.setLegalName(legalName);
		preferences.setPhone(phoneNum);
		preferences.setCompanyEmail(emailId);
		preferences.setTaxId(taxId);
		preferences.setFax(fax);
		preferences.setWebSite(webSite);
		address.setAddress1(address1);
		address.setStreet(address2);
		address.setCity(city);
		address.setZipOrPostalCode(zipCode);
		address.setStateOrProvinence(state);
		address.setCountryOrRegion(countryName);
		preferences.setTradingAddress(address);
		preferences.setTimezone(timeZone);
		preferences.setIndustryType(industryType);
		Integer organizationRefer = get(ORGANIZATION_REFER).getValue();
		preferences.setOrganizationType(organizationRefer);
		Integer customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		Integer supplierTerm = get(SUPPLIER_TERMINOLOGY).getValue();
		Integer accountTerm = get(ACCOUNT_TERMINOLOGY).getValue();
		preferences.setReferCustomers(customerTerm);
		preferences.setReferVendors(supplierTerm);
		preferences.setReferAccounts(accountTerm);
		Integer servProBoth = get(SERVICE_PRODUCTS).getValue();
		if (servProBoth == 1) {
			preferences.setSellServices(true);
		} else if (servProBoth == 2) {
			preferences.setSellProducts(true);
		} else {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}
		preferences.setTaxTrack(trackTax);
		preferences.setTaxPerDetailLine(onePerTrans);
		preferences.setTrackPaidTax(trackTaxPad);
		preferences.setKeepTrackofBills(manageBills);
		preferences.setDoyouwantEstimates(createEstimates);
		setStartDateOfFiscalYear(preferences);
		AccounterCompanyInitializationServiceImpl.intializeCompany(preferences,
				accounts, context.getIOSession().getClient());
	}

	private void setStartDateOfFiscalYear(ClientCompanyPreferences preferences) {
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

	private List<String> getTimeZonesList() {
		return CoreUtils.getTimeZonesAsList();
	}

	private List<String> getOrganizationTypes() {
		List<String> orgTypes = new ArrayList<String>();
		orgTypes.add(getConstants().soleProprietorship());
		orgTypes.add(getConstants().partnershipOrLLP());
		orgTypes.add(getConstants().soleProprietorship());
		orgTypes.add(getConstants().partnershipOrLLP());
		orgTypes.add(getConstants().LLC());
		orgTypes.add(getConstants().corporation());
		orgTypes.add(getConstants().sCorporation());
		orgTypes.add(getConstants().nonProfit());
		orgTypes.add(getConstants().otherNone());
		return orgTypes;
	}

	private List<String> getAccountTerminologies() {
		List<String> accountTerms = new ArrayList<String>();
		accountTerms.add(getConstants().Account());
		accountTerms.add(getConstants().Ledger());
		accountTerms.add(getConstants().Category());
		return accountTerms;
	}

	private List<String> getSupplierTerminologies() {
		List<String> supplierTerms = new ArrayList<String>();
		supplierTerms.add(getConstants().Supplier());
		supplierTerms.add(getConstants().Vendor());
		return supplierTerms;
	}

	private List<String> getCustomerTerminologies() {
		List<String> customerTerms = new ArrayList<String>();
		customerTerms.add(getConstants().Customer());
		customerTerms.add(getConstants().Client());
		customerTerms.add(getConstants().Tenant());
		customerTerms.add(getConstants().Donar());
		customerTerms.add(getConstants().Guest());
		customerTerms.add(getConstants().Member());
		customerTerms.add(getConstants().Patient());
		return customerTerms;
	}

	private List<ClientCurrency> getCurrenciesList() {
		return CoreUtils.getCurrencies();
	}

	private List<String> getFiscalYearMonths() {
		String[] names = new String[] { getConstants().january(),
				getConstants().february(), getConstants().march(),
				getConstants().april(), getConstants().may(),
				getConstants().june(), getConstants().july(),
				getConstants().august(), getConstants().september(),
				getConstants().october(), getConstants().november(),
				getConstants().december() };
		List<String> fiscalYearMonths = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			fiscalYearMonths.add(names[i]);
		}
		return fiscalYearMonths;
	}

	private List<TemplateAccount> getDefaultAccounts() {
		List<TemplateAccount> templateAc = new ArrayList<TemplateAccount>();
		for (AccountsTemplate template : allAccounts) {
			List<TemplateAccount> accounts2 = template.getAccounts();
			templateAc.addAll(accounts2);
		}
		return templateAc;
	}

	private List<TemplateAccount> getDefaultTemplateAccounts() {
		Integer industryType = get(INDUSTRY).getValue();
		AccountsTemplate accountsTemplate = allAccounts.get(industryType);
		List<TemplateAccount> accounts = accountsTemplate.getAccounts();
		if (accounts != null) {
			return accounts;
		}
		return Collections.emptyList();
	}
}
