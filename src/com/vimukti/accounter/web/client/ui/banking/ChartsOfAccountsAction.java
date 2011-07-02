package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ChartsOfAccountsAction extends Action {

	protected ChartOfAccountsView view;

	public ChartsOfAccountsAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	public ChartsOfAccountsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					view = ChartOfAccountsView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ChartsOfAccountsAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Chart of Accounts ", t);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().cahrtOfAccounts();
	}

	@Override
	public String getImageUrl() {
		
		return "/images/chart_of_accounts.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
