package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class CompanyRegisteredDetailsPage extends AbstractCompanyInfoPanel {

	private static CompanyRegisteredDetailsPageUiBinder uiBinder = GWT
			.create(CompanyRegisteredDetailsPageUiBinder.class);

	// // @UiField
	// VerticalPanel viewPanel;
	//
	// // @UiField
	// TextBox registeredCompanyName;
	//
	// // @UiField
	// TextBox address1Text;
	//
	// // @UiField
	// TextBox address2Text;
	//
	// // @UiField
	// TextBox cityText;
	//
	// // @UiField
	// TextField postalcodeText;
	//
	// // @UiField
	// ListBox countryCombo;
	//
	// // @UiField
	// ListBox stateCombo;
	//
	// // @UiField
	// Label registeredCompanyNameLabel;
	//
	// // @UiField
	// Label address1TextLabel;
	//
	// // @UiField
	// Label address2TextLabel;
	//
	// // @UiField
	// Label cityTextLabel;
	//
	// // @UiField
	// Label postalcodeTextLabel;
	//
	// // @UiField
	// Label countryComboLabel;
	//
	// // @UiField
	// Label stateComboLabel;
	//
	// private List<String> countriesList, states;
	//
	// private ClientAddress address;
	//
	interface CompanyRegisteredDetailsPageUiBinder extends
			UiBinder<Widget, CompanyRegisteredDetailsPage> {
	}

	public CompanyRegisteredDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		// createControls();
	}

	public CompanyRegisteredDetailsPage(ClientCompanyPreferences preferences,
			ClientCompany clientCompany, CompanyInfoPage companyInfoPage) {
		initWidget(uiBinder.createAndBindUi(this));
		companyPreferences = preferences;
		company = clientCompany;
		// createControls();
	}

	//
	// private void createControls() {
	//
	// registeredCompanyNameLabel.setText(Accounter.constants()
	// .registeredCompanyName());
	// address1TextLabel.setText(Accounter.constants().address1());
	// address2TextLabel.setText(Accounter.constants().address2());
	// cityTextLabel.setText(Accounter.constants().city());
	// stateComboLabel.setText(Accounter.constants().state());
	// postalcodeTextLabel.setText(Accounter.constants().postalCode());
	// countryComboLabel.setText(Accounter.constants().country());
	// countriesList = new ArrayList<String>();
	// countriesList.addAll(CoreUtils.getCountriesAsList());
	// for (int i = 0; i < countriesList.size(); i++) {
	// countryCombo.addItem(countriesList.get(i));
	// }
	// countryCombo.addChangeHandler(new ChangeHandler() {
	//
	// @Override
	// public void onChange(ChangeEvent event) {
	// if (stateCombo.getItemCount() != 0) {
	// for (int i = 0; i < stateCombo.getItemCount(); i++) {
	// stateCombo.removeItem(i);
	// }
	// }
	// countryChanged();
	// }
	// });
	// countryChanged();
	// }
	//
	// @Override
	// public void onLoad() {
	// registeredCompanyName.setValue(company.getDisplayName());
	// address = company.getTradingAddress();
	// if (address != null) {
	// this.address1Text.setValue(address.getAddress1());
	// this.address2Text.setValue(address.getStreet());
	// this.cityText.setValue(address.getCity());
	// if (address.getStateOrProvinence() != ""
	// && address.getStateOrProvinence() != null
	// && address.getStateOrProvinence().length() != 0) {
	// this.stateCombo.setSelectedIndex(states.indexOf(address
	// .getStateOrProvinence()));
	// }
	// if (address.getCountryOrRegion() != ""
	// && address.getCountryOrRegion() != null
	// && address.getStateOrProvinence().length() != 0) {
	// this.countryCombo.setSelectedIndex(countriesList
	// .indexOf(address.getCountryOrRegion()));
	// }
	// }
	// }

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void onSave() {
	// address = new ClientAddress();
	// company.setTradingName(registeredCompanyName.getValue());
	// address.setAddress1(address1Text.getValue());
	// address.setStreet(address2Text.getValue());
	// address.setCity(cityText.getValue());
	// if (stateCombo.getSelectedIndex() != -1) {
	// address.setStateOrProvinence(states.get(stateCombo
	// .getSelectedIndex()));
	// }
	// if (countryCombo.getSelectedIndex() != -1)
	// address.setCountryOrRegion(countriesList.get(countryCombo
	// .getSelectedIndex()));
	// company.setTradingAddress(address);
	// Accounter.setCompany(company);
	// }
	//
	// private void countryChanged() {
	// int selectedCountry = countryCombo.getSelectedIndex();
	// if (selectedCountry < 0) {
	// return;
	// }
	// List<String> states = CoreUtils.getStatesAsListForCountry(countryCombo
	// .getItemText(selectedCountry));
	// setStates(states);
	//
	// }
	//
	// private void setStates(List<String> states2) {
	// this.states = states2;
	// stateCombo.clear();
	// for (int i = 0; i < states.size(); i++) {
	// stateCombo.addItem(states.get(i));
	// }
	//
	// }

}
