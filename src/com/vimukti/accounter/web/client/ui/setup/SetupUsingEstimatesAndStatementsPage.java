package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;

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
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		SetupTrackBillsAndTimeAction action = new SetupTrackBillsAndTimeAction(
				"Bills and Time");
		action.run(null, false);
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
