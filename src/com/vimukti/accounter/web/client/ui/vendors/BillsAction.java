package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
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
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				if (viewType == null)
					view = BillListView.getInstance();
				else
					view = new BillListView(viewType);
				MainFinanceWindow.getViewManager().showView(view, null, false,
						BillsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
