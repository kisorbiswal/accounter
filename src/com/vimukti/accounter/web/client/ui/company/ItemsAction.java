package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemListView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class ItemsAction extends Action {

	// public ItemsAction() {
	// super();
	// this.catagory = Global.get().customer();
	// }

	public ItemsAction(String catageory) {
		super();
		this.catagory = catageory;
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ItemListView view = new ItemListView();
				view.setCatageoryType(getCatagory());
				// if (getCatagory().equals(Global.get().vendor())) {
				// ItemListView.isPurchaseType = true;
				// ItemListView.isSalesType = false;
				// } else if (getCatagory().equals(Global.get().Customer())) {
				// ItemListView.isPurchaseType = false;
				// ItemListView.isSalesType = true;
				// } else if (getCatagory()
				// .equals(
				// messages.bothCustomerAndVendor(
				// Global.get().Customer(),
				// Global.get().Vendor()))) {
				// ItemListView.isPurchaseType = true;
				// ItemListView.isSalesType = true;
				// }
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ItemsAction.this);

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
		return Accounter.getFinanceMenuImages().items();
	}

	@Override
	public String getHistoryToken() {
		ItemListView view = new ItemListView();
		// view.setCatageoryType(getCatagory());
		if (catagory.equalsIgnoreCase(Global.get().vendor()))
			return "vendorItems";
		if (catagory.equals(Global.get().customer()))
			return "customerItems";
		if (catagory.equals(messages.bothCustomerAndVendor(Global.get()
				.Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

	@Override
	public String getHelpToken() {
		ItemListView view = new ItemListView();
		if (catagory.equals(Global.get().vendor()))
			return "vendorItems";
		if (catagory.equals(Global.get().customer()))
			return "customerItems";
		if (catagory.equals(messages.bothCustomerAndVendor(Global.get()
				.Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

	@Override
	public String getText() {
		return messages.items();
	}

}
