package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 */

public class NewQuoteAction extends Action {

	protected QuoteView view;

	public NewQuoteAction(String text, String icon) {
		super(text, icon);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	public NewQuoteAction(String text, String iconString, ClientEstimate quote,
			AsyncCallback<Object> callback) {
		super(text, iconString, quote, callback);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = QuoteView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewQuoteAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Quote View", t);
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().newQuote();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/new_quote.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "newQuote";
	}
}
