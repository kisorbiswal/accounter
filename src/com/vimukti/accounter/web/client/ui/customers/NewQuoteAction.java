package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
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

public class NewQuoteAction extends Action {

	protected QuoteView view;
	private int type;
	private String title;

	public NewQuoteAction(String text, int type) {
		super(text);
		this.type = type;
		this.title = text;

		if (type == ClientEstimate.QUOTES) {
			title = Accounter.constants().quote();
		} else if (type == ClientEstimate.CHARGES) {
			title = Accounter.constants().charge();
		} else if (type == ClientEstimate.CREDITS) {
			title = Accounter.constants().credit();
		}

	}

	public NewQuoteAction(String text, ClientEstimate quote,
			AccounterAsyncCallback<Object> callback, int type) {
		super(text);
		this.catagory = Global.get().Customer();
		this.type = type;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				if (type == 0 && data != null) {
					type = ((ClientEstimate) data).getEstimateType();
					switch (type) {
					case ClientEstimate.QUOTES:
						title = Accounter.constants().quote();
						break;
					case ClientEstimate.CHARGES:
						title = Accounter.constants().charge();
						break;
					case ClientEstimate.CREDITS:
						title = Accounter.constants().credit();
						break;
					default:
						break;
					}
				}
				view = QuoteView.getInstance(type, title);

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewQuoteAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newQuote();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/new_quote.png";
	// }

	@Override
	public String getHistoryToken() {
		if (type == ClientEstimate.QUOTES) {
			return "newQuote";
		} else if (type == ClientEstimate.CHARGES) {
			return "newCharge";
		} else if (type == ClientEstimate.CREDITS) {
			return "newCredit";
		}
		return "";
	}

	@Override
	public String getHelpToken() {
		return "customer-quote";
	}
}
