package com.vimukti.accounter.web.client.ui.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class SetupIndustrySelectionPage extends AbstractSetupPage {
	private VerticalPanel industryVerticalPanel;
	private SelectCombo industryCombo;
	String[] industries;

	public SetupIndustrySelectionPage() {
		super();

	}

	@Override
	public String getHeader() {
		return this.accounterConstants.selectYourIndustry();
	}

	@Override
	public VerticalPanel getPageBody() {
		createControls();
		industryVerticalPanel.addStyleName("setuppage_body");
		return industryVerticalPanel;
	}

	private void createControls() {
		industryVerticalPanel = new VerticalPanel();
		HTML industryinfoHTML = new HTML(this.accounterMessages
				.selectIndustryInfoHTML());
		HTML industrySelectNote = new HTML(this.accounterMessages
				.industrySelectNote());
		DynamicForm industryDynamicForm = new DynamicForm();
		industryCombo = new SelectCombo(this.accounterConstants.industry());
		industries = new String[] {
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
		industryCombo.initCombo(getIndustryList());
		industryVerticalPanel.add(industryinfoHTML);
		industryVerticalPanel.add(industrySelectNote);
		industryDynamicForm.setFields(industryCombo);
		industryDynamicForm.setCellSpacing(10);
		industryVerticalPanel.add(industryDynamicForm);
	}

	private List<String> getIndustryList() {
		List<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < industries.length; i++) {
			arrayList.add(industries[i]);
		}
		return arrayList;
	}

	@Override
	public void onLoad() {

//		String industryType = preferences.getIndustryType();
//		if (industryType != null)
//			industryCombo.setValue(industryType);
	}

	@Override
	public void onSave() {

//		String selectedValue = industryCombo.getSelectedValue();
//		if (selectedValue != null)
//			preferences.setIndustryType(selectedValue);
	}

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

}
