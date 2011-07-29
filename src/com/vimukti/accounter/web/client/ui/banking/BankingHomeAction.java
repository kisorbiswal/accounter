package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class BankingHomeAction extends Action {

	private BankingSectionHomeView view;

	public BankingHomeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new BankingSectionHomeView();

		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter
					.showError(Accounter.constants().failedToLoadBankingHome());
		}
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bankingHome();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/banking_home.png";
	// }

	@Override
	public String getHistoryToken() {
		return "bankingHome";
	}
}
