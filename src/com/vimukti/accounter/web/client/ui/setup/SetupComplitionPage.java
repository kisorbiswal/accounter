package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;

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
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub
		
	}

}
