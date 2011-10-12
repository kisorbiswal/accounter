/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @author Prasanna Kumar G
 * 
 */
public class NewReconcileAccountAction extends Action<ClientReconciliation> {

	/**
	 * Creates new Instance
	 */
	public NewReconcileAccountAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ReconciliationView view = new ReconciliationView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewReconcileAccountAction.this);
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
