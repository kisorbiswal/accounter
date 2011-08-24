package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class Prepare1099MISCAction extends Action {

	Prepare1099MISCView view;

	public Prepare1099MISCAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new Prepare1099MISCView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, Prepare1099MISCAction.this);
			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});
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

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
