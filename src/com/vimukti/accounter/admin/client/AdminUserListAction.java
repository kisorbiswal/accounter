package com.vimukti.accounter.admin.client;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AdminUserListAction extends Action {
	AdminUsersListView usersListView;

	public AdminUserListAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "admin-user-list";
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
		usersListView = new AdminUsersListView();
		AdminHomePage.getViewManager().showView(usersListView,
				AdminUserListAction.this, false);

	}
}
