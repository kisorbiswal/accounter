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

	@Override
	public String getHeader() {
		return this.accounterConstants.selectYourIndustry();
	}

	@Override
	public VerticalPanel getPageBody() {
		createControls();
		return industryVerticalPanel;
	}

	private void createControls() {
		industryVerticalPanel = new VerticalPanel();
		HTML industryinfoHTML = new HTML(this.accounterMessages
				.selectIndustryInfoHTML());
		DynamicForm industryDynamicForm = new DynamicForm();
		SelectCombo industryCombo = new SelectCombo(this.accounterConstants
				.industry());
		industryCombo.initCombo(getIndustryList());
		industryVerticalPanel.add(industryinfoHTML);
		industryDynamicForm.setFields(industryCombo);
		industryVerticalPanel.add(industryDynamicForm);
	}

	private List<String> getIndustryList() {
		List<String> arrayList = new ArrayList<String>();
		arrayList.add(Accounter.constants().accountingorBookkeeping());
		arrayList.add(Accounter.constants().advertisingorPublicRelations());
		arrayList.add(Accounter.constants().agricultureRanchingFarming());
		arrayList.add(Accounter.constants().artWritingPhotography());
		arrayList.add(Accounter.constants().automotiveSalesAndRepair());
		arrayList.add(Accounter.constants().churhorReligiousOrganisation());
		arrayList.add(Accounter.constants().constructionGeneralContractor());
		arrayList.add(Accounter.constants().constructionTrades());
		arrayList.add(Accounter.constants().designArchitectureEngineering());
		arrayList.add(Accounter.constants().financialServices());
		arrayList.add(Accounter.constants().hairSallonBeautysaloon());
		arrayList.add(Accounter.constants().informationTechnology());
		arrayList.add(Accounter.constants().insuranceAgencyorBroker());
		arrayList.add(Accounter.constants().lawncareOrlandscaping());
		arrayList.add(Accounter.constants().legalServices());
		arrayList.add(Accounter.constants().lodging());
		arrayList
				.add(Accounter.constants().manufacturerRepresentativeOrAgent());
		arrayList.add(Accounter.constants().manufacturing());
		arrayList.add(Accounter.constants().medicalDentalorhealthservices());
		arrayList.add(Accounter.constants().nonProfit());
		arrayList.add(Accounter.constants().professionalConsulting());
		arrayList.add(Accounter.constants()
				.propertyManagementorHomeAssociation());
		arrayList.add(Accounter.constants().realEstateBrokerageorDeveloper());
		arrayList.add(Accounter.constants().rental());
		arrayList.add(Accounter.constants().repairandMaintenance());
		arrayList.add(Accounter.constants().restaurantCatererorbar());
		arrayList.add(Accounter.constants().retailShoporonlinecommerce());
		arrayList.add(Accounter.constants().salesIndependentAgent());
		arrayList.add(Accounter.constants().transportationTruckingordelivery());
		arrayList.add(Accounter.constants().wholesaledistributionandsales());
		arrayList.add(Accounter.constants().generalProductbasedBusiness());
		arrayList.add(Accounter.constants().generalServicebasedBusiness());
		return arrayList;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doShow() {
		return true;
	}

}
