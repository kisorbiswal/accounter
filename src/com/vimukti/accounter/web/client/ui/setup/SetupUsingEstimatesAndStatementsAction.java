package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SetupUsingEstimatesAndStatementsAction extends Action {
	private SetupUsingEstimatesAndStatementsPage statementsPage;

	public SetupUsingEstimatesAndStatementsAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		statementsPage = new SetupUsingEstimatesAndStatementsPage();
		try {
			MainFinanceWindow.getViewManager().showView(statementsPage, data,
					isDependent, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
