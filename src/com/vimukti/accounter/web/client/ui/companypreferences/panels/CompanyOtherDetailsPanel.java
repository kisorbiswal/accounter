package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class CompanyOtherDetailsPanel extends AbstractCompanyInfoPanel {
	private SelectCombo industryCombo, organisationCombo;
	private IntegerField phoneField, faxField;
	private TextItem webTextItem;
	private EmailField emailField;
	private AbstractBaseView view;

	public CompanyOtherDetailsPanel(
			ClientCompanyPreferences companyPreferences, AbstractBaseView view) {
		this.view = view;
		createControls();
	}

	private void createControls() {
		AccounterConstants constants = Accounter.constants();
		VerticalPanel mainPanel = new VerticalPanel();

		String[] organisation = new String[] {

		constants.soleProprietorshipDesc(), constants.partnershipOrLLPDesc(),
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

		Accounter.createGETService().getAccountsTemplate(
				new AccounterAsyncCallback<List<AccountsTemplate>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO
					}

					@Override
					public void onResultSuccess(List<AccountsTemplate> result) {

						for (AccountsTemplate template : result) {
							industryCombo.addItem(template.getName());
						}
					}

				});
		for (int i = 0; i < organisation.length; i++) {
			organisationCombo.addItem(organisation[i]);
		}
		contactDetailsForm.setFields(contactDetailsLabelItem, phoneField,
				emailField, faxField, webTextItem);
		otherDetailsForm.setFields(industryCombo, organisationCombo);

		mainPanel.add(contactDetailsForm);
		mainPanel.add(otherDetailsForm);

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
