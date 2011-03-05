package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.GeneralSettingsView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;

public class GeneralSettingsAction extends Action {
	public GeneralSettingsView view;

	public GeneralSettingsAction(String text) {
		super(text);
		this.catagory = "Getting Started";
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

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					// if (accountType == 0)
					view = new GeneralSettingsView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, GeneralSettingsAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
			}
		});
	}

}
