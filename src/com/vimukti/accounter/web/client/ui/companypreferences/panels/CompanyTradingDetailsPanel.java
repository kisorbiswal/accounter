package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyTradingDetailsPanel extends AbstractCompanyInfoPanel {

	private TextItem tradingCompanyName, address1Text, address2Text, cityText,
			postalcodeText;
	private SelectCombo stateCombo, countryCombo;
	private List<String> countriesList, statesList;
	protected String[] states;

	public CompanyTradingDetailsPanel() {
		createControls();
	}

	private void createControls() {
		tradingCompanyName = new TextItem(Accounter.constants()
				.tradingCompanyName());
		tradingCompanyName.setHelpInformation(true);
		tradingCompanyName.setRequired(false);

		address1Text = new TextItem(Accounter.constants().address1());
		address1Text.setHelpInformation(true);
		address1Text.setRequired(false);

		address2Text = new TextItem(Accounter.constants().address2());
		address2Text.setHelpInformation(true);
		address2Text.setRequired(false);

		cityText = new TextItem(Accounter.constants().city());
		cityText.setHelpInformation(true);
		cityText.setRequired(false);

		stateCombo = new SelectCombo(Accounter.constants().state());
		stateCombo.setHelpInformation(true);
		stateCombo.setRequired(false);

		postalcodeText = new TextItem(Accounter.constants().postalCode());
		postalcodeText.setHelpInformation(true);
		postalcodeText.setRequired(false);

		countryCombo = new SelectCombo(Accounter.constants().country());
		countryCombo.setHelpInformation(true);
		countryCombo.setRequired(false);
		countriesList = new ArrayList<String>();
		countriesList.addAll(CoreUtils.getCountriesAsList());
		countryCombo.initCombo(countriesList);

		countryCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						statesList = new ArrayList<String>();
						countryCombo.setSelected(selectItem);
						states = new String[CoreUtils
								.getStatesForCountry(selectItem).length];
						states = CoreUtils.getStatesForCountry(selectItem);
						for (int i = 0; i < states.length; i++) {
							statesList.add(states[i]);
						}
						stateCombo.initCombo(statesList);
					}
				});

		DynamicForm companyForm = new DynamicForm();

		Label addressLabel = new Label(constants.tradingAddress());

		companyForm.setFields(tradingCompanyName);

		DynamicForm subAddressForm = new DynamicForm();
		VerticalPanel addressPanel = new VerticalPanel();

		subAddressForm.setFields(address1Text, address2Text, cityText,
				postalcodeText, countryCombo, stateCombo);

		addressPanel.add(addressLabel);
		addressPanel.add(subAddressForm);

		addressLabel.addStyleName("header");
		subAddressForm.getElement().getStyle().setPaddingLeft(130, Unit.PX);
		subAddressForm.addStyleName("fullSizePanel");

		companyForm.addStyleName("companyInfoPanel");
		addressPanel.addStyleName("companyInfoPanel");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(companyForm);
		mainPanel.add(addressPanel);

		mainPanel.addStyleName("fullSizePanel");
		mainPanel.setSpacing(8);
		this.add(mainPanel);

	}

	@Override
	public void onLoad() {
		tradingCompanyName.setValue(company.getTradingName());
		address1Text.setValue(company.getTradingAddress().getAddress1());
		address2Text.setValue(company.getTradingAddress().getStreet());
		cityText.setValue(company.getTradingAddress().getCity());
		stateCombo.setSelected(company.getTradingAddress()
				.getStateOrProvinence());
		countryCombo.setSelected(company.getTradingAddress()
				.getCountryOrRegion());
	}

	@Override
	public void onSave() {
		company.setLegalName(tradingCompanyName.getValue());
		company.getTradingAddress().setAddress1(address1Text.getValue());
		company.getTradingAddress().setStreet(address2Text.getValue());
		company.getTradingAddress().setCity(cityText.getValue());
		company.getTradingAddress().setStateOrProvinence(
				stateCombo.getSelectedValue());
		company.getTradingAddress().setCountryOrRegion(
				countryCombo.getSelectedValue());
	}

}
