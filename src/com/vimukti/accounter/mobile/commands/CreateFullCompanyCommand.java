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
import com.vimukti.accounter.mobile.requirements.ClientCurrencyRequirement;
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
		return getMessages().creating(getMessages().fullCompanySetup());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		list.add(getCompanyNameRequirement());

		list.add(new NameRequirement(LEGAL_NAME, getMessages().pleaseEnter(
				getMessages().legalName()), getMessages().legalName(), true,
				true));

		list.add(new NameRequirement(TAX_ID, getMessages().pleaseEnter(
				getMessages().taxId()), getMessages().taxId(), true, true));

		list.add(new CountryRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), false, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						ClientCurrency currency = countrySelected(value);
						get(PRIMARY_CURRENCY).setValue(currency);
						get(STATE).setValue(null);
					}

				}));

		list.add(new StringListRequirement(STATE, getMessages().pleaseEnter(
				getMessages().state()), getMessages().state(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().state());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().state());
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
				getMessages().address1()), getMessages().address1(), true, true));

		list.add(new NameRequirement(ADDRESS2, getMessages().pleaseEnter(
				getMessages().address2()), getMessages().address2(), true, true));

		list.add(new NameRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));

		list.add(new NameRequirement(ZIPCODE, getMessages().pleaseEnter(
				getMessages().zipCode()), getMessages().zipCode(), true, true));

		list.add(new NameRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phone()), getMessages().phone(), true, true));

		list.add(new NameRequirement(FAX, getMessages().pleaseEnter(
				getMessages().fax()), getMessages().fax(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new NameRequirement(WEB_SITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new StringListRequirement(TIME_ZONE, getMessages()
				.pleaseSelect(getMessages().timezone()), getMessages()
				.timezone(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().timezone());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTimeZonesList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().timezone());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		// Second Page
		list.add(new StringListRequirement(INDUSTRY, getMessages().pleaseEnter(
				getMessages().industry()), getMessages().industry(), false,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						get(ACCOUNTS).setValue(null);
					}
				}) {
			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().industry());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getIndustryList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().industry());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

		});

		list.add(new StringListRequirement(ORGANIZATION_REFER, getMessages()
				.pleaseEnter(getMessages().organisation()), getMessages()
				.organisation(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().howisYourCompanyOrganized();
			}

			@Override
			protected List<String> getLists(Context context) {
				return getOrganizationTypes();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().companyOrganization());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (CreateFullCompanyCommand.this.get(COUNTRY).getValue()
						.equals("United States")) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY, getMessages()
				.pleaseEnter(
						getMessages()
								.payeeTerminology(getMessages().customer())),
				getMessages().payeeTerminology(getMessages().customer()), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages()
								.payeeTerminology(getMessages().customer()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages()
								.payeeTerminology(getMessages().Customer()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(
				SUPPLIER_TERMINOLOGY,
				getMessages().pleaseEnter(
						getMessages().payeeTerminology(getMessages().vendor())),
				getMessages().payeeTerminology(getMessages().vendor()), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTerminology(getMessages().Vendor()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getSupplierTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().payeeTerminology(getMessages().Vendor()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(ACCOUNT_TERMINOLOGY, getMessages()
				.payeeTerminology(getMessages().Account()), getMessages()
				.Account(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(
								getMessages().payeeTerminology(
										getMessages().Account()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(
								getMessages().payeeTerminology(
										getMessages().Account()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		list.add(new CurrencyRequirement(PRIMARY_CURRENCY, getMessages()
				.primaryCurrency(), getMessages().primaryCurrency(), true,
				true, null) {
			@Override
			protected List<ClientCurrency> getLists(Context context) {
				List<ClientCurrency> currenciesList = new ArrayList<ClientCurrency>();
				List<ClientCurrency> currencies = CoreUtils
						.getCurrencies(new ArrayList<ClientCurrency>());
				for (ClientCurrency currency : currencies) {
					currenciesList.add(currency);
				}
				return currenciesList;
			}
		});

		list.add(new BooleanRequirement(IS_MULTI_CURRENCY_ENBLED, true) {

			@Override
			protected String getTrueString() {
				return " Multi currency enabled";
			}

			@Override
			protected String getFalseString() {
				return " Multi currency is not enabled";
			}
		});

		list.add(new StringListRequirement(SERVICE_PRODUCTS_BOTH, getMessages()
				.productAndService(), getMessages().productAndService(), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productAndService());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getServiceProductBothList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().productAndServices());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackTaxEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackTaxDisabled();
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
				return getMessages().onepertransaction();
			}

			@Override
			protected String getFalseString() {
				return getMessages().oneperdetailline();
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
				return getMessages().trackingTaxPaidEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackingTaxPaidDisabled();
			}
		});

		list.add(new BooleanRequirement(CREATE_ESTIMATES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().wanttoCreateEstimates();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontWantToCreateEstimates();
			}
		});

		list.add(new ClientCurrencyRequirement(SELECT_CURRENCY, getMessages()
				.pleaseSelect(getMessages().currency()), getMessages()
				.currency(), true, true, null) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return getCurrenciesList();
			}

		});

		list.add(new BooleanRequirement(MANAGE_BILLS_OWE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().manageBillsYouOwe();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontManageBillsYouOwe();
			}
		});

		list.add(new StringListRequirement(FISCAL_YEAR, getMessages()
				.pleaseSelect(getMessages().fiscalYear()), getMessages()
				.fiscalYear(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().fiscalYear());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getFiscalYearMonths();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().fiscalYear());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new TemplateAccountRequirement(ACCOUNTS, getMessages()
				.pleaseSelect(getMessages().account()),
				getMessages().account(), true, true) {

			@Override
			protected int getIndustryType() {
				return getIndustryList().indexOf(
						(String) get(INDUSTRY).getValue());
			}
		});
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().servicesOnly());
		arrayList.add(getMessages().productsOnly());
		arrayList.add(getMessages().bothServicesandProducts());
		return arrayList;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().company());
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
		// get(ONE_PER_TRANSACTION).setDefaultValue(true);
		get(SELECT_CURRENCY).setDefaultValue(getCurrenciesList().get(0));
		get(FISCAL_YEAR).setDefaultValue(getMessages().april());
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
		ClientCurrency primaryCurrency = get(PRIMARY_CURRENCY).getValue();
		Boolean ismultiCurrencyEnabled = get(IS_MULTI_CURRENCY_ENBLED)
				.getValue();

		ClientCompanyPreferences preferences = new ClientCompanyPreferences();
		preferences.setPrimaryCurrency(currency);

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
		preferences.setPrimaryCurrency(primaryCurrency);
		preferences.setEnableMultiCurrency(ismultiCurrencyEnabled);
		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm));
		preferences.setReferVendors(getSupplierTerminologies().indexOf(
				supplierTerm));
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
		preferences.setTaxPerDetailLine(!onePerTrans);
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
					getMessages().company()));
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
		return getMessages().createSuccessfully(getMessages().company());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

}
