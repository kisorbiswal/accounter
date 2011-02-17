package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class PaymentsAction extends Action {

	public PaymentsAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().banking();
	}

	public PaymentsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().banking();
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

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().payments();
	}
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/payments.png";
	}
}
