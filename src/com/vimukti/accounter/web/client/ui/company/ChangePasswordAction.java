package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ChangePasswordAction extends Action {
	private ChangePasswordDialog view;

	public ChangePasswordAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		view = new ChangePasswordDialog(Accounter.constants().changePassword(),
				null);
		view.show();
		view.center();
	}

	@Override
	public String getHistoryToken() {
		return "changePassword";
	}

	@Override
	public String getHelpToken() {
		return "change-password";
	}

}
