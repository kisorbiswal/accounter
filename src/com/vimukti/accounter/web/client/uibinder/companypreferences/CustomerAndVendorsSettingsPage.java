package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class CustomerAndVendorsSettingsPage extends AbstractCompanyInfoPanel {

	@UiField
	Label chargeTaxLabelItem;
	@UiField
	RadioButton chargeTaxYesRadioButton;
	@UiField
	RadioButton chargeTaxNoRadioButton;
	@UiField
	Label managingBillLabelItem;
	@UiField
	RadioButton managingBillYesRadioButton;
	@UiField
	RadioButton managingBillNoRadioButton;
	@UiField
	Label createEstimatesLabelItem;
	@UiField
	RadioButton createEstimatesYesRadioButton;
	@UiField
	RadioButton createEstimatesNoRadioButton;
	@UiField
	Label usingStatementsLabelItem;
	@UiField
	RadioButton usingStatementsYesRadioButton;
	@UiField
	RadioButton usingStatementsNoRadioButton;
	@UiField
	Label billsLabel;
	@UiField
	RadioButton billsYesRadioButton;
	@UiField
	RadioButton billsNoRadioButton;
	private static CustomerAndVendorsSettingsPageUiBinder uiBinder = GWT
			.create(CustomerAndVendorsSettingsPageUiBinder.class);

	interface CustomerAndVendorsSettingsPageUiBinder extends
			UiBinder<Widget, CustomerAndVendorsSettingsPage> {
	}

	public CustomerAndVendorsSettingsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		chargeTaxLabelItem.setText(constants.doyouchargesalestax());

		chargeTaxYesRadioButton.setText(constants.yes());
		chargeTaxNoRadioButton.setText(constants.no());

		managingBillLabelItem.setText(constants.managingBills());

		managingBillYesRadioButton.setText(constants.yes());
		managingBillNoRadioButton.setText(constants.no());

		createEstimatesLabelItem.setText(constants
				.wanttoCreateEstimatesInAccounter());

		createEstimatesYesRadioButton.setText(constants.yes());
		createEstimatesNoRadioButton.setText(constants.no());

		usingStatementsLabelItem.setText(constants.doyouWantToUseStatements());

		usingStatementsYesRadioButton.setText(constants.yes());
		usingStatementsNoRadioButton.setText(constants.no());

		billsLabel.setText(constants.doyouwantTrackBills());

		billsYesRadioButton.setText(constants.yes());
		billsNoRadioButton.setText(constants.no());

	}

	@Override
	public void onLoad() {
		if (companyPreferences.isDoYouChargesalesTax()) {
			chargeTaxYesRadioButton.setValue(true);
		} else {
			chargeTaxNoRadioButton.setValue(false);
		}
		if (companyPreferences.isDoyouKeepTrackofBills()) {
			managingBillYesRadioButton.setValue(true);
		} else {
			managingBillNoRadioButton.setValue(false);
		}
		if (companyPreferences.isDoyouwantEstimates()) {
			createEstimatesYesRadioButton.setValue(true);
		} else {
			createEstimatesNoRadioButton.setValue(false);
		}
		if (companyPreferences.isDoyouwantstatements()) {
			usingStatementsYesRadioButton.setValue(true);
		} else {
			usingStatementsNoRadioButton.setValue(false);
		}
		if (companyPreferences.isDoyouKeepTrackofBills()) {
			billsYesRadioButton.setValue(true);
		} else {
			billsNoRadioButton.setValue(false);
		}
	}

	@Override
	public void onSave() {
		if (chargeTaxYesRadioButton.getValue()) {
			companyPreferences.setDoYouPaySalesTax(true);
		} else {
			companyPreferences.setDoYouChargesalesTax(false);
		}
		if (managingBillYesRadioButton.getValue()) {
			companyPreferences.setDoyouKeepTrackofBills(true);
		} else {
			companyPreferences.setDoyouKeepTrackofBills(false);
		}
		if (createEstimatesYesRadioButton.getValue()) {
			companyPreferences.setDoyouwantEstimates(true);
		} else {
			companyPreferences.setDoyouwantEstimates(false);
		}
		if (usingStatementsYesRadioButton.getValue()) {
			companyPreferences.setDoyouwantstatements(true);
		} else {
			companyPreferences.setDoyouwantstatements(false);
		}
		if (billsYesRadioButton.getValue()) {
			companyPreferences.setDoyouKeepTrackofBills(true);
		} else {
			companyPreferences.setDoyouKeepTrackofBills(false);
		}
	}

}
