/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author vimukti2
 * 
 */
public class CustomerAndVendorsSettingsOption extends AbstractPreferenceOption {

	private static CustomerAndVendorsSettingsOptionUiBinder uiBinder = GWT
			.create(CustomerAndVendorsSettingsOptionUiBinder.class);
	@UiField
	Label salesTaxDescriptionLabel;
	@UiField
	Label VatdescritionLabel;
	@UiField
	Label chargeTaxLabelItem;
	@UiField
	RadioButton chargeTaxYesRadioButton;
	@UiField
	RadioButton chargeTaxNoRadioButton;

	@UiField
	RadioButton onepeTransactionRadioButton;
	@UiField
	Label oneperTransactionLabel;
	@UiField
	RadioButton oneperdetaillineRadioButton;
	@UiField
	Label oneperdetaillineLabel;
	@UiField
	CheckBox enableTaxCheckbox;
	@UiField
	Label enableTaxLabel;
	@UiField
	Label taxItemTransactionLabel;
	@UiField
	CheckBox trackCheckbox;
	@UiField
	Label trackLabel;
	// @UiField
	// Label createEstimatesLabelItem;
	// @UiField
	// RadioButton createEstimatesYesRadioButton;
	// @UiField
	// RadioButton createEstimatesNoRadioButton;
	// @UiField
	// Label usingStatementsLabelItem;
	// @UiField
	// RadioButton usingStatementsYesRadioButton;
	// @UiField
	// RadioButton usingStatementsNoRadioButton;
	@UiField
	VerticalPanel salesTaXPanel;
	@UiField
	VerticalPanel VatPanel;
	@UiField
	RadioButton vatNoRadioButton;
	@UiField
	RadioButton vatYesRadioButton;
	@UiField
	Label vatLabel;
	@UiField
	VerticalPanel checkBoxPanel;

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

	public void initData() {
		if (getCompanyPreferences().isChargeSalesTax()) {
			chargeTaxYesRadioButton.setValue(true);
		} else {
			chargeTaxNoRadioButton.setValue(true);
		}

		if (getCompanyPreferences().isRegisteredForVAT()) {
			vatYesRadioButton.setValue(true);
		} else {
			vatNoRadioButton.setValue(true);
		}

		if (getCompanyPreferences().isTrackTax()) {
			enableTaxCheckbox.setVisible(true);
		}
		if (getCompanyPreferences().isTaxPerDetailLine()) {
			oneperdetaillineRadioButton.setValue(true);
		} else {
			onepeTransactionRadioButton.setValue(true);
		}

		boolean isTaxTrackEnable = getCompanyPreferences().isTrackTax();
		if (isTaxTrackEnable) {
			trackCheckbox.setValue(true);
			checkBoxPanel.setVisible(isTaxTrackEnable);
		} else {
			checkBoxPanel.setVisible(isTaxTrackEnable);
		}
		// if (companyPreferences.isDoyouwantEstimates()) {
		// createEstimatesYesRadioButton.setValue(true);
		// createEstimatesNoRadioButton.setValue(false);
		// } else {
		// createEstimatesYesRadioButton.setValue(false);
		// createEstimatesNoRadioButton.setValue(true);
		// }
		// if (companyPreferences.isDoyouwantstatements()) {
		// usingStatementsYesRadioButton.setValue(true);
		// usingStatementsNoRadioButton.setValue(false);
		// } else {
		// usingStatementsYesRadioButton.setValue(false);
		// usingStatementsNoRadioButton.setValue(true);
		// }

	}

	public void createControls() {

		chargeTaxLabelItem.setText(constants.doyouchargesalestax());
		salesTaxDescriptionLabel.setText(constants.salesTaxDescrition());
		chargeTaxYesRadioButton.setText(constants.yes());
		chargeTaxNoRadioButton.setText(constants.no());
		salesTaxDescriptionLabel.setStyleName("organisation_comment");

		vatLabel.setText(constants.doyouchargeVat());
		VatdescritionLabel.setText(constants.vatDescrition());
		vatNoRadioButton.setText(constants.no());
		vatYesRadioButton.setText(constants.yes());
		VatdescritionLabel.setStyleName("organisation_comment");

		trackCheckbox.setText(constants.chargeOrTrackTax());
		trackLabel.setText(constants.descChrageTrackTax());
		trackLabel.setStyleName("organisation_comment");
		taxItemTransactionLabel.setText(constants.taxtItemTransaction());
		onepeTransactionRadioButton.setText(constants.onepertransaction());
		oneperTransactionLabel.setText(constants.oneperDescription());
		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineRadioButton.setText(constants.oneperdetailline());
		oneperdetaillineLabel.setText(constants.oneperDetailDescription());
		oneperTransactionLabel.setStyleName("organisation_comment");
		enableTaxCheckbox.setText(constants.enableTracking());
		enableTaxLabel.setText(constants.enableTrackingDescription());
		enableTaxLabel.setStyleName("organisation_comment");

		// createEstimatesLabelItem.setText(constants
		// .wanttoCreateEstimatesInAccounter());
		//
		// createEstimatesYesRadioButton.setText(constants.yes());
		// createEstimatesNoRadioButton.setText(constants.no());
		//
		// usingStatementsLabelItem.setText(constants.doyouWantToUseStatements());
		//
		// usingStatementsYesRadioButton.setText(constants.yes());
		// usingStatementsNoRadioButton.setText(constants.no());

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			salesTaXPanel.setVisible(false);
		}
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			VatPanel.setVisible(false);
		}
		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (trackCheckbox.getValue()) {
					checkBoxPanel.setVisible(true);
				} else {
					checkBoxPanel.setVisible(false);
				}

			}
		});

	}

	@Override
	public String getTitle() {

		return constants.customerAndvendorSettings();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setChargeSalesTax(
				chargeTaxYesRadioButton.getValue());

		getCompanyPreferences().setRegisteredForVAT(
				vatYesRadioButton.getValue());
		getCompanyPreferences().setTaxTrack(trackCheckbox.getValue());
		getCompanyPreferences().setTaxPerDetailLine(
				oneperdetaillineRadioButton.getValue());
		getCompanyPreferences().setTrackPaidTax(enableTaxCheckbox.getValue());

		// if (createEstimatesYesRadioButton.getValue()) {
		// companyPreferences.setDoyouwantEstimates(true);
		// } else {
		// companyPreferences.setDoyouwantEstimates(false);
		// }
		// if (usingStatementsYesRadioButton.getValue()) {
		// companyPreferences.setDoyouwantstatements(true);
		// } else {
		// companyPreferences.setDoyouwantstatements(false);
		// }

	}

	@Override
	public String getAnchor() {
		return constants.customerAndvendorSettings();
	}

}
