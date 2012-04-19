package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ChartsOfAccountsAction extends Action {


	public ChartsOfAccountsAction() {
		super();
		this.catagory = messages.banking();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ChartOfAccountsView view = new ChartOfAccountsView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChartsOfAccountsAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//		});
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
		return "chartOfAccount";
	}

	@Override
	public String getHelpToken() {
		// This class not using
		return "accountcharts";
	}

	@Override
	public String getText() {
		return messages.chartOfAccounts();
	}
}
