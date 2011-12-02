package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemListView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseItemsAction extends Action {

	public PurchaseItemsAction(String text) {
		super(text);
		this.catagory = Global.get().customer();
	}

	public PurchaseItemsAction(String text, String catageory) {
		super(text);
		this.catagory = catageory;
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ItemListView view = new ItemListView();
				view.isSalesType = false;
				view.setCatageoryType(getCatagory());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PurchaseItemsAction.this);

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

		return "purchaseItems";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
