package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
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
	private SelectCombo countryCombo, stateCombo;
	private ClientUserInfo admin;

	public AdminInfoPanel(ClientCompanyPreferences companyPreferences,
			ClientCompany company, AbstractBaseView view) {
		super(companyPreferences, company, view);
		createControls();
	}

	private void createControls() {
		List<ClientUserInfo> clientUsers = company.getUsersList();
		for (int i = 0; i < clientUsers.size(); i++) {
			if (clientUsers.get(i).isAdmin())
				admin = clientUsers.get(i);
		}
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
		firstNameTextItem.setValue(admin.getFirstName());
		lastNameTextItem.setValue(admin.getLastName());
		// address1TextItem.setValue(admin.get)
		// phoneField.setValue(admin.get)
		emailField.setValue(admin.getEmail());
	}

	@Override
	public void onSave() {
		admin.setFirstName(firstNameTextItem.getValue());
		admin.setLastName(lastNameTextItem.getValue());
		admin.setEmail(emailField.getValue());

	}

}
