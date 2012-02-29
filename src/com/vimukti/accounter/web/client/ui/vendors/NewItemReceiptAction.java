package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewItemReceiptAction extends Action {

	private ItemReceiptView view;

	public NewItemReceiptAction() {
		super();
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

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new ItemReceiptView();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewItemReceiptAction.this);

			}

		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/item_Recpit.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newItemReceipt";
	}

	@Override
	public String getHelpToken() {
		return "new-item-receipt";
	}

	@Override
	public String getText() {
		return messages.itemReceipt();
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

}
