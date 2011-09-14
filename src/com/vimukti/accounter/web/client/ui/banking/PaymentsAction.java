package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.PaymentListView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PaymentsAction extends Action {

	public PaymentsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				PaymentListView view = PaymentListView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PaymentsAction.this);

			}

//			@Override
//			public void onCreateFailed(Throwable t) {
//				
//			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
		return "payments";
	}
}
