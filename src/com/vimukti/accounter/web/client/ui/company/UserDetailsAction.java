package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class UserDetailsAction extends Action {

	private UserDetailsView view;

	public UserDetailsAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
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
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new UserDetailsView();
		try {
			MainFinanceWindow.getViewManager().showView(view, data, isDependent,
					this);
		} catch (Exception e) {
		}
	}

}
