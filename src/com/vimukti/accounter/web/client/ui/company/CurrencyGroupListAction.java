package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.customers.CurrencyGroupListDialog;

/**
 * this action class is used to display currencyGroupListDialog view
 */
public class CurrencyGroupListAction extends Action {
	private AccounterConstants constants = Global.get().constants();

	public CurrencyGroupListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				CurrencyGroupListDialog groupListDialog = new CurrencyGroupListDialog(
						constants.manageCurrency(), constants
								.toAddCurrencyGroup());
				groupListDialog.show();
				groupListDialog.center();
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter.showError(constants.unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "currency-group";
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "currencyGroupList";
	}

}
