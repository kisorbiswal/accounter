package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;
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
	protected static final String PRIMARY_CURRENCY = "Primary Currency";
	protected static final String IS_MULTI_CURRENCY_ENBLED = "ismultiCurrecnyEnbled";

	List<AccountsTemplate> allAccounts = new ArrayList<AccountsTemplate>();

	ClientCompanyPreferences preferences;

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
		nameRecord.add(name, getNameValue(requirement));
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
		nameRecord.add(name, requirementvalue);
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
			record.add(displayList.get(num));
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

		return "UTC+5:30 Asia/Kolkata";
	}

	protected void setStartDateOfFiscalYear(ClientCompanyPreferences preferences) {
		ClientFinanceDate currentDate = new ClientFinanceDate();
		int fiscalYearFirstMonth = preferences.getFiscalYearFirstMonth();
		ClientFinanceDate fiscalYearStartDate = new ClientFinanceDate(
				currentDate.getYear(), fiscalYearFirstMonth, 1);
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

	protected Requirement getCompanyNameRequirement() {
		return new NameRequirement(COMPANY_NAME, getMessages().pleaseEnter(
				getMessages().companyName()), getMessages().companyName(),
				false, true) {
			@Override
			public void setValue(Object val) {
				String value = (String) val;
				if (value == null) {
					return;
				} else if (value.trim().length() <= 5) {
					setEnterString("Invalid Company Name. Name Should be grater than 5 characters");
					return;
				} else if (AbstractCompanyCommad.this.getCompany(value) != null) {
					setEnterString("Company exist with the same name.Please enter a different name");
					return;
				}
				setEnterString(getMessages().pleaseEnter(
						getMessages().companyName()));
				super.setValue(value);
			}

			@Override
			public boolean isDone() {
				String value = getValue();
				return value != null && !value.isEmpty()
						&& value.trim().length() > 5;
			}
		};
	}

	protected Company getCompany(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getServerCompany.by.name")
				.setParameter("name", companyName);
		return (Company) query.uniqueResult();
	}

	/**
	 * WHEN SELECT THE COUNTRY THEN GET THE CURRENCY OF THAT COUNTRY
	 * 
	 * @param value
	 * @return
	 */
	protected void countrySelected(String value) {
		ICountryPreferences countryPreferences = CountryPreferenceFactory
				.get(value);
		get(STATE).setValue(countryPreferences.getStates()[0]);
		List<ClientCurrency> currenciesList = CoreUtils
				.getCurrencies(new ArrayList<ClientCurrency>());
		for (int i = 0; i < currenciesList.size(); i++) {
			if (countryPreferences.getPreferredCurrency().trim()
					.equals(currenciesList.get(i).getFormalName())) {
				get(PRIMARY_CURRENCY).setValue(currenciesList.get(i));
				return;
			}
		}
	}

	protected Requirement getCountryRequirement() {
		return new CountryRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), false, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						countrySelected(value);
					}

				});
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(getCompanyNameRequirement());

		list.add(new NameRequirement(LEGAL_NAME, getMessages().pleaseEnter(
				getMessages().legalName()), getMessages().legalName(), true,
				true));

		list.add(new NameRequirement(TAX_ID, getMessages().pleaseEnter(
				getMessages().taxId()), getMessages().taxId(), true, true));

		list.add(getCountryRequirement());

		list.add(getStateRequirement());

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
						if (get(ACCOUNTS) != null) {
							get(ACCOUNTS).setValue(null);
						}
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
				if (AbstractCompanyCommad.this.get(COUNTRY).getValue()
						.equals("United States")) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});
	}

	protected Requirement getStateRequirement() {
		return new StringListRequirement(STATE, getMessages().pleaseEnter(
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
		};
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(COUNTRY).setDefaultValue("United Kingdom");
		get(STATE).setDefaultValue("Buckinghamshire");
		countrySelected("United Kingdom");
		get(TIME_ZONE).setDefaultValue(getDefaultTzOffsetStr());
		get(ORGANIZATION_REFER).setDefaultValue(getOrganizationTypes().get(0));
		get(FISCAL_YEAR).setDefaultValue(getMessages().april());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		preferences = new ClientCompanyPreferences();
		String country = context.getIOSession().getClient().getCountry();
		if (country != null) {
			get(COUNTRY).setValue(country);
			countrySelected(country);
		}
		return null;
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
		String organizationRefer = get(ORGANIZATION_REFER).getValue();
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
		preferences.setOrganizationType(getOrganizationTypes().indexOf(
				organizationRefer));
		preferences.setDateFormat(getDateFormat(countryName));
		List<TemplateAccount> accounts = new ArrayList<TemplateAccount>();
		if (get(ACCOUNTS) == null) {
			accounts = getDefaultTemplateAccounts(getIndustryList().indexOf(
					industryType));
		} else {
			accounts = get(ACCOUNTS).getValue();
		}
		ClientCurrency primaryCurrency = get(PRIMARY_CURRENCY).getValue();
		Boolean ismultiCurrencyEnabled = get(IS_MULTI_CURRENCY_ENBLED)
				.getValue();
		String fiscalYear = get(FISCAL_YEAR).getValue();
		preferences.setFiscalYearFirstMonth(getFiscalYearMonths().indexOf(
				fiscalYear));
		preferences.setPrimaryCurrency(primaryCurrency);
		preferences.setEnableMultiCurrency(ismultiCurrencyEnabled);

		setStartDateOfFiscalYear(preferences);

		setPreferences();

		Company company = AccounterCompanyInitializationServiceImpl
				.intializeCompany(preferences, accounts, context.getIOSession()
						.getClient());
		if (company == null) {
			return new Result(getMessages().ProblemWhileCreating(
					getMessages().company()));
		}
		markDone();
		Result result = new Result();
		result.add(getSuccessMessage());
		result.setNextCommand("changeCompany");
		return result;
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

	protected void setPreferences() {

	}

	protected Requirement getFiscalYearRequirement() {
		return new StringListRequirement(FISCAL_YEAR, getMessages()
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
		};
	}

	@Override
	public String getId() {
		return null;
	}

	protected void addCurrencyRequirements(List<Requirement> list) {
		list.add(new CurrencyRequirement(PRIMARY_CURRENCY, getMessages()
				.primaryCurrency(), getMessages().primaryCurrency(), true,
				true, null) {
			@Override
			protected List<ClientCurrency> getLists(Context context) {
				List<ClientCurrency> currencies = CoreUtils
						.getCurrencies(new ArrayList<ClientCurrency>());
				return currencies;
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
	}
}
