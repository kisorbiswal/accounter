/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconcileAccountAction extends Action<ClientBankAccount> {

	/**
	 * Creates new Instance
	 */
	public ReconcileAccountAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				ReconciliationView view = new ReconciliationView(data);
				MainFinanceWindow.getViewManager().showView(view, null,
						isDependent, ReconcileAccountAction.this);
			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "recouncileAccount";
	}

	@Override
	public String getHelpToken() {
		return "reconcile-account";
	}

}
