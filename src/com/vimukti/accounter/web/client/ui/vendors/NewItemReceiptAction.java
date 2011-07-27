package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewItemReceiptAction extends Action {

	private ItemReceiptView view;

	public NewItemReceiptAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.getVendorsMessages()
				.supplier(), Accounter.getVendorsMessages().vendor());
	}

	public NewItemReceiptAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.getVendorsMessages()
				.supplier(), Accounter.getVendorsMessages().vendor());
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().itemReciept();
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load CashPurchase...", t);
			}

			public void onCreated() {

				view = new ItemReceiptView();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewItemReceiptAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

			}

		});

	}

	@Override
	public String getImageUrl() {
		return "/images/item_Recpit.png";
	}

	@Override
	public String getHistoryToken() {
		return "newItemReceipt";
	}

}
