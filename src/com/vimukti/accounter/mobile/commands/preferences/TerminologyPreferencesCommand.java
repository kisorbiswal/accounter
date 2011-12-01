package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class TerminologyPreferencesCommand extends
		AbstractCompanyPreferencesCommand {
	private static final String CUSTOMER_TERMINOLOGY = "customerterminalogy";
	private static final String VENDOR_TERMINOLOGY = "vendorterminology";

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY, getMessages()
				.Customer() + " " + getMessages().terminology(), getMessages()
				.Customer() + " " + getMessages().terminology(), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().Customer() + " "
								+ getMessages().terminology());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().Customer() + " "
								+ getMessages().terminology());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(VENDOR_TERMINOLOGY, true) {

			@Override
			protected String getTrueString() {
				return getMessages().Vendor() + " "
						+ getMessages().terminology() + " :"
						+ getMessages().Supplier();
			}

			@Override
			protected String getFalseString() {
				return getMessages().Vendor() + " "
						+ getMessages().terminology() + " :"
						+ getMessages().Vendor();
			}
		});

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

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		ClientCompanyPreferences preferences = context.getPreferences();

		get(CUSTOMER_TERMINOLOGY)
				.setValue(
						getCustomerTerminologies().get(
								preferences.getReferCustomers()));

		if (preferences.getReferVendors() == 1) {
			get(VENDOR_TERMINOLOGY).setValue(true);
		} else {
			get(VENDOR_TERMINOLOGY).setValue(false);
		}
		return null;

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences preferences = context.getPreferences();

		String customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		boolean supplierTerm = get(VENDOR_TERMINOLOGY).getValue();

		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm));
		if (supplierTerm) {
			preferences.setReferVendors(1);
		} else {
			preferences.setReferVendors(2);
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
