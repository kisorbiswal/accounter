/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.setup.OrganizationTypeConstants;

/**
 * @author vimukti36
 * 
 */
public class OrganisationTypeOption extends AbstractPreferenceOption {

	private static TaxFormOptionUiBinder uiBinder = GWT
			.create(TaxFormOptionUiBinder.class);
	@UiField
	Label radioButtonHeaderLabel;
	@UiField
	HorizontalPanel headerLabelPanel;
	@UiField
	VerticalPanel radioButtonsMainPanel;
	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label organizeText;
	@UiField
	RadioButton propriterShip;
	@UiField
	Label uninCorporated;
	@UiField
	RadioButton partnership;
	@UiField
	Label morePartners;
	@UiField
	RadioButton lLC;
	@UiField
	Label lLCText;
	@UiField
	ListBox lLCCombo;
	@UiField
	RadioButton corporation;
	@UiField
	Label corporationText;
	@UiField
	RadioButton sCorporation;
	@UiField
	Label sCorporationText;
	@UiField
	RadioButton nonProfit;
	@UiField
	Label nonProfitText;
	@UiField
	RadioButton other;
	@UiField
	HTML organizationLink;

	interface TaxFormOptionUiBinder extends
			UiBinder<Widget, OrganisationTypeOption> {
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
	public OrganisationTypeOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void createControls() {
		radioButtonHeaderLabel.setText(constants.howIsYourCompanyOrganized());

		organizeText.setText(Accounter.messages()
				.howIsYourCompanyOrganizedDesc(Global.get().account()));
		propriterShip.setText(constants.soleProprietorship());
		partnership.setText(constants.partnershipOrLLP());
		lLC.setText(constants.LLC());
		corporation.setText(constants.corporation());
		sCorporation.setText(constants.sCorporation());
		nonProfit.setText(constants.nonProfit());
		other.setText(constants.otherNone());

		organizeText.setText(Accounter.messages()
				.howIsYourCompanyOrganizedDesc(Global.get().account()));
		uninCorporated.setText(constants.soleProprietorshipDesc());
		morePartners.setText(constants.partnershipOrLLPDesc());
		lLCText.setText(constants.LLCDesc());
		corporationText.setText(constants.sCorporationDesc());
		sCorporationText.setText(constants.corporationDesc());
		nonProfitText.setText(constants.nonProfitDesc());

		organizeText.addStyleName("organisation_comment");
		uninCorporated.addStyleName("organisation_comment");
		morePartners.addStyleName("organisation_comment");
		lLCText.addStyleName("organisation_comment");
		corporationText.addStyleName("organisation_comment");
		sCorporationText.addStyleName("organisation_comment");
		nonProfitText.addStyleName("organisation_comment");

		// HTML organizationLink;

		propriterShip.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		partnership.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});

		lLCCombo.addStyleName("organisation_combo");

		lLCCombo.addItem(constants.llcSingleMemberForm());
		lLCCombo.addItem(constants.llcMultiMemberForm());
		lLCCombo.setEnabled(false);
		lLC.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(true);
			}
		});
		corporation.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		sCorporation.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		nonProfit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
		other.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				lLCCombo.setEnabled(false);
			}
		});
	}

	private void initData() {
		switch (companyPreferences.getOrganizationType()) {
		case OrganizationTypeConstants.SOLE_PROPRIETORSHIP:
			propriterShip.setValue(true);
			break;
		case OrganizationTypeConstants.CORPORATION:
			partnership.setValue(true);
			break;
		case OrganizationTypeConstants.S_CORPORATION:
			sCorporation.setValue(true);
			break;
		case OrganizationTypeConstants.LLC:
			lLC.setValue(true);
			break;
		case OrganizationTypeConstants.PARTNERSHIP:
			partnership.setValue(true);
			break;
		case OrganizationTypeConstants.NON_PROFIT:
			nonProfit.setValue(true);
			break;
		case OrganizationTypeConstants.OTHER:
			other.setValue(true);
		}
	}

	public OrganisationTypeOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return constants.companyOrganization();
	}

	@Override
	public void onSave() {
		if (propriterShip.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.SOLE_PROPRIETORSHIP);
		} else if (partnership.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.CORPORATION);
		} else if (sCorporation.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.S_CORPORATION);
		} else if (lLC.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.LLC);
		} else if (partnership.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.PARTNERSHIP);
		} else if (nonProfit.getValue()) {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.NON_PROFIT);
		} else {
			companyPreferences
					.setOrganizationType(OrganizationTypeConstants.OTHER);
		}
	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
