package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

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
//	@Override
//	public ParentCanvas getView() {
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		view = new ChangePasswordDialog(Accounter.constants().changePassword(),
				null);
		try {
			view.show();
			view.center();
		} catch (Exception e) {
		}
	}

	@Override
	public String getHistoryToken() {
		return "changePassword";
	}

}
