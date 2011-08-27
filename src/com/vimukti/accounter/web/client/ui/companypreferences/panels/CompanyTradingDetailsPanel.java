package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyTradingDetailsPanel extends VerticalPanel {

	private TextItem tradingCompanyName, address1Text, address2Text, cityText,
			stateText, postalcodeText, countryText;

	public CompanyTradingDetailsPanel(
			ClientCompanyPreferences companyPreferences) {
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

		stateText = new TextItem(Accounter.constants().state());
		stateText.setHelpInformation(true);
		stateText.setRequired(false);
		stateText.setWidth(100);

		postalcodeText = new TextItem(Accounter.constants().postalCode());
		postalcodeText.setHelpInformation(true);
		postalcodeText.setRequired(false);
		postalcodeText.setWidth(100);

		countryText = new TextItem(Accounter.constants().postalCode());
		countryText.setHelpInformation(true);
		countryText.setRequired(false);
		countryText.setWidth(100);

		DynamicForm companyForm = new DynamicForm();
		companyForm.setWidth("80%");
		companyForm.getCellFormatter().setWidth(0, 0, "225px");
		companyForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		companyForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");

		Label addressLabel = new Label(Accounter.constants().tradingAddress());

		companyForm.setFields(tradingCompanyName);

		DynamicForm addressForm = new DynamicForm();

		addressForm.setWidth("80%");
		addressForm.getCellFormatter().setWidth(0, 0, "225px");
		addressForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		addressForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");

		addressForm.setFields(address1Text, address2Text, cityText, stateText,
				postalcodeText, countryText);

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

}
