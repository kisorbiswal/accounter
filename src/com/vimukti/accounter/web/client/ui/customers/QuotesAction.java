package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
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

	private int type;

	public QuotesAction(int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				QuoteListView view = new QuoteListView(type);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, QuotesAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
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
		if (type == ClientEstimate.CHARGES) {
			return "charges";
		} else if (type == ClientEstimate.CREDITS) {
			return "credits";
		} else if (type == ClientEstimate.SALES_ORDER) {
			return "salesOrderList";
		}
		return "quotes";
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHelpToken() {
		return "customer-quote";
	}

	@Override
	public String getText() {
		String text = "";
		if (type == ClientEstimate.CHARGES) {
			text = messages.Charges();
		} else if (type == ClientEstimate.CREDITS) {
			text = messages.credits();
		} else if (type == ClientEstimate.QUOTES) {
			text = messages.quotes();
		} else if (type == ClientEstimate.SALES_ORDER) {
			text = messages.salesOrders();
		}
		return text;
	}
}
