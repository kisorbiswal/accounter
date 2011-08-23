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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupIndustrySelectionPage extends AbstractSetupPage {

	private static SetupIndustrySelectionPageUiBinder uiBinder = GWT
			.create(SetupIndustrySelectionPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	ListBox industryList;
	@UiField
	HTML customizeAccounter, settingsLater, industry;
	@UiField
	Label selectIndustry;
	@UiField
	Label headerLabel;

	interface SetupIndustrySelectionPageUiBinder extends
			UiBinder<Widget, SetupIndustrySelectionPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 * 
	 * @param setupWizard
	 */
	public SetupIndustrySelectionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.selectYourIndustry());

		customizeAccounter.setHTML(accounterMessages.selectIndustryInfoHTML());
		settingsLater.setHTML(accounterMessages.industrySelectNote());
		selectIndustry.setText(accounterMessages.selectIndustry());
		industryList.setName(accounterConstants.industry());

		String[] industries = new String[] {
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
		industryList.setVisibleItemCount(15);

	}

	public void onLoad() {

		int industryType = preferences.getIndustryType();
		if (industryType < 0) {
			return;
		}
		industryType--;
		industryList.setSelectedIndex(industryType);
	}

	@Override
	public void onSave() {

		int selectedValue = industryList.getSelectedIndex();
		if (selectedValue < 0) {
			return;
		}
		selectedValue++;
		if (selectedValue != preferences.getIndustryType()) {
			preferences.setIndustryType(selectedValue);
		}
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		if (industryList.getSelectedIndex() == -1) {
			Accounter.showError(accounterConstants.selectYourIndustry());
			return false;
		} else {
			return true;
		}
	}
}
