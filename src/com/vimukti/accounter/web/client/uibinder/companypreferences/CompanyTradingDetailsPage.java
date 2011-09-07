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
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CompanyTradingDetailsPage extends AbstractCompanyInfoPanel {

	private static CompanyTradingDetailsPageUiBinder uiBinder = GWT
			.create(CompanyTradingDetailsPageUiBinder.class);
	@UiField
	Label tradingCompanyNameLabel;
	@UiField
	TextBox tradingCompanyName;
	@UiField
	Label tradingAddressLabel;
	@UiField
	Label address1TextLabel;
	@UiField
	TextBox address1Text;
	@UiField
	Label address2TextLabel;
	@UiField
	TextBox address2Text;
	@UiField
	Label cityTextLabel;
	@UiField
	TextBox cityText;
	@UiField
	Label postalcodeTextLabel;
	@UiField
	TextBox postalcodeText;
	@UiField
	Label countryComboLabel;
	@UiField
	ListBox countryCombo;
	@UiField
	Label stateComboLabel;
	@UiField
	ListBox stateCombo;

	private List<String> countriesList, states;


	interface CompanyTradingDetailsPageUiBinder extends
			UiBinder<Widget, CompanyTradingDetailsPage> {
	}

	public CompanyTradingDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		tradingAddressLabel.setText(constants.tradingAddress());
		tradingCompanyNameLabel.setText(Accounter.constants()
				.tradingCompanyName());
		address1TextLabel.setText(Accounter.constants().address1());
		address2TextLabel.setText(Accounter.constants().address2());
		cityTextLabel.setText(Accounter.constants().city());
		stateComboLabel.setText(Accounter.constants().state());

		ThemesUtil.addDivToListBox(countryCombo, countryComboLabel.getText());
		ThemesUtil.addDivToListBox(stateCombo, stateComboLabel.getText());

		postalcodeTextLabel.setText(Accounter.constants().postalCode());
		countryComboLabel.setText(Accounter.constants().country());
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
		tradingCompanyName.setValue(company.getTradingName());
		address1Text.setValue(company.getTradingAddress().getAddress1());
		address2Text.setValue(company.getTradingAddress().getStreet());
		cityText.setValue(company.getTradingAddress().getCity());
		stateCombo.setSelectedIndex(states.indexOf(company.getTradingAddress()
				.getStateOrProvinence()));
		postalcodeText.setValue(company.getTradingAddress()
				.getZipOrPostalCode());
		countryCombo.setSelectedIndex(countriesList.indexOf(company
				.getTradingAddress().getCountryOrRegion()));

	}

	@Override
	public void onSave() {
		company.setTradingName(tradingCompanyName.getValue());
		company.getTradingAddress().setAddress1(address1Text.getValue());
		company.getTradingAddress().setStreet(address2Text.getValue());
		company.getTradingAddress().setCity(cityText.getValue());
		company.getTradingAddress().setStateOrProvinence(
				states.get(stateCombo.getSelectedIndex()));
		company.getTradingAddress().setZipOrPostalCode(
				postalcodeText.getValue());
		company.getTradingAddress().setCountryOrRegion(
				countriesList.get(countryCombo.getSelectedIndex()));

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
