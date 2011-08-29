package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyOtherDetailsPanel extends AbstractCompanyInfoPanel {
	private SelectCombo industryCombo, organisationCombo;
	private IntegerField phoneField, faxField;
	private TextItem webTextItem;
	private EmailField emailField;
	private static List<String> industryList;
	private List<String> organisationList;

	public CompanyOtherDetailsPanel() {
		super();
		createControls();
	}

	private void createControls() {
		AccounterConstants constants = Accounter.constants();
		VerticalPanel mainPanel = new VerticalPanel();

		String[] organisation = new String[] {
				constants.soleProprietorshipDesc(),
				constants.partnershipOrLLPDesc(),
				constants.LLCDesc() + " " + constants.llcSingleMemberForm(),
				constants.LLCDesc() + " " + constants.llcMultiMemberForm(),
				constants.sCorporationDesc(), constants.corporationDesc(),
				constants.nonProfitDesc(), constants.otherNone() };

		DynamicForm contactDetailsForm = new DynamicForm();
		DynamicForm otherDetailsForm = new DynamicForm();

		LabelItem contactDetailsLabelItem = new LabelItem();
		contactDetailsLabelItem.setValue(constants.contactDetails());
		phoneField = new IntegerField(view, constants.phone());
		emailField = new EmailField(constants.email());
		faxField = new IntegerField(view, constants.fax());
		webTextItem = new TextItem(constants.webSite());

		LabelItem otherDetailsLabelItem = new LabelItem();
		otherDetailsLabelItem.setValue(constants.otherDetails());

		industryCombo = new SelectCombo(constants.industry());
		organisationCombo = new SelectCombo(constants.organisation());
		industryList = new ArrayList<String>();
		organisationList = new ArrayList<String>();

		Accounter.createGETService().getAccountsTemplate(
				new AccounterAsyncCallback<List<AccountsTemplate>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO
					}

					@Override
					public void onResultSuccess(List<AccountsTemplate> result) {

						for (AccountsTemplate template : result) {
							industryList.add(template.getName());
							industryCombo.addItem(template.getName());
						}
					}

				});
		for (int i = 0; i < organisation.length; i++) {
			organisationList.add(organisation[i]);
			organisationCombo.addItem(organisation[i]);
		}
		contactDetailsForm.setFields(contactDetailsLabelItem, phoneField,
				emailField, faxField, webTextItem);
		otherDetailsForm.setFields(otherDetailsLabelItem, industryCombo,
				organisationCombo);

		mainPanel.add(contactDetailsForm);
		mainPanel.add(otherDetailsForm);

		add(mainPanel);
	}

	@Override
	public void onLoad() {
		phoneField.setValue(company.getPhone());
		emailField.setValue(company.getCompanyEmail());
		webTextItem.setValue(company.getWebSite());
		faxField.setValue(company.getFax());
		if (industryList != null && industryList.size() != 0)
			industryCombo.setSelected(industryList.get(company.getIndustry()));
		organisationCombo.setSelected(organisationList.get(companyPreferences
				.getOrganizationType()));

	}

	@Override
	public void onSave() {
		company.setPhone(phoneField.getValue());
		company.setCompanyEmail(emailField.getValue());
		company.setWebSite(webTextItem.getValue());
		company.setFax(faxField.getValue());
		company.setIndustry(industryList.indexOf(industryCombo
				.getSelectedValue()));
		companyPreferences.setOrganizationType(organisationList
				.indexOf(organisationCombo.getSelectedValue()));
	}
}
