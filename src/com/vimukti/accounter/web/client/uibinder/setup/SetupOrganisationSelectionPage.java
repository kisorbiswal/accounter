/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

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
	Label OrganizeText;
	@UiField
	RadioButton PropriterShip;
	@UiField
	Label UninCorporated;
	@UiField
	RadioButton Partnership;
	@UiField
	Label MorePartners;
	@UiField
	RadioButton LLC;
	@UiField
	Label LLCText;
	@UiField
	RadioButton Corporation;
	@UiField
	Label CorporationText;
	@UiField
	RadioButton SCorporation;
	@UiField
	Label SCorporationText;
	@UiField
	RadioButton NonProfit;
	@UiField
	Label NonProfitText;
	@UiField
	RadioButton Other;
	@UiField
	HTML OrganizationLink;
	@UiField
	ListBox LLCCombo;

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
		// TODO Auto-generated method stub

	}

}
