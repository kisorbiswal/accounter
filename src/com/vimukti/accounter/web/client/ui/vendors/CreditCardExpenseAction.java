package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CreditCardExpenseAction extends Action {

	CreditCardExpenseView view;

	public CreditCardExpenseAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public CreditCardExpenseAction(String text, String icon,
			ClientVendor vendor, AsyncCallback<Object> callback) {
		super(text, icon, vendor, callback);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new CreditCardExpenseView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, CreditCardExpenseAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer View..", t);
			}
		});

	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {

		return this.view;
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getImageUrl() {
		return "/images/credit_card_charge.png";
	}

	@Override
	public String getHistoryToken() {
		return "creditCardExpense";
	}
}
