package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PaySalesTaxView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PaySalesTaxAction extends Action {

	public PaySalesTaxAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				// if (Accounter.getCompany().getAccountingType() == 1) {
				// MainFinanceWindow.getViewManager().showView(
				// new VATPaymentView(), data, false,
				// PaySalesTaxAction.this);
				// } else {
				// UIUtils.setCanvas(new PaySalesTaxView(),
				// getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(
						new PaySalesTaxView(), data, false,
						PaySalesTaxAction.this);
				// }

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().paySalesTax();
	}

	@Override
	public String getHistoryToken() {
		return "paySalesTax";
	}

	@Override
	public String getHelpToken() {
		return "pay_sales-tax";
	}

}
