package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
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

		Label addressLabel = new Label(constants.registeredAddress());
		companyForm.setFields(registeredCompanyName);

		DynamicForm subAddressForm = new DynamicForm();
		VerticalPanel addressPanel = new VerticalPanel();

		subAddressForm.setFields(address1Text, address2Text, cityText,
				postalcodeText, countryCombo, stateCombo);

		addressPanel.add(addressLabel);
		addressPanel.add(subAddressForm);
		
		addressLabel.addStyleName("header");
		subAddressForm.getElement().getStyle().setPaddingLeft(150, Unit.PX);
		subAddressForm.addStyleName("fullSizePanel");

		VerticalPanel mainPanel = new VerticalPanel();

		companyForm.addStyleName("companyInfoPanel");
		addressPanel.addStyleName("companyInfoPanel");

		mainPanel.add(companyForm);
		mainPanel.add(addressPanel);

		mainPanel.setSize("100%", "100%");
		mainPanel.setSpacing(8);
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
