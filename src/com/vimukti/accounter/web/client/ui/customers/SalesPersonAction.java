package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class SalesPersonAction extends Action {

	protected SalesPersonListView view;

	public SalesPersonAction(String text) {
		super(text);
		this.catagory = Accounter.getCustomersMessages().salesPerson();
	}

	public SalesPersonAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCustomersMessages().salesPerson();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new SalesPersonListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, SalesPersonAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load the InvoiceList", t);

			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/customers.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "salesPersons";
	}
}
