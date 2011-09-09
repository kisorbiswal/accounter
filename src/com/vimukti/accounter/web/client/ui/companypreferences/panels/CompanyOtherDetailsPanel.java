package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CompanyOtherDetailsPanel extends AbstractCompanyInfoPanel {
	private IntegerField phoneField, faxField;
	private TextItem webTextItem;
	private EmailField emailField;

	public CompanyOtherDetailsPanel() {
		super();
		createControls();
	}

	private void createControls() {
		AccounterConstants constants = Accounter.constants();
		VerticalPanel mainPanel = new VerticalPanel();

		DynamicForm contactDetails2Form = new DynamicForm();

		Label contactDetailsLabelItem = new Label(constants.contactDetails());
		phoneField = new IntegerField(view, constants.phone());
		phoneField.setToolTip(Accounter.messages().phoneNumber(
				Accounter.constants().company()));
		emailField = new EmailField(constants.email());
		faxField = new IntegerField(view, constants.fax());
		webTextItem = new TextItem(constants.webSite());

		contactDetails2Form.setFields(phoneField, emailField, faxField,
				webTextItem);

		mainPanel.add(contactDetailsLabelItem);
		mainPanel.add(contactDetails2Form);
		contactDetailsLabelItem.addStyleName("header");

		contactDetails2Form.getElement().getStyle()
				.setPaddingLeft(120, Unit.PX);
		contactDetails2Form.addStyleName("fullSizePanel");
		mainPanel.addStyleName("companyInfoPanel");

		mainPanel.addStyleName("fullSizePanel");
		mainPanel.setSpacing(8);
		add(mainPanel);
	}

	@Override
	public void onLoad() {
		phoneField.setValue(company.getPhone());
		emailField.setValue(company.getCompanyEmail());
		webTextItem.setValue(company.getWebSite());
		faxField.setValue(company.getFax());

	}

	@Override
	public void onSave() {
		company.setPhone(phoneField.getValue());
		company.setCompanyEmail(emailField.getValue());
		company.setWebSite(webTextItem.getValue());
		company.setFax(faxField.getValue());

	}
}
