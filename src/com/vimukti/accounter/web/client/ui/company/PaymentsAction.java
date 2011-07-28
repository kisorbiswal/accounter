package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author Raj Vimal
 */

public class PaymentsAction extends Action {

	public PaymentsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public PaymentsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					PaymentListView view = PaymentListView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, PaymentsAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Payment List ", t);
			}
		});
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().payments();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/payments.png";
	// }

	@Override
	public String getHistoryToken() {
		return "payments";
	}
}
