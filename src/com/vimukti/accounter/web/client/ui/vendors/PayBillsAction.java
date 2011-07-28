package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class PayBillsAction extends Action {

	ClientPayBill payBill;
	protected PayBillView view;

	public PayBillsAction(String icon) {
		super(Accounter.constants().payBills(), icon);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public PayBillsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load the PayBill...", t);

			}

			public void onCreated() {

				view = PayBillView.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, PayBillsAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

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
		return Accounter.getFinanceMenuImages().payBill();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/pay_bills.png";
	// }

	@Override
	public String getHistoryToken() {
		return "payBill";
	}

}
