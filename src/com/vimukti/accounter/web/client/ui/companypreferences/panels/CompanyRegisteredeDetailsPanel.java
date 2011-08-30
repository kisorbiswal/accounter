package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyRegisteredeDetailsPanel extends AbstractCompanyInfoPanel {

	private TextItem registeredCompanyName, address1Text, address2Text,
			cityText, postalcodeText;
	private SelectCombo countryCombo, stateCombo;
	private ArrayList<String> countriesList, statesList;
	protected String[] states;

	// public CompanyRegisteredeDetailsPanel() {
	// super();
	// createControls();
	// }

	public CompanyRegisteredeDetailsPanel(
			ClientCompanyPreferences companyPreferences, ClientCompany company,
			CompanyInfoView companyInfoView) {
		super(companyPreferences, company, view);
		createControls();
	}

	private void createControls() {

		registeredCompanyName = new TextItem(Accounter.constants()
				.registeredCompanyName());
		registeredCompanyName.setHelpInformation(true);
		registeredCompanyName.setRequired(false);
		registeredCompanyName.setWidth(100);

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
						statesList = new ArrayList<String>();
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

		Label addressLabel = new Label(Accounter.constants()
				.registeredAddress());

		companyForm.setFields(registeredCompanyName);

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
		registeredCompanyName.setValue(company.getDisplayName());
		address1Text.setValue(company.getRegisteredAddress().getAddress1());
		address2Text.setValue(company.getRegisteredAddress().getStreet());
		cityText.setValue(company.getRegisteredAddress().getCity());
		stateCombo.setSelected(company.getRegisteredAddress()
				.getStateOrProvinence());
		countryCombo.setSelected(company.getRegisteredAddress()
				.getCountryOrRegion());
	}

	@Override
	public void onSave() {
		company.setTradingName(registeredCompanyName.getValue());
		company.getRegisteredAddress().setAddress1(address1Text.getValue());
		company.getRegisteredAddress().setStreet(address2Text.getValue());
		company.getRegisteredAddress().setCity(cityText.getValue());
		company.getRegisteredAddress().setStateOrProvinence(
				stateCombo.getSelectedValue());
		company.getRegisteredAddress().setCountryOrRegion(
				countryCombo.getSelectedValue());
	}

}
