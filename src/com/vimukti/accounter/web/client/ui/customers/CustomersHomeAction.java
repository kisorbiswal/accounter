package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class CustomersHomeAction extends Action {

	private CustomerSectionHomeView view;

	public CustomersHomeAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	public CustomersHomeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new CustomerSectionHomeView();
		try {
			MainFinanceWindow.getViewManager().showView(view, null, false,
					CustomersHomeAction.this);
		} catch (Exception e) {
			Accounter.showError(FinanceApplication.getCustomersMessages()
					.failedToLoadCustomerHome());
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().customersHome();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/customers_home.png";
	}
}
