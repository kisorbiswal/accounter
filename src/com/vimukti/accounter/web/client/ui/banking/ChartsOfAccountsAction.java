package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ChartsOfAccountsAction extends Action {

	protected ChartOfAccountsView view;

	public ChartsOfAccountsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

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

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().cahrtOfAccounts();
	}

	@Override
	public String getHistoryToken() {
		return "ChartsOfAccount";
	}
}
