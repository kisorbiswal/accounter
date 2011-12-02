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

	public ItemsAction(String text) {
		super(text);
		this.catagory = Global.get().customer();
	}

	public ItemsAction(String text, String catageory) {
		super(text);
		this.catagory = catageory;
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ItemListView view = new ItemListView();
				view.setCatageoryType(getCatagory());
				if (getCatagory().equals(Global.get().vendor())) {
					ItemListView.isPurchaseType = true;
					ItemListView.isSalesType = false;
				} else if (getCatagory().equals(Global.get().Customer())) {
					ItemListView.isPurchaseType = false;
					ItemListView.isSalesType = true;
				} else if (getCatagory()
						.equals(
								Accounter.messages().bothCustomerAndVendor(
										Global.get().Customer(),
										Global.get().Vendor()))) {
					ItemListView.isPurchaseType = true;
					ItemListView.isSalesType = true;
				}
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ItemsAction.this);

			}

		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().items();
	}

	@Override
	public String getHistoryToken() {
		ItemListView view = new ItemListView();
		view.setCatageoryType(getCatagory());
		if (getCatagory().equals(Global.get().vendor()))
			return "vendorItems";
		if (getCatagory().equals(Global.get().Customer()))
			return "customerItems";
		if (getCatagory().equals(
				Accounter.messages().bothCustomerAndVendor(
						Global.get().Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

	@Override
	public String getHelpToken() {
		ItemListView view = new ItemListView();
		view.setCatageoryType(getCatagory());
		if (getCatagory().equals(Global.get().vendor()))
			return "vendorItems";
		if (getCatagory().equals(Global.get().Customer()))
			return "customerItems";
		if (getCatagory().equals(
				Accounter.messages().bothCustomerAndVendor(
						Global.get().Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

}
