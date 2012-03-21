package com.vimukti.accounter.web.client.ui.win8;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AccounterMenuAction extends Action {

	@Override
	public String getText() {
		return "Accounter Menu";
	}

	@Override
	public void run() {
		AccounterMenuView view = new AccounterMenuView();
		MainFinanceWindow.getViewManager().showView(view, data, isDependent,
				AccounterMenuAction.this);
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.ACCOUNTER_MENU;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
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

}
