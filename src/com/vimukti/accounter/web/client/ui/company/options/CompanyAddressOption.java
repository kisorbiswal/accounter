package com.vimukti.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

	// @UiField
	// CheckBox companyLegalCheckBox;

	@UiField
	VerticalPanel companyCustomerPanel;

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

	private void initData() {
		ClientAddress tradingAddress = company.getTradingAddress();
		if (tradingAddress != null) {
			tAddress1TextBox.setValue(tradingAddress.getAddress1());
			tAddress2TextBox.setValue(tradingAddress.getStreet());
			tCityTextBox.setValue(tradingAddress.getCity());
			tPostalCodeTextBox.setValue(tradingAddress.getZipOrPostalCode());
		}
		ClientAddress registeredAddress = company.getRegisteredAddress();
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

	}

	private void createControls() {
		List<String> countriesList = CoreUtils.getCountriesAsList();

		tradingAddressTitle.setText(constants.tradingAddress());
		tAddress1Label.setText(constants.address1());
		tAddress2Label.setText(constants.address2());
		tCityLabel.setText(constants.city());
		tStateLabel.setText(constants.state());
		tPostalCodeLabel.setText(constants.postalCode());
		tCountryLabel.setText(constants.country());

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
		if (company.getTradingAddress() != null
				&& company.getTradingAddress().getCountryOrRegion() != null) {
			tCountryCombo.setSelectedIndex(countriesList.indexOf(company
					.getTradingAddress().getCountryOrRegion()));
		}
		ClientAddress addr = company.getRegisteredAddress();
		if (addr == null) {
			addr = company.getTradingAddress();
		}
		if (addr != null && addr.getCountryOrRegion() != null) {
			rCountryCombo.setSelectedIndex(countriesList.indexOf(company
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

		// companyLegalCheckBox.setText(Accounter.constants()
		// .getCompanyLegalCheckBoxText());
		// companyLegalCheckBox.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// companyCustomerPanel.setVisible(companyLegalCheckBox.getValue());
		// }
		// });

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
		ClientAddress address = company.getRegisteredAddress();
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
		ClientAddress tradingAddress = companyPreferences.getTradingAddress();
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
		ClientAddress tAddress = company.getPreferences().getTradingAddress();
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
		ClientAddress rAddress = company.getRegisteredAddress();
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
		company.setRegisteredAddress(rAddress);

	}

	@Override
	public String getAnchor() {

		return null;
	}

}
