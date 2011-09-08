package com.vimukti.accounter.web.client.uibinder.companypreferences;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CompanyRegisteredDetailsPage extends AbstractCompanyInfoPanel {

	private static CompanyRegisteredDetailsPageUiBinder uiBinder = GWT
			.create(CompanyRegisteredDetailsPageUiBinder.class);
	@UiField
	Label registeredCompanyNameLabel;
	@UiField
	TextBox registeredCompanyName;
	@UiField
	Label address1Label;
	@UiField
	TextBox address1TextBox;
	@UiField
	Label address2Label;
	@UiField
	TextBox address2TextBox;
	@UiField
	Label cityLabel;
	@UiField
	TextBox cityTextBox;
	@UiField
	Label postalCodeLabel;
	@UiField
	TextBox postalCodeTextBox;
	@UiField
	Label countryLabel;
	@UiField
	ListBox countryCombo;
	@UiField
	Label stateLabel;
	@UiField
	ListBox stateCombo;
	@UiField
	Label registeredAddress;

	private List<String> countriesList, states;

	private ClientAddress address;

	interface CompanyRegisteredDetailsPageUiBinder extends
			UiBinder<Widget, CompanyRegisteredDetailsPage> {
	}

	public CompanyRegisteredDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	public CompanyRegisteredDetailsPage(ClientCompanyPreferences preferences,
			ClientCompany clientCompany, CompanyInfoPage companyInfoPage) {
		initWidget(uiBinder.createAndBindUi(this));
		companyPreferences = preferences;
		company = clientCompany;
		createControls();
	}

	private void createControls() {
		registeredAddress.setText(constants.registeredAddress());
		registeredCompanyNameLabel.setText(Accounter.constants()
				.registeredCompanyName());
		address1Label.setText(Accounter.constants().address1());
		address2Label.setText(Accounter.constants().address2());
		cityLabel.setText(Accounter.constants().city());
		stateLabel.setText(Accounter.constants().state());
		postalCodeLabel.setText(Accounter.constants().postalCode());
		countryLabel.setText(Accounter.constants().country());

		countriesList = new ArrayList<String>();
		countriesList.addAll(CoreUtils.getCountriesAsList());
		for (int i = 0; i < countriesList.size(); i++) {
			countryCombo.addItem(countriesList.get(i));
		}

		countryCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (stateCombo.getItemCount() != 0) {
					for (int i = 0; i < stateCombo.getItemCount(); i++) {
						stateCombo.removeItem(i);
					}
				}
				countryChanged();
			}
		});
		countryChanged();
	}

	@Override
	public void onLoad() {
		registeredCompanyName.setValue(company.getDisplayName());
		address = company.getRegisteredAddress();
		if (address != null) {
			this.address1TextBox.setValue(address.getAddress1());
			this.address2TextBox.setValue(address.getStreet());
			this.cityTextBox.setValue(address.getCity());
			this.postalCodeTextBox.setValue(address.getZipOrPostalCode());
			if (address.getStateOrProvinence() != ""
					&& address.getStateOrProvinence() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.stateCombo.setSelectedIndex(states.indexOf(address
						.getStateOrProvinence()));
			}
			if (address.getCountryOrRegion() != ""
					&& address.getCountryOrRegion() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.countryCombo.setSelectedIndex(countriesList
						.indexOf(address.getCountryOrRegion()));
			}
		}
	}

	@Override
	public void onSave() {
		address = new ClientAddress();
		address.setAddress1(address1TextBox.getValue());
		address.setStreet(address2TextBox.getValue());
		address.setCity(cityTextBox.getValue());
		address.setZipOrPostalCode(postalCodeTextBox.getValue());
		if (stateCombo.getSelectedIndex() != -1) {
			address.setStateOrProvinence(states.get(stateCombo
					.getSelectedIndex()));
		}
		if (countryCombo.getSelectedIndex() != -1)
			address.setCountryOrRegion(countriesList.get(countryCombo
					.getSelectedIndex()));
		company.setRegisteredAddress(address);
		Accounter.setCompany(company);
	}

	private void countryChanged() {
		int selectedCountry = countryCombo.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		List<String> states = CoreUtils.getStatesAsListForCountry(countryCombo
				.getItemText(selectedCountry));
		setStates(states);

	}

	private void setStates(List<String> states2) {
		this.states = states2;
		stateCombo.clear();
		for (int i = 0; i < states.size(); i++) {
			stateCombo.addItem(states.get(i));
		}

	}
}
