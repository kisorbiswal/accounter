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

	public CompanyHomeAction() {
		super();
		this.catagory = Accounter.messages().company();
	}

	public CompanyHomeAction(IsSerializable editableObject,
			AccounterAsyncCallback<Object> callbackObject) {
		super();
		this.catagory = Accounter.messages().company();
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

	@Override
	public String getText() {
		return messages.home();
	}

}
