package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class CompanyEmailOption extends AbstractPreferenceOption {

	private static CompanyEmailOptionUiBinder uiBinder = GWT
			.create(CompanyEmailOptionUiBinder.class);
	@UiField
	Label companyEmailHeaderLabel;
	@UiField
	TextBox companyEmailTextBox;
//	@UiField
//	CheckBox customersEmailAddressCheckBox;
//	@UiField
//	VerticalPanel companyLegalAddressPanel;
//	@UiField
//	Label customerEmailHeaderLabel;
//	@UiField
//	TextBox customerEmailTextBox;

	interface CompanyEmailOptionUiBinder extends
			UiBinder<Widget, CompanyEmailOption> {
	}

	public CompanyEmailOption() {
		initWidget(uiBinder.createAndBindUi(this));
		creatControls();
		initData();
	}

	private void initData() {
		companyEmailTextBox.setText(company.getCompanyEmail());
//		customerEmailTextBox.setText(company.getCompanyEmailForCustomers());
	}

	private void creatControls() {
		companyEmailHeaderLabel.setText(Accounter.constants().emailId());
//		customersEmailAddressCheckBox.setText(Accounter.constants()
//				.getCustomersEmailId());
//		customersEmailAddressCheckBox.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				companyLegalAddressPanel
//						.setVisible(customersEmailAddressCheckBox.getValue());
//
//			}
//		});
//		customerEmailHeaderLabel.setText(Accounter.constants().customerID());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().email();
	}

	@Override
	public void onSave() {
		company.setCompanyEmail(companyEmailTextBox.getValue());
//		company.setCompanyEmailForCustomers(customerEmailTextBox.getValue());
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().email();
	}

}
