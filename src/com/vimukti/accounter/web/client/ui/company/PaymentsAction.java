package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class PaymentsAction extends Action {

	public static final int BANKING = 2;
	public static final int COMPANY = 1;

	public PaymentsAction(String text, int category) {
		super(text);
		if (category == COMPANY)
			catagory = Accounter.constants().company();
		else
			catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				PaymentListView view = PaymentListView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PaymentsAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().payments();
	}

	@Override
	public String getHistoryToken() {
		return "payments";
	}

	@Override
	public String getHelpToken() {
		return null;
	}
}
