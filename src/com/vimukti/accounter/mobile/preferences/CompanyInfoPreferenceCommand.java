package com.vimukti.accounter.mobile.preferences;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.commands.URLRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CompanyInfoPreferenceCommand extends
		AbstractCompanyPreferencesCommand {
	protected static final String COMPANY_NAME = "companyname";
	private static final String IS_DIFFERENT_NAME = "isdiffrentname";
	protected static final String LEGAL_NAME = "legalname";
	private static final String COMPANY_ADDRESS = "address";
	// private static final String IS_DIFFERENT_ADDRESS = "isdiffrentaddress";
	private static final String COMPANY_ADDRESS2 = "address";
	protected static final String EMAIL = "email";
	protected static final String WEBSITE = "website";
	protected static final String PHONE_NUMBER = "phonenumber";
	private static final String ADDRESS1 = "address1";
	private static final String ADDRESS2 = "address2";
	private static final String CITY = "city";
	private static final String POSTAL_CODE = "postalCode";
	private static final String STATE = "stateOrProvinence";
	private static final String COUNTRY = "countryOrRegion";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(COMPANY_NAME, "Please Enter Company name",
				getMessages().companyName(), true, true));

		list.add(new BooleanRequirement(IS_DIFFERENT_NAME, true) {

			@Override
			protected String getTrueString() {

				return getMessages().getDifferentLegalName() + " "
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().getDifferentLegalName() + " "
						+ getMessages().inActive();
			}
		});

		list.add(new NameRequirement(LEGAL_NAME,
				"Please Enter Company legal name", getMessages()
						.legalCompanyName(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				boolean difname = get(IS_DIFFERENT_NAME).getValue();
				if (difname) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NameRequirement(ADDRESS1, getMessages().pleaseEnter(
				getMessages().address1()), getMessages().address1(), true, true));
		list.add(new NameRequirement(ADDRESS2, getMessages().pleaseEnter(
				getMessages().address2()), getMessages().address2(), true, true));
		list.add(new NameRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));
		list.add(new NameRequirement(POSTAL_CODE, getMessages().pleaseEnter(
				getMessages().postalCode()), getMessages().postalCode(), true,
				true));
		list.add(new NameRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), true, true));

		list.add(new StringListRequirement(STATE, "Please Enter state", STATE,
				true, true, null) {

			@Override
			protected String getSetMessage() {
				return "state has been selected";
			}

			@Override
			protected String getSelectString() {
				return "Select Account type";
			}

			@Override
			protected List<String> getLists(Context context) {
				String countryName = CompanyInfoPreferenceCommand.this.get(
						COUNTRY).getValue();
				List<String> statesAsListForCountry = CoreUtils
						.getStatesAsListForCountry(countryName);
				return statesAsListForCountry;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEBSITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new PhoneRequirement(PHONE_NUMBER, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		get(COMPANY_NAME).setValue(preferences.getTradingName());
		get(IS_DIFFERENT_NAME).setValue(preferences.isShowLegalName());
		get(LEGAL_NAME).setValue(preferences.getLegalName());
		ClientAddress tradingAddress = preferences.getTradingAddress();
		get(ADDRESS1).setValue(tradingAddress.getAddress1());
		get(ADDRESS2).setValue(tradingAddress.getStreet());
		get(POSTAL_CODE).setValue(tradingAddress.getZipOrPostalCode());
		get(COUNTRY).setValue(tradingAddress.getCountryOrRegion());
		get(COUNTRY).setEditable(false);
		get(STATE).setValue(tradingAddress.getStateOrProvinence());
		get(EMAIL).setValue(preferences.getCompanyEmail());
		get(WEBSITE).setValue(preferences.getWebSite());
		get(PHONE_NUMBER).setValue(preferences.getPhone());

		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();
		String name = get(COMPANY_NAME).getValue();
		boolean isdiffname = get(IS_DIFFERENT_NAME).getValue();
		String legelname = get(LEGAL_NAME).getValue();
		String email = get(EMAIL).getValue();
		String website = get(WEBSITE).getValue();
		String phno = get(PHONE_NUMBER).getValue();

		preferences.setTradingName(name);

		if (isdiffname) {
			preferences.setShowLegalName(true);
		} else {
			preferences.setShowLegalName(false);
		}
		preferences.setLegalName(legelname);
		preferences.setPhone(phno);
		preferences.setCompanyEmail(email);
		preferences.setWebSite(website);
		ClientAddress address = new ClientAddress();
		address.setAddress1((String) get(ADDRESS1).getValue());
		address.setStreet((String) get(ADDRESS2).getValue());
		address.setZipOrPostalCode((String) get(POSTAL_CODE).getValue());
		address.setCountryOrRegion((String) get(COUNTRY).getValue());
		address.setStateOrProvinence((String) get(STATE).getValue());
		preferences.setTradingAddress(address);
		savePreferences(context, preferences);
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Updating Preferences";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToUpdate(getMessages().companyPreferences());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().updateSuccessfully(
				getMessages().companyPreferences());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
