package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewItemReceiptAction extends Action {

	private ItemReceiptView view;

	public NewItemReceiptAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
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

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ItemReceiptView();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewItemReceiptAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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

}
