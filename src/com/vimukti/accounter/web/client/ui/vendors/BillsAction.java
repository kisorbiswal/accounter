package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Kumar kasimala
 * @modified by Ravi Kiran.G
 */

public class BillsAction extends Action {

	protected BillListView view;
	public String viewType;

	public BillsAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				if (viewType == null)
					view = BillListView.getInstance();
				else
					view = new BillListView(viewType);

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, null, false,
						BillsAction.this);

			}

		});
	}

	public void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		run(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bills();
	}

	// @Override
	// public String getImageUrl() {
	// return "images/bills.png";
	// }

	@Override
	public String getHistoryToken() {
		return "billsAndExpenses";
	}

}
