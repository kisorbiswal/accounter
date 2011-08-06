package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SetupSkipDialogAction extends Action {
	private SetupSkipDialog dialog;

	public SetupSkipDialogAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		dialog = new SetupSkipDialog();
		try {
			// MainFinanceWindow.getViewManager().showView(view, data,
			// isDependent, this);
			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		// TODO Auto-generated method stub
		return null;
	}

}
