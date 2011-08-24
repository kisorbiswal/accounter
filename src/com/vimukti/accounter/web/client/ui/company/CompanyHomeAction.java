package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DashBoardView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CompanyHomeAction extends Action {

	private DashBoardView view;

	public CompanyHomeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public CompanyHomeAction(String text, IsSerializable editableObject,
			AccounterAsyncCallback<Object> callbackObject) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		view = new DashBoardView();

		MainFinanceWindow.getViewManager().showView(view, null, false, this);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "dashBoard";
	}

	@Override
	public String getHelpToken() {
		return "company";
	}

}
