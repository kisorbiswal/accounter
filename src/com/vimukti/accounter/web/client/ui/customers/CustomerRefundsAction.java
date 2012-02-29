package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class CustomerRefundsAction extends Action {

	protected CustomerRefundListView view;

	public CustomerRefundsAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerRefundListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerRefundsAction.this);

			}

		});
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customerRefundsList();
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/customer_refunds_list.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerRefunds";
	}

	@Override
	public String getHelpToken() {
		return "customer-refund";
	}

	@Override
	public String getText() {
		return messages.customerRefunds(Global.get().Customer());
	}
}
