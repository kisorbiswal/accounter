package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.VendorCenterView;

public class VendorCenterAction extends Action {
	protected VendorCenterView view;

	public VendorCenterAction() {
		super();
	}

	@Override
	public String getText() {
		return messages.vendorCentre(Global.get().Vendor());
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new VendorCenterView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, VendorCenterAction.this);

			}

		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getHistoryToken() {
		return "vendorCenter";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
