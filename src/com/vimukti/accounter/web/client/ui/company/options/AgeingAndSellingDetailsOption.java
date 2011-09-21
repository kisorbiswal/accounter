package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

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

	interface AgeingAndSellingDetailsOptionUiBinder extends
			UiBinder<Widget, AgeingAndSellingDetailsOption> {
	}

	public AgeingAndSellingDetailsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		if (getCompanyPreferences().getAgeingFromTransactionDateORDueDate() == 1) {
			ageingforduedateRadioButton.setValue(true);
			ageingfortransactiondateRadioButton.setValue(false);
		} else {
			ageingforduedateRadioButton.setValue(false);
			ageingfortransactiondateRadioButton.setValue(true);
		}
	}

	public void createControls() {

		ageingforduedateRadioButton.setText(constants.ageingforduedate());
		ageingfortransactiondateRadioButton.setText(constants
				.ageingfortransactiondate());
		ageingLabel.setText(constants.ageingDetails());
		ageingdescriptionLabel.setText(constants.agingDetailsDescription());
		ageingdescriptionLabel.setStyleName("organisation_comment");
	}

	public AgeingAndSellingDetailsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return constants.ageingAndSellingDetails();
	}

	@Override
	public void onSave() {
		if (ageingforduedateRadioButton.getValue())
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(1);
		else
			getCompanyPreferences().setAgeingFromTransactionDateORDueDate(2);

	}

	@Override
	public String getAnchor() {
		return constants.ageingAndSellingDetails();
	}

}
