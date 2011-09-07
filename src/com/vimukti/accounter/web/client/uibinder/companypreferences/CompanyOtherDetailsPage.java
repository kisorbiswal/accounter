package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class CompanyOtherDetailsPage extends AbstractCompanyInfoPanel {

	private static CompanyOtherDetailsPageUiBinder uiBinder = GWT
			.create(CompanyOtherDetailsPageUiBinder.class);
	@UiField
	Label contactDetailLabel;
	@UiField
	Label phoneTextBoxLabel;
	@UiField
	TextBox phoneTextBox;
	@UiField
	Label emailFieldBoxLabel;
	@UiField
	TextBox emailFieldBox;
	@UiField
	Label faxTextBoxLabel;
	@UiField
	TextBox faxTextBox;
	@UiField
	Label webTextBoxLabel;
	@UiField
	TextBox webTextBox;

	interface CompanyOtherDetailsPageUiBinder extends
			UiBinder<Widget, CompanyOtherDetailsPage> {
	}

	public CompanyOtherDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		contactDetailLabel.setText(constants.contactDetails());
		phoneTextBoxLabel.setText(constants.phone());
		emailFieldBoxLabel.setText(constants.email());
		faxTextBoxLabel.setText(constants.fax());
		webTextBoxLabel.setText(constants.webSite());
	}

	@Override
	public void onLoad() {
		phoneTextBox.setValue(company.getPhone());
		emailFieldBox.setValue(company.getCompanyEmail());
		webTextBox.setValue(company.getWebSite());
		faxTextBox.setValue(company.getFax());
	}

	@Override
	public void onSave() {
		company.setPhone(phoneTextBox.getValue());
		company.setCompanyEmail(emailFieldBox.getValue());
		company.setWebSite(webTextBox.getValue());
		company.setFax(faxTextBox.getValue());
	}

}
