package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseOrderAction extends Action {

	protected PurchaseOrderView view;

	public PurchaseOrderAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new PurchaseOrderView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PurchaseOrderAction.this);
				// / UIUtils.setCanvas(view, getViewConfiguration());

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer list...", t);
			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().purchaseOrder();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Purchase-order.png";
	// }

	@Override
	public String getHistoryToken() {

		return "purchaseOrder";
	}

	@Override
	public String getHelpToken() {
		return "purchase-order";
	}

}
