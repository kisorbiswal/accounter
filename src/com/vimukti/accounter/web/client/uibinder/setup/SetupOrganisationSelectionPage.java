/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupOrganisationSelectionPage extends AbstractSetupPage {

	private static SetupOrganisationSelectionPageUiBinder uiBinder = GWT
			.create(SetupOrganisationSelectionPageUiBinder.class);
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
	@UiField
	ListBox lLCCombo;

	interface SetupOrganisationSelectionPageUiBinder extends
			UiBinder<Widget, SetupOrganisationSelectionPage> {
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
	public SetupOrganisationSelectionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		organizeText.setTitle(accounterConstants
				.howIsYourCompanyOrganizedDesc());
		propriterShip.setText(accounterConstants.soleProprietorship());
		partnership.setText(accounterConstants.partnershipOrLLP());
		lLC.setText(accounterConstants.LLC());
		corporation.setText(accounterConstants.corporation());
		sCorporation.setText(accounterConstants.sCorporation());
		nonProfit.setText(accounterConstants.nonProfit());
		other.setText(accounterConstants.otherNone());

		organizeText
				.setText(accounterConstants.howIsYourCompanyOrganizedDesc());
		uninCorporated.setText(accounterConstants.soleProprietorshipDesc());
		morePartners.setText(accounterConstants.partnershipOrLLPDesc());
		lLCText.setText(accounterConstants.LLCDesc());
		corporationText.setText(accounterConstants.sCorporationDesc());
		sCorporationText.setText(accounterConstants.corporationDesc());
		nonProfitText.setText(accounterConstants.nonProfitDesc());
		// HTML organizationLink;
	}

}
