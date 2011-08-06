package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupUsingEstimatesAndStatementsPage extends AbstractSetupPage {

	@Override
	public String getHeader() {
		return this.accounterConstants.wanttoCreateEstimatesInAccounter();
	}

	@Override
	public VerticalPanel getPageBody() {
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
		return false;
	}

}
