package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.vat.ChalanDetailsListView;

public class ChalanListViewAction extends Action {

	protected ChalanDetailsListView view;

	public ChalanListViewAction() {
		super();
		this.catagory = messages.taxList();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new ChalanDetailsListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChalanListViewAction.this);
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
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "chalanDetailsList";
	}

	@Override
	public String getHelpToken() {
		return "chalanDetailsList";
	}

	@Override
	public String getText() {
		return messages.challanListView();
	}
}
