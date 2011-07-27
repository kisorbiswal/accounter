package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DashBoardView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CompanyHomeAction extends Action {

	private DashBoardView view;

	public CompanyHomeAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public CompanyHomeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public CompanyHomeAction(String text, String iconString,
			IsSerializable editableObject, AsyncCallback<Object> callbackObject) {
		super(text, iconString, editableObject, callbackObject);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new DashBoardView();

		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getCompanyMessages()
					.failedToLoadCompanyHome());
		}
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

}
