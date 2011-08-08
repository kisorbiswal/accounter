package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupComplitionPage extends AbstractSetupPage {
	HTML row1, row2, row3;
	VerticalPanel vpanel;

	public SetupComplitionPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.congratulations();
	}

	@Override
	public VerticalPanel getPageBody() {
		creatControls();
		return vpanel;
	}

	private void creatControls() {
		vpanel = new VerticalPanel();
		vpanel.add(new HTML(this.accounterConstants.completedEasySteps()));
		vpanel.add(new HTML(this.accounterConstants.youHaveCompanyFile()));
		vpanel.add(new HTML(this.accounterConstants.weWillHelpYou()));

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
