package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

public class AgeingAndSellingDetailsOption extends AbstractPreferenceOption {

	private static AgeingAndSellingDetailsOptionUiBinder uiBinder = GWT
			.create(AgeingAndSellingDetailsOptionUiBinder.class);
	@UiField
	Label ageingLabel;
	@UiField
	RadioButton ageingforduedateRadioButton;
	@UiField
	RadioButton ageingfortransactiondateRadioButton;
	@UiField
	Label ageingdescriptionLabel;
	@UiField
	CheckBox salesOrderCheckBox;

	interface AgeingAndSellingDetailsOptionUiBinder extends
			UiBinder<Widget, AgeingAndSellingDetailsOption> {
	}

	public AgeingAndSellingDetailsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().getAgeingFromTransactionDateORDueDate() == 1) {
			ageingforduedateRadioButton.setValue(true);
			ageingfortransactiondateRadioButton.setValue(false);
		} else {
			ageingforduedateRadioButton.setValue(false);
			ageingfortransactiondateRadioButton.setValue(true);
		}
		salesOrderCheckBox.setValue(getCompanyPreferences()
				.isSalesOrderEnabled());
	}

	@Override
	public void createControls() {

		ageingforduedateRadioButton.setText(messages.ageingforduedate());
		ageingfortransactiondateRadioButton.setText(messages
				.ageingfortransactiondate());
		ageingLabel.setText(messages.ageingDetails());
		ageingdescriptionLabel.setText(messages.agingDetailsDescription());
		ageingdescriptionLabel.setStyleName("organisation_comment");

		salesOrderCheckBox.setValue(Accounter
				.hasPermission(Features.SALSE_ORDER));
		salesOrderCheckBox.setText(messages.enablePreference(messages
				.salesOrder()));
		salesOrderCheckBox.setStyleName("bold");
	}

	public AgeingAndSellingDetailsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return messages.ageingAndSellingDetails();
	}

	@Override
	public void onSave() {
		if (ageingforduedateRadioButton.getValue())
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(1);
		else
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(2);
		getCompanyPreferences().setSalesOrderEnabled(
				salesOrderCheckBox.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.ageingAndSellingDetails();
	}

}
