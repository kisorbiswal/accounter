package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesPersonListsAction extends Action {

	public SalesPersonListsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				// UIUtils.setCanvas(new SalesPersonListView(),
				// getViewConfiguration());
				// MainFinanceWindow.getViewManager().showView(new
				// SalesPersonListView(), null,
				// false, SalesPersonListsAction.this);
			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {

		return "salesPersonList";
	}

	@Override
	public String getHelpToken() {
		return "sales-person-list";
	}

}
