package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class EditProfileAction extends Action {
	private EditProfileDialog view;

	public EditProfileAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		runAsync(data);

	}

	private void runAsync(Object data) {
		view = new EditProfileDialog(Accounter.constants().editProfile(), null);
		view.show();
		view.center();
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
		return "editProfile";
	}

	@Override
	public String getHelpToken() {
		return "edit-profile";
	}

}
