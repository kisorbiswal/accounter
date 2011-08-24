package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class UsersAction extends Action {
	private UsersView view;

	public UsersAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		try {
			view = new UsersView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, UsersAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getHistoryToken() {

		return "users";
	}

	@Override
	public String getHelpToken() {
		return "user";
	}

}
