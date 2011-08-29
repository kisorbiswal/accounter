package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class AdminInfoPanel extends AbstractCompanyInfoPanel {
	private TextItem firstNameTextItem, lastNameTextItem, address1TextItem,
			address2TextItem, cityTextItem, webSiteTextItem;
	private IntegerField phoneField, postCodeField;
	private EmailField emailField;
	private AbstractBaseView view;
	private SelectCombo countryCombo, stateCombo;

	public AdminInfoPanel(ClientCompanyPreferences companyPreferences,
			AbstractBaseView view) {
		this.view = view;
		createControls();
	}

	private void createControls() {
		AccounterConstants constants = Accounter.constants();
		VerticalPanel mainPanel = new VerticalPanel();
		DynamicForm nameForm = new DynamicForm();
		DynamicForm addressForm = new DynamicForm();
		DynamicForm contactDetailForm = new DynamicForm();

		firstNameTextItem = new TextItem(constants.firstName());
		lastNameTextItem = new TextItem(constants.lastName());
		address1TextItem = new TextItem(constants.address1());
		address2TextItem = new TextItem(constants.address2());
		cityTextItem = new TextItem(constants.city());
		phoneField = new IntegerField(view, constants.phone());
		postCodeField = new IntegerField(view, constants.postalCode());
		emailField = new EmailField(constants.email());
		webSiteTextItem = new TextItem(constants.webSite());
		countryCombo = new SelectCombo(constants.country());
		stateCombo = new SelectCombo(constants.state());

		firstNameTextItem.setRequired(true);
		lastNameTextItem.setRequired(true);
		emailField.setRequired(true);

		nameForm.setFields(firstNameTextItem, lastNameTextItem);
		addressForm.setFields(address1TextItem, address2TextItem, cityTextItem,
				postCodeField, countryCombo, stateCombo);
		contactDetailForm.setFields(phoneField, emailField, webSiteTextItem);

		mainPanel.add(nameForm);
		mainPanel.add(addressForm);
		mainPanel.add(contactDetailForm);

		add(mainPanel);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

}
