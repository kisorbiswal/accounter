package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TemplateAccountRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.TemplateAccount;
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
	private static final String TRACK_TAX_PAD = "tracktaxpad";
	private static final String ONE_PER_TRANSACTION = "onepertrans";

	@Override
	public String getWelcomeMessage() {
		return "Full Company company setup...";
	}

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

		list.add(new CountryRequirement(COUNTRY, getMessages().pleaseSelect(
				getConstants().country()), getConstants().country(), true,
				true, null));

		list.add(new StringListRequirement(STATE, "Enter State", "State", true,
				true, null) {

			@Override
			protected String getSetMessage() {
				return "State has been selected";
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().state());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getStatesList((String) get(COUNTRY).getValue());
			}
		});

		list.add(new NameRequirement(ADDRESS1, "Enter Address1", "Address1",
				true, true));

		list.add(new NameRequirement(ADDRESS1, "Enter Address2", "Address2",
				true, true));

		list.add(new NameRequirement(CITY, "Enter City", "City", true, true));

		list.add(new NameRequirement(ZIPCODE, "Enter Zipcode", "Zipcode", true,
				true));

		list.add(new NameRequirement(PHONE, "Enter Phone", "Phone", true, true));

		list.add(new NameRequirement(FAX, "Enter Fax", "Fax", true, true));

		list.add(new EmailRequirement(EMAIL, "Enter Email", "Email", true, true));

		list.add(new NameRequirement(WEB_SITE, "Enter Web Site name",
				"Web Site", true, true));

		list.add(new StringListRequirement(TIME_ZONE, getMessages()
				.pleaseSelect(getConstants().timezone()), getConstants()
				.timezone(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().timezone());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTimeZonesList();
			}

			@Override
			protected String getSetMessage() {
				return "Time Zone has been selected";
			}
		});

		// Second Page
		list.add(new StringListRequirement(INDUSTRY,
				"Please Enter Industry type", getConstants().industry(), false,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						get(ACCOUNTS).setValue(null);
					}
				}) {
			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().industry());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getIndustryList();
			}

			@Override
			protected String getSetMessage() {
				return "Industry has been selected";
			}

		});

		list.add(new StringListRequirement(ORGANIZATION_REFER,
				"Enter Organization name", "Company Organization", true, true,
				null) {

			@Override
			protected String getSelectString() {
				return "How is your company organized?";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getOrganizationTypes();
			}

			@Override
			protected String getSetMessage() {
				return "Company Organization has been selected";
			}

		});

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY,
				"Enter Customer terminology", "Customer Terminology", true,
				true, null) {

			@Override
			protected String getSelectString() {
				return "Select Customer Terminology";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return "Customer Terminology has been selected";
			}
		});

		list.add(new StringListRequirement(SUPPLIER_TERMINOLOGY, getMessages()
				.terminology(getConstants().Supplier()), getConstants()
				.supplier(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect("Supplier Terminology");
			}

			@Override
			protected List<String> getLists(Context context) {
				return getSupplierTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return "Vendor Terminology has been selected";
			}
		});

		list.add(new StringListRequirement(ACCOUNT_TERMINOLOGY, getMessages()
				.terminology(getConstants().Account()), getConstants()
				.Account(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect("Account Terminology");
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return "Account Terminology has been selected";
			}
		});

		list.add(new StringListRequirement(SERVICE_PRODUCTS_BOTH,
				getConstants().productAndService(), getConstants()
						.productAndService(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().productAndService());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getServiceProductBothList();
			}

			@Override
			protected String getSetMessage() {
				String v = getValue();
				return "You have " + v + " selected";
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return "Track Tax Enabled";
			}

			@Override
			protected String getFalseString() {
				return "Track Tax Disabled";
			}
		});

		list.add(new BooleanRequirement(ONE_PER_TRANSACTION, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) CreateFullCompanyCommand.this.get(TRACK_TAX)
						.getValue()) {
					super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getConstants().onepertransaction();
			}

			@Override
			protected String getFalseString() {
				return getConstants().oneperdetailline();
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX_PAD, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) CreateFullCompanyCommand.this.get(TRACK_TAX)
						.getValue()) {
					super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Tracking Tax paid enabled";
			}

			@Override
			protected String getFalseString() {
				return "Tracking Tax paid disabled";
			}
		});

		list.add(new BooleanRequirement(CREATE_ESTIMATES, true) {

			@Override
			protected String getTrueString() {
				return "Yes,want to create estimates";
			}

			@Override
			protected String getFalseString() {
				return "No,don't want to create estimates";
			}
		});

		list.add(new CurrencyRequirement(SELECT_CURRENCY, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), true, true, null) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return getCurrenciesList();
			}

		});

		list.add(new BooleanRequirement(MANAGE_BILLS_OWE, true) {

			@Override
			protected String getTrueString() {
				return "Manage Bill you owe";
			}

			@Override
			protected String getFalseString() {
				return "Don't Manage Bill you owe";
			}
		});

		list.add(new StringListRequirement(FISCAL_YEAR, getMessages()
				.pleaseSelect(getConstants().fiscalYear()), getConstants()
				.fiscalYear(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().fiscalYear());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getFiscalYearMonths();
			}

			@Override
			protected String getSetMessage() {
				return "Fiscal year has been selected";
			}
		});

		list.add(new TemplateAccountRequirement(ACCOUNTS, getMessages()
				.pleaseSelect(getConstants().account()), getConstants()
				.account(), true, true) {

			@Override
			protected int getIndustryType() {
				return getIndustryList().indexOf(
						(String) get(INDUSTRY).getValue()) + 1;
			}
		});
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Services Only");
		arrayList.add("Products Only");
		arrayList.add("Both");
		return arrayList;
	}

	private List<ClientCurrency> getCurrenciesList() {
		return CoreUtils.getCurrencies(new ArrayList<ClientCurrency>());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().company());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(COUNTRY).setDefaultValue("United Kingdom");
		get(STATE).setDefaultValue("Buckinghamshire");
		get(TIME_ZONE).setDefaultValue(getDefaultTzOffsetStr());
		get(ORGANIZATION_REFER).setDefaultValue(getOrganizationTypes().get(0));
		get(CUSTOMER_TERMINOLOGY).setDefaultValue(
				getCustomerTerminologies().get(0));
		get(SUPPLIER_TERMINOLOGY).setDefaultValue(
				getSupplierTerminologies().get(0));
		get(ACCOUNT_TERMINOLOGY).setDefaultValue(
				getAccountTerminologies().get(0));
		get(SERVICE_PRODUCTS_BOTH).setDefaultValue(
				getServiceProductBothList().get(0));
		get(ONE_PER_TRANSACTION).setDefaultValue(true);
		get(SELECT_CURRENCY).setDefaultValue(getCurrenciesList().get(0));
		get(FISCAL_YEAR).setDefaultValue("January");
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String companyName = get(COMPANY_NAME).getValue();
		String industryType = get(INDUSTRY).getValue();
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
			preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
					countryPreferences.getDefaultFiscalYearStartingMonth()));
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
		preferences
				.setIndustryType(getIndustryList().indexOf(industryType) + 1);

		String organizationRefer = get(ORGANIZATION_REFER).getValue();
		preferences.setOrganizationType(getOrganizationTypes().indexOf(
				organizationRefer) + 1);
		String customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		String supplierTerm = get(SUPPLIER_TERMINOLOGY).getValue();
		String accountTerm = get(ACCOUNT_TERMINOLOGY).getValue();

		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm) + 1);
		preferences.setReferVendors(getSupplierTerminologies().indexOf(
				supplierTerm) + 1);
		preferences.setReferAccounts(getAccountTerminologies().indexOf(
				accountTerm) + 1);
		String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH).getValue();
		Integer servProBoth = getServiceProductBothList().indexOf(
				serviceProductBoth) + 1;
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
		Company company = AccounterCompanyInitializationServiceImpl
				.intializeCompany(preferences, accounts, context.getIOSession()
						.getClient());
		if (company == null) {
			return new Result("Problem while creating the company.");
		}
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().company());
	}

}
