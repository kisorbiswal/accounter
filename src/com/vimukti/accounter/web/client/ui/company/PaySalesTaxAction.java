package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PaySalesTaxView;
import com.vimukti.accounter.web.client.ui.VATPaymentView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class PaySalesTaxAction extends Action {

	public PaySalesTaxAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public PaySalesTaxAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load PaySalesTax..", t);
				return;
			}

			public void onCreated() {

				try {
					if (FinanceApplication.getCompany().getAccountingType() == 1) {
						MainFinanceWindow.getViewManager().showView(
								new VATPaymentView(), data, false,
								PaySalesTaxAction.this);
					} else {
						// UIUtils.setCanvas(new PaySalesTaxView(),
						// getViewConfiguration());
						MainFinanceWindow.getViewManager().showView(
								new PaySalesTaxView(), data, false,
								PaySalesTaxAction.this);
					}
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().paySalesTax();
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/Pay_Sales_Tax.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "paySalesTax";
	}

}
