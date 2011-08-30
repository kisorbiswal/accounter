package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.setup.OrganizationTypeConstants;

public class OrganisationPanel extends AbstractCompanyInfoPanel {

	private RadioButton soleProprietorshipRadio, partnershipRadio, llcRadio,
			corporationRadio, sCorporationRadio, nonProfitRadio,
			otherNoneRadio;
	private SelectCombo llcFormCombo;
	private String ORG_TYPE;

	public OrganisationPanel() {
		super();
		createControls();
	}

	private void createControls() {
		VerticalPanel viewContainer = new VerticalPanel();
		ORG_TYPE = constants.organisation();
		soleProprietorshipRadio = new RadioButton(ORG_TYPE, constants
				.soleProprietorship());
		partnershipRadio = new RadioButton(ORG_TYPE, constants
				.partnershipOrLLP());
		llcRadio = new RadioButton(ORG_TYPE, constants.LLC());
		corporationRadio = new RadioButton(ORG_TYPE, constants.corporation());
		sCorporationRadio = new RadioButton(ORG_TYPE, constants.sCorporation());
		nonProfitRadio = new RadioButton(ORG_TYPE, constants.nonProfit());
		otherNoneRadio = new RadioButton(ORG_TYPE, constants.otherNone());

		CustomLabel soleProprietorshipLabel = new CustomLabel(constants
				.soleProprietorshipDesc());
		CustomLabel partnershipLabel = new CustomLabel(constants
				.partnershipOrLLPDesc());
		CustomLabel llcLabel = new CustomLabel(constants.LLCDesc());
		CustomLabel sCorporationLabel = new CustomLabel(constants
				.sCorporationDesc());
		CustomLabel corporationLabel = new CustomLabel(constants
				.corporationDesc());
		CustomLabel nonProfitLabel = new CustomLabel(constants.nonProfitDesc());

		soleProprietorshipLabel.addStyleName("organisation_comment");
		partnershipLabel.addStyleName("organisation_comment");
		llcLabel.addStyleName("organisation_comment");
		sCorporationLabel.addStyleName("organisation_comment");
		corporationLabel.addStyleName("organisation_comment");
		nonProfitLabel.addStyleName("organisation_comment");

		DynamicForm comboForm = new DynamicForm();

		viewContainer.add(soleProprietorshipRadio);
		viewContainer.add(soleProprietorshipLabel);
		soleProprietorshipRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.add(partnershipRadio);
		viewContainer.add(partnershipLabel);
		partnershipRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.add(llcRadio);
		viewContainer.add(llcLabel);

		llcFormCombo = new SelectCombo("");
		llcFormCombo.addStyleName("organisation_combo");

		llcFormCombo.addItem(constants.llcSingleMemberForm());
		llcFormCombo.addItem(constants.llcMultiMemberForm());
		llcFormCombo.setDisabled(true);
		llcRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(false);
			}
		});
		comboForm.setFields(llcFormCombo);
		viewContainer.add(comboForm);

		viewContainer.add(corporationRadio);
		viewContainer.add(corporationLabel);
		corporationRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.add(sCorporationRadio);
		viewContainer.add(sCorporationLabel);
		sCorporationRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.add(nonProfitRadio);
		viewContainer.add(nonProfitLabel);
		nonProfitRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.add(otherNoneRadio);
		otherNoneRadio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				llcFormCombo.setDisabled(true);
			}
		});
		viewContainer.setSize("100%", "100%");
		add(viewContainer);
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
