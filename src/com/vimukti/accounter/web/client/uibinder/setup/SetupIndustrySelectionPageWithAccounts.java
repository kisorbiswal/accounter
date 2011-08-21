/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SetupIndustrySelectionPageWithAccounts extends AbstractSetupPage {
	private static SetupIndustrySelectionPageWithAccountsUiBinder uiBinder = GWT
			.create(SetupIndustrySelectionPageWithAccountsUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	VerticalPanel industryAccountsPanel;
	@UiField
	ListBox industryList;
	@UiField
	ListBox accountsList;
	@UiField
	HTML customizeAccounter, settingsLater, industry;
	@UiField
	Label selectIndustry;
	@UiField
	Label headerLabel;

	interface SetupIndustrySelectionPageWithAccountsUiBinder extends
			UiBinder<Widget, SetupIndustrySelectionPageWithAccounts> {
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
	public SetupIndustrySelectionPageWithAccounts() {
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
		headerLabel.setText(accounterConstants.selectYourIndustry());

		customizeAccounter.setHTML(accounterMessages.selectIndustryInfoHTML());
		settingsLater.setHTML(accounterMessages.industrySelectNote());
		selectIndustry.setText(accounterMessages.selectIndustry());
		industryList.setName(accounterConstants.industry());

		final String[] industries = new String[] {
				Accounter.constants().accountingorBookkeeping(),
				Accounter.constants().advertisingorPublicRelations(),
				Accounter.constants().agricultureRanchingFarming(),
				Accounter.constants().artWritingPhotography(),
				Accounter.constants().automotiveSalesAndRepair(),
				Accounter.constants().churhorReligiousOrganisation(),
				Accounter.constants().constructionGeneralContractor(),
				Accounter.constants().constructionTrades(),
				Accounter.constants().designArchitectureEngineering(),
				Accounter.constants().financialServices(),
				Accounter.constants().hairSallonBeautysaloon(),
				Accounter.constants().informationTechnology(),
				Accounter.constants().insuranceAgencyorBroker(),
				Accounter.constants().lawncareOrlandscaping(),
				Accounter.constants().legalServices(),
				Accounter.constants().lodging(),
				Accounter.constants().manufacturerRepresentativeOrAgent(),
				Accounter.constants().manufacturing(),
				Accounter.constants().medicalDentalorhealthservices(),
				Accounter.constants().nonProfit(),
				Accounter.constants().professionalConsulting(),
				Accounter.constants().propertyManagementorHomeAssociation(),
				Accounter.constants().realEstateBrokerageorDeveloper(),
				Accounter.constants().rental(),
				Accounter.constants().repairandMaintenance(),
				Accounter.constants().restaurantCatererorbar(),
				Accounter.constants().retailShoporonlinecommerce(),
				Accounter.constants().salesIndependentAgent(),
				Accounter.constants().transportationTruckingordelivery(),
				Accounter.constants().wholesaledistributionandsales(),
				Accounter.constants().generalProductbasedBusiness(),
				Accounter.constants().generalServicebasedBusiness() };
		for (int i = 0; i < industries.length; i++) {
			industryList.addItem(industries[i]);
		}

		industryList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = industryList.getSelectedIndex();
				if (selectedIndex < 0) {
					return;
				}
				String selectedIndusty = industries[selectedIndex];
			}
		});
		industryList.setVisibleItemCount(10);
	}

	@Override
	public boolean canShow() {
		return true;
	}

}
