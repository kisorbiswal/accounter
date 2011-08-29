package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TDSVendorsListAction extends Action {

	public TDSVendorsListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				MainFinanceWindow.getViewManager().showView(
						new TDSVendorsListView(), null, false,
						TDSVendorsListAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor list", t);
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
		return "TDSVendorsList";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return "TDSVendorList";
	}

}
