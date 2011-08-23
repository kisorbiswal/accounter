package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PurchaseOrderListAction extends Action {

	private PurchaseOrderListView view;

	public PurchaseOrderListAction(String text) {
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

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new PurchaseOrderListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PurchaseOrderListAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
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
	public String getHistoryToken() {

		return "purchaseOrderList";
	}
}
