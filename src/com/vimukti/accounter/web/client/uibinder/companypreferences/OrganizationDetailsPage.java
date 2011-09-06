package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class OrganizationDetailsPage extends AbstractCompanyInfoPanel {

	private static OrganizationDetailsPageUiBinder uiBinder = GWT
			.create(OrganizationDetailsPageUiBinder.class);

	// private RadioButton soleProprietorshipRadio, partnershipRadio, llcRadio,
	// corporationRadio, sCorporationRadio, nonProfitRadio,
	// otherNoneRadio;
	// private SelectCombo llcFormCombo;
	// private String ORG_TYPE;
	//
	// // @UiField
	// VerticalPanel viewPanel;
	//
	// // @UiField
	// RadioButton soleProprietorshipRadio;
	//
	// // @UiField
	// RadioButton partnershipRadio;
	//
	// // @UiField
	// RadioButton llcRadio;
	//
	// // @UiField
	// RadioButton corporationRadio;
	//
	// // @UiField
	// RadioButton sCorporationRadio;
	//
	// // @UiField
	// RadioButton nonProfitRadio;
	//
	// // @UiField
	// RadioButton otherNoneRadio;
	//
	// // @UiField
	// ListBox llcFormCombo;
	//
	// // @UiField
	// Label soleProprietorshipRadioLabel;
	//
	// // @UiField
	// Label partnershipRadioLabel;
	//
	// // @UiField
	// Label llcRadioLabel;
	//
	// // @UiField
	// Label corporationRadioLabel;
	//
	// // @UiField
	// Label sCorporationRadioLabel;
	//
	// // @UiField
	// Label nonProfitRadioLabel;
	//
	// // @UiField
	// Label otherNoneRadioLabel;

	interface OrganizationDetailsPageUiBinder extends
			UiBinder<Widget, OrganizationDetailsPage> {
	}

	public OrganizationDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		// createControls();
	}

	// private void createControls() {
	// ORG_TYPE = constants.organisation();
	// soleProprietorshipRadio.setName(constants.soleProprietorship());
	// soleProprietorshipRadio.setHTML(ORG_TYPE);
	// partnershipRadio.setName(constants.partnershipOrLLP());
	// partnershipRadio.setHTML(ORG_TYPE);
	// llcRadio.setName(constants.LLC());
	// llcRadio.setHTML(ORG_TYPE);
	// corporationRadio.setName(constants.corporation());
	// corporationRadio.setHTML(ORG_TYPE);
	// sCorporationRadio.setName(constants.sCorporation());
	// sCorporationRadio.setHTML(ORG_TYPE);
	// nonProfitRadio.setName(constants.nonProfit());
	// nonProfitRadio.setHTML(ORG_TYPE);
	// otherNoneRadio.setName(constants.otherNone());
	// otherNoneRadio.setHTML(ORG_TYPE);
	// llcFormCombo.addItem(constants.llcSingleMemberForm());
	// llcFormCombo.addItem(constants.llcMultiMemberForm());
	// }

	@Override
	public void onLoad() {
		// switch (companyPreferences.getOrganizationType()) {
		// case OrganizationTypeConstants.SOLE_PROPRIETORSHIP:
		// soleProprietorshipRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.CORPORATION:
		// corporationRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.S_CORPORATION:
		// sCorporationRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.LLC:
		// llcRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.PARTNERSHIP:
		// partnershipRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.NON_PROFIT:
		// nonProfitRadio.setValue(true);
		// break;
		// case OrganizationTypeConstants.OTHER:
		// otherNoneRadio.setValue(true);
		// }
	}

	@Override
	public void onSave() {
		// if (soleProprietorshipRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
		// } else if (corporationRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.CORPORATION);
		// } else if (sCorporationRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.S_CORPORATION);
		// } else if (llcRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.LLC);
		// } else if (partnershipRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.PARTNERSHIP);
		// } else if (nonProfitRadio.getValue()) {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.NON_PROFIT);
		// } else {
		// companyPreferences
		// .setOrganizationType(OrganizationTypeConstants.OTHER);
		// }
	}

}
