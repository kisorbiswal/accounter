package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.AccounterServerConstants;
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
		return getMessages().creating(getConstants().fullCompanySetup());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		list.add(new NameRequirement(COMPANY_NAME, getMessages().pleaseEnter(
				getConstants().companyName()), getConstants().companyName(),
				false, true));

		list.add(new NameRequirement(LEGAL_NAME, getMessages().pleaseEnter(
				getConstants().legalName()), getConstants().legalName(), true,
				true));

		list.add(new NameRequirement(TAX_ID, getMessages().pleaseEnter(
				getConstants().taxId()), getConstants().taxId(), true, true));

		list.add(new CountryRequirement(COUNTRY, true, true, null));

		list.add(new StringListRequirement(STATE, getMessages().pleaseEnter(
				getConstants().state()), getConstants().state(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().state());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().state());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getStatesList((String) get(COUNTRY).getValue());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new NameRequirement(ADDRESS1, getMessages().pleaseEnter(
				getConstants().address1()), getConstants().address1(), true,
				true));

		list.add(new NameRequirement(ADDRESS1, getMessages().pleaseEnter(
				getConstants().address2()), getConstants().address2(), true,
				true));

		list.add(new NameRequirement(CITY, getMessages().pleaseEnter(
				getConstants().city()), getConstants().city(), true, true));

		list.add(new NameRequirement(ZIPCODE, getMessages().pleaseEnter(
				getConstants().zipCode()), getConstants().zipCode(), true, true));

		list.add(new NameRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phone()), getConstants().phone(), true, true));

		list.add(new NameRequirement(FAX, getMessages().pleaseEnter(
				getConstants().fax()), getConstants().fax(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), true, true));

		list.add(new NameRequirement(WEB_SITE, getMessages().pleaseEnter(
				getConstants().webSite()), getConstants().webSite(), true, true));

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
				return getMessages().hasSelected(getConstants().timezone());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		// Second Page
		list.add(new StringListRequirement(INDUSTRY, getMessages().pleaseEnter(
				getConstants().industry()), getConstants().industry(), false,
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
				return getMessages().hasSelected(getConstants().industry());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

		});

		list.add(new StringListRequirement(ORGANIZATION_REFER, getMessages()
				.pleaseEnter(getConstants().organisation()), getConstants()
				.organisation(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getConstants().howisYourCompanyOrganized();
			}

			@Override
			protected List<String> getLists(Context context) {
				return getOrganizationTypes();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getConstants().companyOrganization());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

		});

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY, getMessages()
				.pleaseEnter(
						getMessages().terminology(getConstants().customer())),
				getMessages().terminology(getConstants().customer()), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().terminology(getConstants().customer()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().terminology(getConstants().Customer()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(SUPPLIER_TERMINOLOGY,
				getMessages().pleaseEnter(
						getMessages().terminology(getConstants().vendor())),
				getMessages().terminology(getConstants().vendor()), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().terminology(getConstants().Vendor()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getSupplierTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().terminology(getConstants().Vendor()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(ACCOUNT_TERMINOLOGY, getMessages()
				.terminology(getConstants().Account()), getConstants()
				.Account(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().terminology(getConstants().Account()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().terminology(getConstants().Account()));
			}

			@Override
			protected String getEmptyString() {
				return null;
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
				return getMessages().hasSelected(
						getConstants().productAndServices());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return getConstants().trackTaxEnabled();
			}

			@Override
			protected String getFalseString() {
				return getConstants().trackTaxDisabled();
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
				return getConstants().trackingTaxPaidEnabled();
			}

			@Override
			protected String getFalseString() {
				return getConstants().trackingTaxPaidDisabled();
			}
		});

		list.add(new BooleanRequirement(CREATE_ESTIMATES, true) {

			@Override
			protected String getTrueString() {
				return getConstants().wanttoCreateEstimates();
			}

			@Override
			protected String getFalseString() {
				return getConstants().dontWantToCreateEstimates();
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
				return getConstants().manageBillsYouOwe();
			}

			@Override
			protected String getFalseString() {
				return getConstants().dontManageBillsYouOwe();
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
				return getMessages().hasSelected(getConstants().fiscalYear());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new TemplateAccountRequirement(ACCOUNTS, getMessages()
				.pleaseSelect(getConstants().account()), getConstants()
				.account(), true, true) {

			@Override
			protected int getIndustryType() {
				return getIndustryList().indexOf(
						(String) get(INDUSTRY).getValue());
			}
		});
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getConstants().servicesOnly());
		arrayList.add(getConstants().productsOnly());
		arrayList.add(getConstants().bothServicesandProducts());
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
		get(FISCAL_YEAR).setDefaultValue(getConstants().april());
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
		preferences.setPrimaryCurrency(currency.getFormalName());

		String fiscalYear = get(FISCAL_YEAR).getValue();
		preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
				fiscalYear));

		preferences.setTradingName(companyName);
		preferences.setLegalName(legalName);
		preferences.setPhone(phoneNum);
		preferences.setCompanyEmail(emailId);
		preferences.setTaxId(taxId);
		preferences.setFax(fax);
		preferences.setWebSite(webSite);

		ClientAddress address = new ClientAddress();
		address.setAddress1(address1);
		address.setStreet(address2);
		address.setCity(city);
		address.setZipOrPostalCode(zipCode);
		address.setStateOrProvinence(state);
		address.setCountryOrRegion(countryName);

		preferences.setTradingAddress(address);
		preferences.setTimezone(timeZone);
		preferences.setIndustryType(getIndustryList().indexOf(industryType));

		String organizationRefer = get(ORGANIZATION_REFER).getValue();
		preferences.setOrganizationType(getOrganizationTypes().indexOf(
				organizationRefer));
		String customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		String supplierTerm = get(SUPPLIER_TERMINOLOGY).getValue();
		String accountTerm = get(ACCOUNT_TERMINOLOGY).getValue();

		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm));
		preferences.setReferVendors(getSupplierTerminologies().indexOf(
				supplierTerm));
		preferences.setReferAccounts(getAccountTerminologies().indexOf(
				accountTerm));
		String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH).getValue();
		Integer servProBoth = getServiceProductBothList().indexOf(
				serviceProductBoth);
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
		preferences.setDateFormat(getDateFormat(countryName));
		setStartDateOfFiscalYear(preferences);

		Company company = AccounterCompanyInitializationServiceImpl
				.intializeCompany(preferences, accounts, context.getIOSession()
						.getClient());
		if (company == null) {
			return new Result(getMessages().ProblemWhileCreating(
					getConstants().company()));
		}
		return null;
	}

	private String getDateFormat(String countryName) {
		if (countryName.equals(CountryPreferenceFactory.UNITED_STATES)) {
			return AccounterServerConstants.MMddyyyy;
		} else if (countryName.equals(CountryPreferenceFactory.UNITED_KINGDOM)) {
			return AccounterServerConstants.ddMMyyyy;
		} else if (countryName.equals(CountryPreferenceFactory.INDIA)) {
			return AccounterServerConstants.ddMMyyyy;
		} else {
			return AccounterServerConstants.ddMMyyyy;
		}
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().company());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

}
