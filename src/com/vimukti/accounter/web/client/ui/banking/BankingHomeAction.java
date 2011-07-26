package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class BankingHomeAction extends Action {

	private BankingSectionHomeView view;

	public BankingHomeAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getBankingsMessages().banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new BankingSectionHomeView();

		try {
			MainFinanceWindow.getViewManager()
					.showView(view, null, false, this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getBankingsMessages()
					.failedToLoadBankingHome());
		}
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bankingHome();
	}

	@Override
	public String getImageUrl() {		
		return "/images/banking_home.png";
	}

	@Override
	public String getHistoryToken() {
		return "bankingHome";
	}
}
