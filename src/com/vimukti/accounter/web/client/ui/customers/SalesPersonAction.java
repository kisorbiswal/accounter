package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SalesPersonAction extends Action {

	protected SalesPersonListView view;

	public SalesPersonAction(String text) {
		super(text);
		this.catagory = Accounter.constants().salesPerson();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new SalesPersonListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesPersonAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/customers.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesPersons";
	}

	@Override
	public String getHelpToken() {
		return "new_sales-person";
	}
}
