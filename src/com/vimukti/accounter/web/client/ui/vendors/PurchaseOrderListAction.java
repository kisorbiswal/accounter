package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseOrderListAction extends Action {


	public PurchaseOrderListAction() {
		super();
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
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				PurchaseOrderListView view = new PurchaseOrderListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PurchaseOrderListAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
	}

	public ImageResource getBigImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Purchase-order.png";
	// }
	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getHistoryToken() {

		return "purchaseOrderList";
	}

	@Override
	public String getHelpToken() {
		return "purchase-order-list";
	}

	@Override
	public String getText() {
		return messages.purchaseOrderList();
	}
}
