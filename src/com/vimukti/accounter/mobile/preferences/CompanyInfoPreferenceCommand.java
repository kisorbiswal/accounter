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
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class CompanyInfoPreferenceCommand extends
		AbstractCompanyPreferencesCommand {
	protected static final String COMPANY_NAME = "companyname";
	private static final String IS_DIFFERENT_NAME = "isdiffrentname";
	protected static final String LEGAL_NAME = "legalname";
	private static final String COMPANY_ADDRESS = "address";
	private static final String IS_DIFFERENT_ADDRESS = "isdiffrentaddress";
	private static final String COMPANY_ADDRESS2 = "address";
	protected static final String EMAIL = "email";
	protected static final String WEBSITE = "website";
	protected static final String PHONE_NUMBER = "phonenumber";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(COMPANY_NAME, "Please Enter Company name",
				getMessages().companyName(), true, true));

		list.add(new BooleanRequirement(IS_DIFFERENT_NAME, true) {

			@Override
			protected String getTrueString() {

				return getMessages().getCompanyLegalCheckBoxText() + " "
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().getCompanyLegalCheckBoxText() + " "
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

		list.add(new BooleanRequirement(IS_DIFFERENT_ADDRESS, true) {

			@Override
			protected String getTrueString() {
				return getMessages().getCustomersEmailId() + " "
						+ getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().getCustomersEmailId() + " "
						+ getMessages().inActive();
			}
		});

		// TODO address requirement

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new URLRequirement(WEBSITE, getMessages().pleaseEnter(
				getMessages().webSite()), getMessages().webSite(), true, true));

		list.add(new PhoneRequirement(PHONE_NUMBER, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));
		// list.add(new CompanyPreferencesAddressRequirment(COMPANY_ADDRESS,
		// getMessages().pleaseEnter(getMessages().address()),
		// getMessages().address(), true, true));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();
		get(COMPANY_NAME).setValue(preferences.getTradingName());
		get(IS_DIFFERENT_NAME).setValue(preferences.isShowLegalName());
		get(LEGAL_NAME).setValue(preferences.getLegalName());
		get(IS_DIFFERENT_ADDRESS).setValue(
				preferences.isShowRegisteredAddress());
		// get(COMPANY_ADDRESS).setValue(preferences.getTradingAddress());
		// get(COMPANY_ADDRESS2).setValue(preferences.isShowRegisteredAddress());
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
		boolean isdiffadd = get(IS_DIFFERENT_ADDRESS).getValue();
		// ClientAddress address = get(COMPANY_ADDRESS).getValue();
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
		// preferences.setTradingAddress(address);

		if (isdiffadd) {
			preferences.setShowRegisteredAddress(true);
		} else {
			preferences.setShowRegisteredAddress(false);
		}

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
