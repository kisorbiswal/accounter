package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ListRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;

public class CreateFullCompanyCommand extends AbstractCompanyCommad {

	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
	private static final String TRACK_TAX = "trackTax";
	private static final String CREATE_ESTIMATES = "createestimates";
	private static final String SELECT_CURRENCY = "selectcurrency";
	private static final String MANAGE_BILLS_OWE = "managebills";
	private static final String STATES = "statesList";
	private static final String TIME_ZONES = "timezoneslist";
	private static final String CUSTOMER_TERMINOLOGIES = "customerterms";
	private static final String SUPPLIER_TERMINOLOGIES = "supplierterms";
	private static final String ACCOUNT_TERMINOLOGIES = "accountterms";
	private static final String SERVICE_PRODUCTS = "serproboth";
	private static final String TRACK_TAX_PAD = "tracktaxpad";
	private static final String ONE_PER_TRANSACTION = "onepertrans";
	private static final String ACCOUNTS_LIST = "accountslist";
	private static final String CURRENCIES = "selectcurrencies";

	private static final String VALUES = "values";

	List<AccountsTemplate> allAccounts = new ArrayList<AccountsTemplate>();

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		list.add(new NameRequirement(COMPANY_NAME, "Enter Company Name",
				"Company Name", false, true));

		list.add(new NameRequirement(LEGAL_NAME, "Enter Legal Name",
				"Legal Name", true, true));

		list.add(new NameRequirement(TAX_ID, "Enter Tax Id", "Tax Id", true,
				true));

		list.add(new CountryRequirement(COUNTRY, "Enter Country Name",
				"Country", true, true, null));

		list.add(new NameRequirement(STATE, "Enter State", "State", true, true));

		list.add(new NameRequirement(ADDRESS1, "Enter Address1", "Address1",
				true, true));

		list.add(new NameRequirement(ADDRESS1, "Enter Address2", "Address2",
				true, true));

		list.add(new NameRequirement(CITY, "Enter City", "City", true, true));

		list.add(new NameRequirement(ZIPCODE, "Enter Zipcode", "Zipcode", true,
				true));

		list.add(new NameRequirement(PHONE, "Enter Phone", "Phone", true, true));

		list.add(new NameRequirement(FAX, "Enter Fax", "Fax", true, true));

		list.add(new NameRequirement(EMAIL, "Enter Email", "Email", true, true));

		list.add(new NameRequirement(WEB_SITE, "Enter Web Site name",
				"Web Site", true, true));

		// TODO list.add(new Requirement(TIME_ZONE, true, true));

		// Second Page
		// TODO list.add(new Requirement(INDUSTRY, false, true));
		list.add(new ListRequirement<String>(ORGANIZATION_REFER,
				"Enter Organization name", "Company Organization", true, true,
				null) {

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add("", value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected String getCreateCommandString() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return "How is your company organized?";
			}

			@Override
			protected List<String> getLists(Context context, String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new ListRequirement<String>(CUSTOMER_TERMINOLOGY,
				"Enter Customer terminology", "Customer Terminology", true,
				true, null) {

			@Override
			protected Record createRecord(String value) {
				Record record = new Record(value);
				record.add("", value);
				return record;
			}

			@Override
			protected String getDisplayValue(String value) {
				return value;
			}

			@Override
			protected String getCreateCommandString() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return "Select Customer Terminology";
			}

			@Override
			protected List<String> getLists(Context context, String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new Requirement(SUPPLIER_TERMINOLOGY, true, true));
		list.add(new Requirement(ACCOUNT_TERMINOLOGY, true, true));
		list.add(new Requirement(SERVICE_PRODUCTS_BOTH, true, true));
		list.add(new Requirement(TRACK_TAX, true, true));
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

		Result makeResult = context.makeResult();
		makeResult
				.add("Company is ready to be created with following details:");
		ResultList list = new ResultList(VALUES);
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		Result result = nameRequirement(context, list, COMPANY_NAME,
				getConstants().companyName(),
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

	private void createCompany(Context context) throws AccounterException {
		String companyName = get(COMPANY_NAME).getValue();
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

	private Result createOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			// case FINISH_FIRST_PAGE:
			// context.removeAttribute(INPUT_ATTR);
			// return null;
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
		get(SERVICE_PRODUCTS_BOTH).setDefaultValue(1);
		get(TRACK_TAX).setDefaultValue(false);
		get(ONE_PER_TRANSACTION).setDefaultValue(true);
		get(TRACK_TAX_PAD).setDefaultValue(false);
		get(CREATE_ESTIMATES).setDefaultValue(false);
		get(SELECT_CURRENCY).setDefaultValue(new ClientCurrency());
		get(MANAGE_BILLS_OWE).setDefaultValue(false);
		get(FISCAL_YEAR).setDefaultValue("January");
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Services Only");
		arrayList.add("Products Only");
		arrayList.add("Both");
		return arrayList;
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

	private List<TemplateAccount> getDefaultAccounts() {
		List<TemplateAccount> templateAc = new ArrayList<TemplateAccount>();
		for (AccountsTemplate template : allAccounts) {
			List<TemplateAccount> accounts2 = template.getAccounts();
			templateAc.addAll(accounts2);
		}
		return templateAc;
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

	private List<ClientCurrency> getCurrenciesList() {
		return CoreUtils.getCurrencies();
	}

}
