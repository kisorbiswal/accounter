package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.setup.OrganizationTypeConstants;

public class OrganizationDetailsPage extends AbstractCompanyInfoPanel {

	private static OrganizationDetailsPageUiBinder uiBinder = GWT
			.create(OrganizationDetailsPageUiBinder.class);
	@UiField
	Label soleProprietorshipRadioLabel;
	@UiField
	RadioButton partnershipRadio;
	@UiField
	Label partnershipRadioLabel;
	@UiField
	RadioButton llcRadio;
	@UiField
	Label llcRadioLabel;
	@UiField
	RadioButton corporationRadio;
	@UiField
	Label corporationRadioLabel;
	@UiField
	RadioButton sCorporationRadio;
	@UiField
	Label sCorporationRadioLabel;
	@UiField
	RadioButton nonProfitRadio;
	@UiField
	Label nonProfitRadioLabel;
	@UiField
	RadioButton otherNoneRadio;
	@UiField
	Label otherNoneRadioLabel;
	@UiField
	RadioButton soleProprietorshipRadio;
	@UiField
	ListBox llcFormCombo;

	private String ORG_TYPE;

	interface OrganizationDetailsPageUiBinder extends
			UiBinder<Widget, OrganizationDetailsPage> {
	}

	public OrganizationDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		ORG_TYPE = constants.organisation();
		soleProprietorshipRadio.setName(ORG_TYPE);
		soleProprietorshipRadio.setHTML(constants.soleProprietorship());
		soleProprietorshipRadioLabel
				.setText(constants.soleProprietorshipDesc());
		partnershipRadio.setName(ORG_TYPE);
		partnershipRadio.setHTML(constants.partnershipOrLLP());
		partnershipRadioLabel.setText(constants.partnershipOrLLPDesc());
		llcRadio.setName(ORG_TYPE);
		llcRadio.setHTML(constants.LLC());
		llcRadioLabel.setText(constants.LLCDesc());
		corporationRadio.setName(ORG_TYPE);
		corporationRadio.setHTML(constants.corporation());
		corporationRadioLabel.setText(constants.corporationDesc());
		sCorporationRadio.setName(ORG_TYPE);
		sCorporationRadio.setHTML(constants.sCorporation());
		sCorporationRadioLabel.setText(constants.sCorporationDesc());
		nonProfitRadio.setName(ORG_TYPE);
		nonProfitRadio.setHTML(constants.nonProfit());
		nonProfitRadioLabel.setText(constants.nonProfitDesc());
		otherNoneRadio.setName(ORG_TYPE);
		otherNoneRadio.setHTML(constants.otherNone());

		llcFormCombo.addItem(constants.llcSingleMemberForm());
		llcFormCombo.addItem(constants.llcMultiMemberForm());
		llcFormCombo.setEnabled(false);

		soleProprietorshipRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});
		partnershipRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});
		llcRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(true);
			}
		});
		corporationRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});
		sCorporationRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});
		nonProfitRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});
		otherNoneRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				llcFormCombo.setEnabled(false);
			}
		});

	}

	@Override
	public void onLoad() {
		switch (companyPreferences.getOrganizationType()) {
		case OrganizationTypeConstants.SOLE_PROPRIETORSHIP:
			soleProprietorshipRadio.setValue(true);
			break;
		case OrganizationTypeConstants.CORPORATION:
			corporationRadio.setValue(true);
			break;
		case OrganizationTypeConstants.S_CORPORATION:
			sCorporationRadio.setValue(true);
			break;
		case OrganizationTypeConstants.LLC:
			llcRadio.setValue(true);
			llcFormCombo.setEnabled(true);
			break;
		case OrganizationTypeConstants.PARTNERSHIP:
			partnershipRadio.setValue(true);
			break;
		case OrganizationTypeConstants.NON_PROFIT:
			nonProfitRadio.setValue(true);
			break;
		case OrganizationTypeConstants.OTHER:
			otherNoneRadio.setValue(true);
		}
		llcFormCombo
				.setEnabled(companyPreferences.getOrganizationType() == OrganizationTypeConstants.LLC);

	}

	@Override
	public void onSave() {
		if (soleProprietorshipRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
		} else if (corporationRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.CORPORATION);
		} else if (sCorporationRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.S_CORPORATION);
		} else if (llcRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.LLC);
			
		} else if (partnershipRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.PARTNERSHIP);
		} else if (nonProfitRadio.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.NON_PROFIT);
		} else {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.OTHER);
		}
	}

}
