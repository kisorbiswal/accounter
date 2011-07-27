package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class PurchaseOrderListAction extends Action {

	private PurchaseOrderListView view;

	public PurchaseOrderListAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.getVendorsMessages()
				.supplier(), Accounter.getVendorsMessages().vendor());
	}

	public PurchaseOrderListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.getVendorsMessages()
				.supplier(), Accounter.getVendorsMessages().vendor());
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {

					view = new PurchaseOrderListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, PurchaseOrderListAction.this);
					// / UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}
			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer list...", t);
			}
		});

	}

	public ImageResource getBigImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	@Override
	public String getImageUrl() {
		return "/images/Purchase-order.png";
	}

	@Override
	public String getHistoryToken() {

		return "purchaseOrderList";
	}
}
