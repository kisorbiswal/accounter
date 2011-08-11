package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupSelectAccountsPage extends AbstractSetupPage {

	@Override
	public String getHeader() {
		return this.accounterConstants.reviewIncomeAndExpensesAccounts();
	}

	@Override
	public VerticalPanel getPageBody() {
		// TODO Auto-generated method stub
		// .addStyleName("setuppage_body");
		return new VerticalPanel();
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
