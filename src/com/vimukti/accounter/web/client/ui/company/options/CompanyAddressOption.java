package com.vimukti.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CompanyAddressOption extends AbstractPreferenceOption {

	@UiField
	Label tradingAddressTitle;

	@UiField
	Label tAddress1Label;

	@UiField
	TextBox tAddress1TextBox;

	@UiField
	Label tAddress2Label;

	@UiField
	TextBox tAddress2TextBox;

	@UiField
	Label tCityLabel;

	@UiField
	TextBox tCityTextBox;

	@UiField
	Label tPostalCodeLabel;

	@UiField
	TextBox tPostalCodeTextBox;

	@UiField
	Label tCountryLabel;

	@UiField
	ListBox tCountryCombo;

	@UiField
	Label tStateLabel;

	@UiField
	ListBox tStateCombo;

	@UiField
	Label registeredAddressTitle;

	@UiField
	Label rAddress1Label;

	@UiField
	TextBox rAddress1TextBox;

	@UiField
	Label rAddress2Label;

	@UiField
	TextBox rAddress2TextBox;

	@UiField
	Label rCityLabel;

	@UiField
	TextBox rCityTextBox;

	@UiField
	Label rPostalCodeLabel;

	@UiField
	TextBox rPostalCodeTextbox;

	@UiField
	Label rCountryComboLabel;

	@UiField
	ListBox rCountryCombo;

	@UiField
	Label rStateComboLabel;

	@UiField
	ListBox rStateCombo;
	@UiField
	Label registeredAddressDescription;
	@UiField
	Label tradingAddressDescription;
	// @UiField
	// CheckBox companyLegalCheckBox;

	@UiField
	VerticalPanel registeredAddressSubPanel;
	@UiField
	VerticalPanel tradingAddressSubPanel;
	@UiField
	VerticalPanel tradingAddressPanel;
	@UiField
	VerticalPanel registeredAddressPanel;
	@UiField
	CheckBox isShowRegisteredAddressCheckBox;

	private static CompanyAddressOptionUiBinder uiBinder = GWT
			.create(CompanyAddressOptionUiBinder.class);

	interface CompanyAddressOptionUiBinder extends
			UiBinder<Widget, CompanyAddressOption> {
	}

	public CompanyAddressOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		ClientAddress tradingAddress = getCompany().getTradingAddress();
		if (tradingAddress != null) {
			tAddress1TextBox.setValue(tradingAddress.getAddress1());
			tAddress2TextBox.setValue(tradingAddress.getStreet());
			tCityTextBox.setValue(tradingAddress.getCity());
			tPostalCodeTextBox.setValue(tradingAddress.getZipOrPostalCode());
		}
		ClientAddress registeredAddress = getCompany().getRegisteredAddress();
		if (registeredAddress == null) {
			registeredAddress = tradingAddress;
		}
		if (registeredAddress == null) {
			return;
		}
		this.rAddress1TextBox.setValue(registeredAddress.getAddress1());
		this.rAddress2TextBox.setValue(registeredAddress.getStreet());
		this.rCityTextBox.setValue(registeredAddress.getCity());
		this.rPostalCodeTextbox
				.setValue(registeredAddress.getZipOrPostalCode());
		this.isShowRegisteredAddressCheckBox.setValue(getCompanyPreferences()
				.isShowRegisteredAddress());
		registeredAddressPanel.setVisible(getCompanyPreferences()
				.isShowRegisteredAddress());
	}

	public void createControls() {
		List<String> countriesList = CoreUtils.getCountriesAsList();

		tradingAddressDescription
				.setText(constants.tradingAddressDescription());
		tradingAddressDescription.setStyleName("organisation_comment");
		tradingAddressTitle.setText(constants.tradingAddress());
		tAddress1Label.setText(constants.address1());
		tAddress2Label.setText(constants.address2());
		tCityLabel.setText(constants.city());
		tStateLabel.setText(constants.state());
		tPostalCodeLabel.setText(constants.postalCode());
		tCountryLabel.setText(constants.country());

		registeredAddressDescription.setText(constants
				.registeredAddresDescription());
		registeredAddressDescription.setStyleName("organisation_comment");
		registeredAddressTitle.setText(constants.registeredAddress());
		rAddress1Label.setText(constants.address1());
		rAddress2Label.setText(constants.address2());
		rCityLabel.setText(constants.city());
		rStateComboLabel.setText(constants.state());
		rPostalCodeLabel.setText(constants.postalCode());
		rCountryComboLabel.setText(constants.country());

		for (int i = 0; i < countriesList.size(); i++) {
			tCountryCombo.addItem(countriesList.get(i));
			rCountryCombo.addItem(countriesList.get(i));
		}
		if (getCompany().getTradingAddress() != null
				&& getCompany().getTradingAddress().getCountryOrRegion() != null) {
			tCountryCombo.setSelectedIndex(countriesList.indexOf(getCompany()
					.getTradingAddress().getCountryOrRegion()));
		}
		ClientAddress addr = getCompany().getRegisteredAddress();
		if (addr == null) {
			addr = getCompany().getTradingAddress();
		}
		if (addr != null && addr.getCountryOrRegion() != null) {
			rCountryCombo.setSelectedIndex(countriesList.indexOf(getCompany()
					.getTradingAddress().getCountryOrRegion()));
		}
		tCountryCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				tCountryChanged();
			}
		});
		tCountryChanged();
		rCountryCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				rCountryChanged();
			}
		});
		rCountryChanged();
		isShowRegisteredAddressCheckBox.setText(constants
				.registeredAddressComment());
	}

	private void rCountryChanged() {
		int selectedCountry = rCountryCombo.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		String countryName = rCountryCombo.getItemText(selectedCountry);
		List<String> states = CoreUtils.getStatesAsListForCountry(countryName);
		rStateCombo.clear();
		for (String state : states) {
			rStateCombo.addItem(state);
		}
		ClientAddress address = getCompany().getRegisteredAddress();
		if (address != null && address.getStateOrProvinence() != null) {
			if (states.contains(address.getStateOrProvinence())) {
				rStateCombo.setSelectedIndex(states.indexOf(address
						.getStateOrProvinence()));
			}
		}
	}

	private void tCountryChanged() {
		int selectedCountry = tCountryCombo.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		String countryName = tCountryCombo.getItemText(selectedCountry);
		List<String> states = CoreUtils.getStatesAsListForCountry(countryName);
		tStateCombo.clear();
		for (String state : states) {
			tStateCombo.addItem(state);
		}
		ClientAddress tradingAddress = getCompanyPreferences()
				.getTradingAddress();
		if (tradingAddress != null
				&& tradingAddress.getStateOrProvinence() != null) {
			if (states.contains(tradingAddress.getStateOrProvinence())) {
				tStateCombo.setSelectedIndex(states.indexOf(tradingAddress
						.getStateOrProvinence()));
			}
		}
	}

	public CompanyAddressOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return Accounter.constants().address();
	}

	@Override
	public void onSave() {
		ClientAddress tAddress = getCompany().getPreferences()
				.getTradingAddress();
		if (tAddress == null) {
			tAddress = new ClientAddress();
		}
		tAddress.setAddress1(tAddress1TextBox.getValue());
		tAddress.setStreet(tAddress2TextBox.getValue());
		tAddress.setCity(tCityTextBox.getValue());
		if (tStateCombo.getSelectedIndex() > 0) {
			tAddress.setStateOrProvinence(tStateCombo.getItemText(tStateCombo
					.getSelectedIndex()));
		}
		tAddress.setZipOrPostalCode(tPostalCodeTextBox.getValue());
		if (tCountryCombo.getSelectedIndex() > 0) {
			tAddress.setCountryOrRegion(tCountryCombo.getItemText(tCountryCombo
					.getSelectedIndex()));
		}
		ClientAddress rAddress = getCompany().getRegisteredAddress();
		if (rAddress == null) {
			rAddress = new ClientAddress();
		}
		rAddress.setAddress1(rAddress1TextBox.getValue());
		rAddress.setStreet(rAddress2TextBox.getValue());
		rAddress.setCity(rCityTextBox.getValue());
		rAddress.setZipOrPostalCode(rPostalCodeTextbox.getValue());

		this.rPostalCodeTextbox.setValue(rAddress.getZipOrPostalCode());
		if (rStateCombo.getSelectedIndex() > 0) {
			rAddress.setStateOrProvinence(rStateCombo.getItemText(rStateCombo
					.getSelectedIndex()));
		}
		if (rCountryCombo.getSelectedIndex() > 0) {
			rAddress.setCountryOrRegion(rCountryCombo.getItemText(rCountryCombo
					.getSelectedIndex()));
		}
		getCompany().setRegisteredAddress(rAddress);
		getCompanyPreferences().setShowRegisteredAddress(
				isShowRegisteredAddressCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return constants.address();
	}

	@UiHandler("isShowRegisteredAddressCheckBox")
	void onIsShowRegisteredAddressCheckBoxValueChange(
			ValueChangeEvent<Boolean> event) {
		registeredAddressPanel.setVisible(event.getValue());
	}
}
