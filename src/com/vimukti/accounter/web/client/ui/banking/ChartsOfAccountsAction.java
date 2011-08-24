package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ChartsOfAccountsAction extends Action {

	protected ChartOfAccountsView view;

	public ChartsOfAccountsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = ChartOfAccountsView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChartsOfAccountsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
		return "chartOfAccount";
	}

	@Override
	public String getHelpToken() {
		//This class not using
		return "accountcharts";
	}
}
