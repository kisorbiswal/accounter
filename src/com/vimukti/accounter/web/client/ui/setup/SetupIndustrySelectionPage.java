package com.vimukti.accounter.web.client.ui.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
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
		arrayList.add("Accounting or bookkeeping");
		arrayList.add("Advertising or public relations");
		arrayList.add("Agriculture , Ranching or Farming");
		arrayList.add("Art, Writing or Photography");
		arrayList.add("Automotive Sales or Repair");
		arrayList.add("Church or Religious Organisation");
		arrayList.add("Construction General Contractor");
		arrayList.add("Construction Trades (Plumber, Electrician, HVAC , etc)");
		arrayList.add("Design, Architecture Or Engineering");
		arrayList
				.add("Financial Services Other than Accounting Or bookkeeping");
		arrayList.add("Hair saloon, Beauty Saloon or barber shop");
		arrayList.add("Information Technology (Computers, software)");
		arrayList.add("Insurance Agency or broker");
		arrayList.add("Lawn Care or Landscaping");
		arrayList.add("Legal Services");
		arrayList.add("Lodging (Hotel, Motel)");
		arrayList.add("Manufacturer Representative Or Agent");
		arrayList.add("Manufacturing");
		arrayList.add("Medical, Dental or health services");
		arrayList.add("Non-profit");
		arrayList.add("Professional Consulting");
		arrayList.add("Property Management or Home Association");
		arrayList.add("Real Estate Brokerage or Developer");
		arrayList.add("Rental");
		arrayList.add("Repair and Maintenance");
		arrayList.add("Restaurant, Caterer or bar");
		arrayList.add("Retail Shop or online commerce");
		arrayList.add("Sales : Independent Agent");
		arrayList.add("Transportation, Trucking or delivery");
		arrayList.add("Wholesale distribution and sales");
		arrayList.add("General Product-based Business");
		arrayList.add("General Service-based Business");
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
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub

	}

}
