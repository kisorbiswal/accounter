package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.ChartOfAccountsView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class ChartOfAccountsAction extends Action {

	protected ChartOfAccountsView view;
	public int accountType;

	public ChartOfAccountsAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public ChartOfAccountsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public ChartOfAccountsAction(String text, String iconString, int accountType) {
		super(text, iconString);
		this.accountType = accountType;
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					if (accountType == 0)
						view = ChartOfAccountsView.getInstance();
					else
						view = new ChartOfAccountsView(accountType);

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ChartOfAccountsAction.this);

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
	public ParentCanvas<?> getView() {
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
		if (accountType == ClientAccount.TYPE_BANK) {
			return "bankAccounts";
		} else {
			return "accountsList";
		}
	}
}
