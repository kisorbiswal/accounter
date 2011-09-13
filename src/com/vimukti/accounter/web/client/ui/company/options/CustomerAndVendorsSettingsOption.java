/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti2
 * 
 */
public class CustomerAndVendorsSettingsOption extends AbstractPreferenceOption {

	private static CustomerAndVendorsSettingsOptionUiBinder uiBinder = GWT
			.create(CustomerAndVendorsSettingsOptionUiBinder.class);

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

	interface CustomerAndVendorsSettingsOptionUiBinder extends
			UiBinder<Widget, CustomerAndVendorsSettingsOption> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public CustomerAndVendorsSettingsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public CustomerAndVendorsSettingsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	private void initData() {
		if (companyPreferences.isChargeSalesTax()) {
			chargeTaxYesRadioButton.setValue(true);
			chargeTaxNoRadioButton.setValue(false);
		} else {
			chargeTaxYesRadioButton.setValue(false);
			chargeTaxNoRadioButton.setValue(true);
		}
		if (companyPreferences.isKeepTrackofBills()) {
			managingBillYesRadioButton.setValue(true);
			managingBillNoRadioButton.setValue(false);
		} else {
			managingBillYesRadioButton.setValue(false);
			managingBillNoRadioButton.setValue(true);
		}
		if (companyPreferences.isDoyouwantEstimates()) {
			createEstimatesYesRadioButton.setValue(true);
			createEstimatesNoRadioButton.setValue(false);
		} else {
			createEstimatesYesRadioButton.setValue(false);
			createEstimatesNoRadioButton.setValue(true);
		}
		if (companyPreferences.isDoyouwantstatements()) {
			usingStatementsYesRadioButton.setValue(true);
			usingStatementsNoRadioButton.setValue(false);
		} else {
			usingStatementsYesRadioButton.setValue(false);
			usingStatementsNoRadioButton.setValue(true);
		}

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

	}

	@Override
	public String getTitle() {

		return constants.customerAndvendorSettings();
	}

	@Override
	public void onSave() {
		if (chargeTaxYesRadioButton.getValue()) {
			companyPreferences.setChargeSalesTax(true);
		} else {
			companyPreferences.setChargeSalesTax(false);
		}
		if (managingBillYesRadioButton.getValue()) {
			companyPreferences.setKeepTrackofBills(true);
		} else {
			companyPreferences.setKeepTrackofBills(false);
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

	}

	@Override
	public String getAnchor() {
		return constants.customerAndvendorSettings();
	}

}
