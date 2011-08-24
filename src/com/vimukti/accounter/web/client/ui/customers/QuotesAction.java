package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class QuotesAction extends Action {

	protected QuoteListView view;

	public QuotesAction(String text) {
		super(text);
		this.catagory = Global.get().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new QuoteListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, QuotesAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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

	@Override
	public String getHelpToken() {
		return "customer-quote";
	}
}
