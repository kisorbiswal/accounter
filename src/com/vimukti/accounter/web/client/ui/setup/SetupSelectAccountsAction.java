package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SetupSelectAccountsAction extends Action {
	private SetupSelectAccountsPage view;

	public SetupSelectAccountsAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new SetupSelectAccountsPage();
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
