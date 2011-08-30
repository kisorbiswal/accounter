package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
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
		tradingCompanyName.setWidth(100);

		address1Text = new TextItem(Accounter.constants().address1());
		address1Text.setHelpInformation(true);
		address1Text.setRequired(false);
		address1Text.setWidth(100);

		address2Text = new TextItem(Accounter.constants().address2());
		address2Text.setHelpInformation(true);
		address2Text.setRequired(false);
		address2Text.setWidth(100);

		cityText = new TextItem(Accounter.constants().city());
		cityText.setHelpInformation(true);
		cityText.setRequired(false);
		cityText.setWidth(100);

		stateCombo = new SelectCombo(Accounter.constants().state());
		stateCombo.setHelpInformation(true);
		stateCombo.setRequired(false);
		stateCombo.setWidth(100);

		postalcodeText = new TextItem(Accounter.constants().postalCode());
		postalcodeText.setHelpInformation(true);
		postalcodeText.setRequired(false);
		postalcodeText.setWidth(100);

		countryCombo = new SelectCombo(Accounter.constants().country());
		countryCombo.setHelpInformation(true);
		countryCombo.setRequired(false);
		countryCombo.setWidth(100);
		countriesList = new ArrayList<String>();
		countriesList.addAll(CoreUtils.getCountriesAsList());
		countryCombo.initCombo(countriesList);

		countryCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						statesList=new ArrayList<String>();
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

		Label addressLabel = new Label(Accounter.constants().tradingAddress());

		companyForm.setFields(tradingCompanyName);

		DynamicForm addressForm = new DynamicForm();

		addressForm.setFields(address1Text, address2Text, cityText,
				postalcodeText, countryCombo, stateCombo);

		HorizontalPanel companyHorzPanel = new HorizontalPanel();
		companyHorzPanel.setWidth("100%");
		companyHorzPanel.add(companyForm);

		HorizontalPanel addressHorzPanel = new HorizontalPanel();
		addressHorzPanel.setWidth("100%");
		addressHorzPanel.add(addressLabel);
		addressHorzPanel.add(addressForm);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(companyHorzPanel);
		mainPanel.add(addressHorzPanel);

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
		company.setTradingName(tradingCompanyName.getValue());
		company.getTradingAddress().setAddress1(address1Text.getValue());
		company.getTradingAddress().setStreet(address2Text.getValue());
		company.getTradingAddress().setCity(cityText.getValue());
		company.getTradingAddress().setStateOrProvinence(
				stateCombo.getSelectedValue());
		company.getTradingAddress().setCountryOrRegion(
				countryCombo.getSelectedValue());
	}

}
