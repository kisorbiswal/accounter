package com.vimukti.accounter.admin.client;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AddNewAdminuserAction extends Action {

	public AddNewAdminuserAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "add-new-admin-user";
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

		AddNewAdminUserDialog addAdminUserDialog = new AddNewAdminUserDialog(
				"Add new User");
		addAdminUserDialog.show();
	}

}
