package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.google.gwt.user.client.ui.CheckBox;

public class CompanyAddressOption extends AbstractPreferenceOption {

	@UiField
	Label registeredAddress;

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
	Label customerHeaderLabel;

	@UiField
	Label customerAddress1Label;

	@UiField
	TextBox customerAddress1TextBox;

	@UiField
	Label customerAddress2Label;

	@UiField
	TextBox customerAddress2TextBox;

	@UiField
	Label customerCityLabel;

	@UiField
	TextBox customerCityTextBox;

	@UiField
	Label customerPostalCodeLabel;

	@UiField
	TextBox customerPostalCodeTextbox;

	@UiField
	Label customerCountryComboLabel;

	@UiField
	ListBox custmerComboBox;

	@UiField
	Label customerStateComboLabel;

	@UiField
	ListBox customerStateComboBox;

//	@UiField
//	CheckBox companyLegalCheckBox;

	@UiField
	VerticalPanel companyCustomerPanel;

	ArrayList<String> countriesList;

	private List<String> states;

	private ClientAddress address;

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
		address1TextBox.setValue(company.getTradingAddress().getAddress1());
		address2TextBox.setValue(company.getTradingAddress().getStreet());
		cityTextBox.setValue(company.getTradingAddress().getCity());
		stateCombo.setSelectedIndex(states.indexOf(company.getTradingAddress()
				.getStateOrProvinence()));
		postalCodeTextBox.setValue(company.getTradingAddress()
				.getZipOrPostalCode());
		countryCombo.setSelectedIndex(countriesList.indexOf(company
				.getTradingAddress().getCountryOrRegion()));
		address = company.getRegisteredAddress();
		if (address != null) {
			this.customerAddress1TextBox.setValue(address.getAddress1());
			this.customerAddress2TextBox.setValue(address.getStreet());
			this.customerCityTextBox.setValue(address.getCity());
			this.customerPostalCodeTextbox.setValue(address
					.getZipOrPostalCode());
			if (address.getStateOrProvinence() != ""
					&& address.getStateOrProvinence() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.customerStateComboBox.setSelectedIndex(states
						.indexOf(address.getStateOrProvinence()));
			}
			if (address.getCountryOrRegion() != ""
					&& address.getCountryOrRegion() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.custmerComboBox.setSelectedIndex(countriesList
						.indexOf(address.getCountryOrRegion()));
			}
		} else {
			this.customerAddress1TextBox.setValue(company.getTradingAddress()
					.getAddress1());
			this.customerAddress2TextBox.setValue(company.getTradingAddress()
					.getStreet());
			this.customerCityTextBox.setValue(company.getTradingAddress()
					.getCity());
			this.customerPostalCodeTextbox.setValue(company.getTradingAddress()
					.getZipOrPostalCode());
			if (address.getStateOrProvinence() != ""
					&& address.getStateOrProvinence() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.customerStateComboBox.setSelectedIndex(states
						.indexOf(company.getTradingAddress()
								.getStateOrProvinence()));
			}
			if (address.getCountryOrRegion() != ""
					&& address.getCountryOrRegion() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.custmerComboBox.setSelectedIndex(countriesList
						.indexOf(company.getTradingAddress()
								.getCountryOrRegion()));
			}
		}

	}

	private void createControls() {
		registeredAddress.setText(constants.tradingAddress());
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
				countryChanged1(countryCombo, stateCombo);
			}
		});
		countryChanged1(countryCombo, stateCombo);
//		companyLegalCheckBox.setText(Accounter.constants()
//				.getCompanyLegalCheckBoxText());
//		companyLegalCheckBox.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				companyCustomerPanel.setVisible(companyLegalCheckBox.getValue());
//			}
//		});
		customerHeaderLabel.setText(constants.registeredAddress());
		customerAddress1Label.setText(Accounter.constants().address1());
		customerAddress2Label.setText(Accounter.constants().address2());
		customerCityLabel.setText(Accounter.constants().city());
		customerStateComboLabel.setText(Accounter.constants().state());
		customerPostalCodeLabel.setText(Accounter.constants().postalCode());
		customerCountryComboLabel.setText(Accounter.constants().country());
		for (int i = 0; i < countriesList.size(); i++) {
			custmerComboBox.addItem(countriesList.get(i));
		}

		custmerComboBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (customerStateComboBox.getItemCount() != 0) {
					for (int i = 0; i < customerStateComboBox.getItemCount(); i++) {
						customerStateComboBox.removeItem(i);
					}
				}
				countryChanged(custmerComboBox, customerStateComboBox);
			}
		});
		countryChanged(custmerComboBox, customerStateComboBox);

	}

	private void countryChanged1(ListBox countryCombo2, ListBox stateCombo2) {
		int selectedCountry = countryCombo2.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		List<String> states = CoreUtils.getStatesAsListForCountry(countryCombo2
				.getItemText(selectedCountry));
		setStates1(states, stateCombo2);

	}

	private void setStates1(List<String> states2, ListBox stateCombo1) {
		this.states = states2;
		stateCombo1.clear();
		for (int i = 0; i < states.size(); i++) {
			stateCombo1.addItem(states.get(i));
		}
	}

	private void countryChanged(ListBox countryCombo2, ListBox stateCombo2) {
		int selectedCountry = countryCombo2.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		List<String> states = CoreUtils.getStatesAsListForCountry(countryCombo2
				.getItemText(selectedCountry));
		setStates(states, stateCombo2);

	}

	private void setStates(List<String> states2, ListBox stateCombo1) {
		this.states = states2;
		stateCombo1.clear();
		for (int i = 0; i < states.size(); i++) {
			stateCombo1.addItem(states.get(i));
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
		company.getTradingAddress().setAddress1(address1TextBox.getValue());
		company.getTradingAddress().setStreet(address2TextBox.getValue());
		company.getTradingAddress().setCity(cityTextBox.getValue());
		if (stateCombo.getSelectedIndex() > 0)
			company.getTradingAddress().setStateOrProvinence(
					states.get(stateCombo.getSelectedIndex()));
		company.getTradingAddress().setZipOrPostalCode(
				postalCodeTextBox.getValue());
		if (countryCombo.getSelectedIndex() > 0)
			company.getTradingAddress().setCountryOrRegion(
					countriesList.get(countryCombo.getSelectedIndex()));
		address = company.getRegisteredAddress();
		if (address != null) {
			this.customerAddress1TextBox.setValue(address.getAddress1());
			this.customerAddress2TextBox.setValue(address.getStreet());
			this.customerCityTextBox.setValue(address.getCity());
			this.customerPostalCodeTextbox.setValue(address
					.getZipOrPostalCode());
			if (address.getStateOrProvinence() != ""
					&& address.getStateOrProvinence() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.customerStateComboBox.setSelectedIndex(states
						.indexOf(address.getStateOrProvinence()));
			}
			if (address.getCountryOrRegion() != ""
					&& address.getCountryOrRegion() != null
					&& address.getStateOrProvinence().length() != 0) {
				this.custmerComboBox.setSelectedIndex(countriesList
						.indexOf(address.getCountryOrRegion()));
			}
		}

	}

	@Override
	public String getAnchor() {

		return null;
	}

}
