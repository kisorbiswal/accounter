package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class CompanyTradingDetailsPage extends AbstractCompanyInfoPanel {

	private static CompanyTradingDetailsPageUiBinder uiBinder = GWT
			.create(CompanyTradingDetailsPageUiBinder.class);

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	// // @UiField
	// VerticalPanel viewPanel;
	//
	// // @UiField
	// TextBox tradingCompanyName;
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
	// Label tradingCompanyNameLabel;
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
	interface CompanyTradingDetailsPageUiBinder extends
			UiBinder<Widget, CompanyTradingDetailsPage> {
	}

	public CompanyTradingDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		// createControls();
	}
	//
	// private void createControls() {
	// tradingCompanyNameLabel.setText(Accounter.constants()
	// .tradingCompanyName());
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
	// tradingCompanyName.setValue(company.getTradingName());
	// address1Text.setValue(company.getTradingAddress().getAddress1());
	// address2Text.setValue(company.getTradingAddress().getStreet());
	// cityText.setValue(company.getTradingAddress().getCity());
	// stateCombo.setSelectedIndex(states.indexOf(company.getTradingAddress()
	// .getStateOrProvinence()));
	// countryCombo.setSelectedIndex(countriesList.indexOf(company
	// .getTradingAddress().getCountryOrRegion()));
	//
	// }
	//
	// @Override
	// public void onSave() {
	// company.setTradingName(tradingCompanyName.getValue());
	// company.getTradingAddress().setAddress1(address1Text.getValue());
	// company.getTradingAddress().setStreet(address2Text.getValue());
	// company.getTradingAddress().setCity(cityText.getValue());
	// company.getTradingAddress().setStateOrProvinence(
	// states.get(stateCombo.getSelectedIndex()));
	// company.getTradingAddress().setCountryOrRegion(
	// countriesList.get(countryCombo.getSelectedIndex()));
	//
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
