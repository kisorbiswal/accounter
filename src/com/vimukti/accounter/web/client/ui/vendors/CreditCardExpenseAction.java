package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CreditCardExpenseAction extends Action {

	CreditCardExpenseView view;

	public CreditCardExpenseAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public CreditCardExpenseAction(String text, ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new CreditCardExpenseView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CreditCardExpenseAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/credit_card_charge.png";
	// }

	@Override
	public String getHistoryToken() {
		return "creditCardExpense";
	}
}
