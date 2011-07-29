package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class QuotesAction extends Action {

	protected QuoteListView view;

	public QuotesAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				try {
					view = new QuoteListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, QuotesAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());
				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Quote list..", t);

			}
		});

	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().quotes();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/quotes.png";
	// }

	@Override
	public String getHistoryToken() {

		return "quotes";
	}
}
