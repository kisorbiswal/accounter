package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.ChartOfAccountsView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class ChartOfAccountsAction extends Action {

	protected ChartOfAccountsView view;
	public int accountType;

	public ChartOfAccountsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public ChartOfAccountsAction(String text, int accountType) {
		super(text);
		this.accountType = accountType;
		this.catagory = Accounter.constants().company();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				if (accountType == 0)
					view = ChartOfAccountsView.getInstance();
				else
					view = new ChartOfAccountsView(accountType);

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChartOfAccountsAction.this);

			}
		});
	}

	@Override
	public void run() {
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
		if (accountType == ClientAccount.TYPE_BANK) {
			return "bankAccounts";
		} else {
			return "accountsList";
		}
	}

	@Override
	public String getHelpToken() {
		return "accountcharts";
	}
}
