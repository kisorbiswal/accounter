package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.server.AccounterCompanyInitializationServiceImpl;

public class CreatePartialCompanyCommand extends AbstractCompanyCommad {

	private String country;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
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

		list.add(new StringListRequirement(COUNTRY, getMessages().pleaseSelect(
				getConstants().country()), getConstants().country(), true,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						country = value;
					}
				}) {

			@Override
			protected String getEmptyString() {
				return null;
			}

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
				return getMessages().pleaseSelect(getConstants().country());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCountryList();
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new StringListRequirement(STATE, "Enter State", "State", true,
				true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

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
				return getMessages().pleaseSelect(getConstants().state());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getStatesList(country);
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
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
			protected String getEmptyString() {
				return null;
			}

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
				return getMessages().pleaseSelect(getConstants().timezone());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTimeZonesList();
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		// Second Page
		// Second Page
		list.add(new StringListRequirement(INDUSTRY, getMessages()
				.pleaseSelect(getConstants().industry()), getConstants()
				.industry(), false, true, null) {

			@Override
			protected String getEmptyString() {
				return null;
			}

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
				return getMessages().pleaseSelect(getConstants().industry());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getIndustryList();
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new StringListRequirement(ORGANIZATION_REFER,
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
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getOrganizationTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

		});

		list.add(new StringListRequirement(FISCAL_YEAR, getMessages()
				.pleaseSelect(getConstants().fiscalYear()), getConstants()
				.fiscalYear(), true, true, null) {

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
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().fiscalYear());
			}

			@Override
			protected boolean filter(String e, String name) {
				return e.startsWith(name);
			}

			@Override
			protected List<String> getLists(Context context) {
				return getFiscalYearMonths();
			}

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});

	}

	// @Override
	// public Result run(Context context) {
	// setDefaultValues();
	// if (context.getAttribute(INPUT_ATTR) == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	//
	// Result makeResult = context.makeResult();
	// makeResult
	// .add("Company is ready to be created with following details:");
	// ResultList list = new ResultList(VALUES);
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	//
	// Result result = nameRequirement(context, list, COMPANY_NAME,
	// getConstants().companyName(),
	// getMessages().pleaseEnter(getConstants().companyName()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberListRequirement(context, getConstants().industry(),
	// getIndustryList(), INDUSTRIES, get(INDUSTRY), list);
	// if (result != null) {
	// return result;
	// }
	//
	// result = createOptionalRequirement(context, list, actions, makeResult);
	// makeResult.add(actions);
	// if (result != null) {
	// return result;
	// }
	//
	// try {
	// createCompany(context);
	// markDone();
	// } catch (AccounterException e) {
	// e.printStackTrace();
	// return new Result(e.getMessage());
	//
	// }
	// result = new Result(getMessages().createSuccessfully(
	// getConstants().companyPreferences()));
	// return result;
	// }

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
		get(FISCAL_YEAR).setDefaultValue("January");
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
		List<TemplateAccount> accounts = get(ACCOUNTS).getValue();
		ClientCompanyPreferences preferences = new ClientCompanyPreferences();

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
		setStartDateOfFiscalYear(preferences);
		AccounterCompanyInitializationServiceImpl.intializeCompany(preferences,
				accounts, context.getIOSession().getClient());
	}

	// private Result createOptionalRequirement(Context context, ResultList
	// list,
	// ResultList actions, Result makeResult) {
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// // case FINISH_FIRST_PAGE:
	// // context.removeAttribute(INPUT_ATTR);
	// // return null;
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// return null;
	// default:
	// break;
	// }
	// }
	// selection = context.getSelection(VALUES);
	//
	// Result result = nameRequirement(context, list, LEGAL_NAME,
	// getConstants().legalCompanyName(),
	// getMessages().pleaseEnter(getConstants().legalCompanyName()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, TAX_ID, getConstants().taxId(),
	// getMessages().pleaseEnter(getConstants().taxId()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = listRequirement(context, getConstants().state(),
	// getStatesList((String) get(COUNTRY).getValue()), COUNTRIES,
	// get(COUNTRY), list);
	// if (result != null) {
	// return result;
	// }
	//
	// result = listRequirement(context, getConstants().state(),
	// getCountryList(), STATES, get(STATE), list);
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, ADDRESS1, getConstants()
	// .address1(),
	// getMessages().pleaseEnter(getConstants().address1()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, ADDRESS2, getConstants()
	// .address2(),
	// getMessages().pleaseEnter(getConstants().address2()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, CITY, getConstants().city(),
	// getMessages().pleaseEnter(getConstants().city()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, ZIPCODE, getConstants()
	// .zipCode(), getMessages().pleaseEnter(getConstants().zipCode()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, PHONE, getConstants()
	// .phoneNumber(),
	// getMessages().pleaseEnter(getConstants().phoneNumber()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(context, list, FAX,
	// getConstants().faxNumber(),
	// getMessages().pleaseEnter(getConstants().faxNumber()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = emailRequirement(context, list, EMAIL, getConstants().email(),
	// getMessages().pleaseEnter(getConstants().email()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = nameRequirement(
	// context,
	// list,
	// WEB_SITE,
	// getConstants().webSite(),
	// getMessages().pleaseEnter(
	// getMessages().name(getConstants().webSite())));
	// if (result != null) {
	// return result;
	// }
	//
	// result = listRequirement(context, getConstants().timezone(),
	// getTimeZonesList(), TIME_ZONES, get(TIME_ZONE), list);
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberListRequirement(context, getConstants().organisation(),
	// getOrganizationTypes(), ORGANIZATION_TYPES,
	// get(ORGANIZATION_REFER), list);
	// if (result != null) {
	// return result;
	// }
	//
	// result = listRequirement(context, getConstants().fiscalYear(),
	// getFiscalYearMonths(), FISCAL_YEAR_LIST, get(FISCAL_YEAR), list);
	// if (result != null) {
	// return result;
	// }
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add(
	// "",
	// getMessages().finishToCreate(
	// getConstants().companyPreferences()));
	// actions.add(finish);
	// return makeResult;
	// }

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().company());
	}

	@Override
	protected void setDefaultValues(Context context) {
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
		get(FISCAL_YEAR).setDefaultValue("January");
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().company());
	}

}
