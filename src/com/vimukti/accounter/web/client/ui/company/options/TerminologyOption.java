/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * @author vimukti36
 * 
 */
public class TerminologyOption extends AbstractPreferenceOption {

	private static TerminologyOptionUiBinder uiBinder = GWT
			.create(TerminologyOptionUiBinder.class);
	@UiField
	Label terminologyforCustomerLabel;
	@UiField
	RadioButton tenantsLabelRadioButton;
	@UiField
	RadioButton clientsRadioButton;
	@UiField
	RadioButton custimersRadioBuitton;
	@UiField
	RadioButton DonorsRadioButton;
	@UiField
	RadioButton guestardioButton;
	@UiField
	RadioButton membersRadioButton;
	@UiField
	RadioButton PatientRadioButton;
	@UiField
	Label customerDescriptionLabel;
	@UiField
	RadioButton vendorRadioButton;
	@UiField
	RadioButton supplierRadioButton;
	@UiField
	Label vendorsHeaderLabel;
	@UiField
	Label vendorsDescriptionLabel;

	interface TerminologyOptionUiBinder extends
			UiBinder<Widget, TerminologyOption> {
	}

	public TerminologyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public TerminologyOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void createControls() {
		terminologyforCustomerLabel.setText(messages.useTerminologyFor(Global
				.get().Customer()));
		customerDescriptionLabel.setText(messages.customerDescription());
		customerDescriptionLabel.setStyleName("organisation_comment");
		tenantsLabelRadioButton.setName(messages.group());
		tenantsLabelRadioButton.setHTML(messages.Tenant());
		custimersRadioBuitton.setName(messages.group());
		custimersRadioBuitton.setHTML(messages.Customer());
		guestardioButton.setName(messages.group());
		guestardioButton.setHTML(messages.Guest());
		membersRadioButton.setName(messages.group());
		membersRadioButton.setHTML(messages.Member());
		PatientRadioButton.setName(messages.group());
		PatientRadioButton.setHTML(messages.Patient());
		clientsRadioButton.setName(messages.group());
		clientsRadioButton.setHTML(messages.Client());
		DonorsRadioButton.setName(messages.group());
		DonorsRadioButton.setHTML(messages.Donar());

		vendorsHeaderLabel.setText(messages.useTerminologyFor(Global.get()
				.Vendor()));
		vendorsDescriptionLabel.setText(messages.vendorDescription());
		vendorsDescriptionLabel.setStyleName("organisation_comment");
		vendorRadioButton.setName(messages.Vendor());
		vendorRadioButton.setHTML(messages.Vendor());
		supplierRadioButton.setName(messages.Vendor());
		supplierRadioButton.setHTML(messages.Supplier());
	}

	@Override
	public String getTitle() {
		return messages.terminology();
	}

	@Override
	public void onSave() {

		if (tenantsLabelRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.TENANT);
		} else if (custimersRadioBuitton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CUSTOMER);
		} else if (guestardioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.GUEST);
		} else if (membersRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.MEMBER);
		} else if (PatientRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.PATITEINT);
		} else if (clientsRadioButton.getValue()) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CLIENT);
		} else {
			getCompanyPreferences().setReferCustomers(ClientCustomer.DONAR);
		}

		if (vendorRadioButton.getValue()) {
			getCompanyPreferences().setReferVendors(ClientVendor.VENDOR);
		} else if (supplierRadioButton.getValue()) {
			getCompanyPreferences().setReferVendors(ClientVendor.SUPPLIER);
		}

	}

	@Override
	public String getAnchor() {
		return messages.terminology();
	}

	@Override
	public void initData() {

		int referCustomers = getCompanyPreferences().getReferCustomers();
		terminologyforCustomerLabel.setText(messages.useTerminologyFor(Global
				.get().Customer()));
		switch (referCustomers) {
		case ClientCustomer.TENANT:
			tenantsLabelRadioButton.setValue(true);
			break;
		case ClientCustomer.CUSTOMER:
			custimersRadioBuitton.setValue(true);
			break;
		case ClientCustomer.GUEST:
			guestardioButton.setValue(true);
			break;
		case ClientCustomer.MEMBER:
			tenantsLabelRadioButton.setValue(true);
			break;
		case ClientCustomer.PATITEINT:
			PatientRadioButton.setValue(true);
			break;
		case ClientCustomer.CLIENT:
			clientsRadioButton.setValue(true);
			break;
		case ClientCustomer.DONAR:
			DonorsRadioButton.setValue(true);
			break;
		}
		int referVendors = getCompanyPreferences().getReferVendors();
		switch (referVendors) {
		case ClientVendor.VENDOR:
			vendorRadioButton.setValue(true);
			break;
		case ClientVendor.SUPPLIER:
			supplierRadioButton.setValue(true);
			break;
		}

	}

}
