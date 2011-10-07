package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CustomersHomeAction extends Action {

	private CustomerSectionHomeView view;

	public CustomersHomeAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		view = new CustomerSectionHomeView();
		try {
			MainFinanceWindow.getViewManager().showView(view, null, false,
					CustomersHomeAction.this);
		} catch (Exception e) {
			Accounter.showError(Accounter.messages().failedToLoadCustomerHome(
					Global.get().Customer()));
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customersHome();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/customers_home.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerHome";
	}

	@Override
	public String getHelpToken() {
		return "customers";
	}
}
