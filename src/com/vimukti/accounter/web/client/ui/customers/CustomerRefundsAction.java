package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 */

public class CustomerRefundsAction extends Action {

	protected CustomerRefundListView view;

	public CustomerRefundsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public CustomerRefundsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new CustomerRefundListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, CustomerRefundsAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load Customer Refund ListView",
				// t);

			}
		});

	}

//	
//	@Override
//	public ParentCanvas getView() {
//		return this.view;
//	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customerRefundsList();
	}
//	@Override
//	public String getImageUrl() {
//		
//		return "/images/customer_refunds_list.png";
//	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "customerRefunds";
	}
}
