package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class SetupOrganisationSelectionPage extends AbstractSetupPage {

	private static final String ORG_TYPE = "OrganizationType";
	private RadioButton soleProprietorshipRadio, partnershipRadio, llcRadio,
			corporationRadio, sCorporationRadio, nonProfitRadio,
			otherNoneRadio;
	private CustomLabel soleProprietorshipLabel, partnershipLabel, llcLabel,
			corporationLabel, sCorporationLabel, nonProfitLabel;
	private SelectCombo llcFormCombo;
	private DynamicForm comboForm;

	@Override
	public String getHeader() {
		return accounterConstants.howIsYourCompanyOrganized();
	}

	@Override
	public VerticalPanel getPageBody() {
		VerticalPanel viewContainer = new VerticalPanel();

		soleProprietorshipRadio = new RadioButton(ORG_TYPE, accounterConstants
				.soleProprietorship());
		partnershipRadio = new RadioButton(ORG_TYPE, accounterConstants
				.partnershipOrLLP());
		llcRadio = new RadioButton(ORG_TYPE, accounterConstants.LLC());
		corporationRadio = new RadioButton(ORG_TYPE, accounterConstants
				.corporation());
		sCorporationRadio = new RadioButton(ORG_TYPE, accounterConstants
				.sCorporation());
		nonProfitRadio = new RadioButton(ORG_TYPE, accounterConstants
				.nonProfit());
		otherNoneRadio = new RadioButton(ORG_TYPE, accounterConstants
				.otherNone());

		CustomLabel headerDesc = new CustomLabel(Accounter.messages()
				.howIsYourCompanyOrganizedDesc(Global.get().Accounts()));
		soleProprietorshipLabel = new CustomLabel(accounterConstants
				.soleProprietorshipDesc());
		partnershipLabel = new CustomLabel(accounterConstants
				.partnershipOrLLPDesc());
		llcLabel = new CustomLabel(accounterConstants.LLCDesc());
		sCorporationLabel = new CustomLabel(accounterConstants
				.sCorporationDesc());
		corporationLabel = new CustomLabel(accounterConstants.corporationDesc());
		nonProfitLabel = new CustomLabel(accounterConstants.nonProfitDesc());

		soleProprietorshipLabel.addStyleName("organisation_comment");
		partnershipLabel.addStyleName("organisation_comment");
		llcLabel.addStyleName("organisation_comment");
		sCorporationLabel.addStyleName("organisation_comment");
		corporationLabel.addStyleName("organisation_comment");
		nonProfitLabel.addStyleName("organisation_comment");

		comboForm = new DynamicForm();

		viewContainer.add(headerDesc);
		headerDesc.addStyleName("org-header");

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

		llcFormCombo.addItem(accounterConstants.llcSingleMemberForm());
		llcFormCombo.addItem(accounterConstants.llcMultiMemberForm());
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
		viewContainer.addStyleName("setuppage_body");
		return viewContainer;
	}

	@Override
	public void onLoad() {

		switch (preferences.getOrganizationType()) {
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
			preferences
					.setOrganizationType(OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
		} else if (corporationRadio.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.CORPORATION);
		} else if (sCorporationRadio.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.S_CORPORATION);
		} else if (llcRadio.getValue()) {
			preferences.setOrganizationType(OrganizationTypeConstants.LLC);
		} else if (partnershipRadio.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.PARTNERSHIP);
		} else if (nonProfitRadio.getValue()) {
			preferences
					.setOrganizationType(OrganizationTypeConstants.NON_PROFIT);
		} else {
			preferences.setOrganizationType(OrganizationTypeConstants.OTHER);
		}

	}

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
