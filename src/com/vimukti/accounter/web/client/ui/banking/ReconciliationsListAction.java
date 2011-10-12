/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
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
public class ReconciliationsListAction extends Action {

	/**
	 * Creates new Instance
	 */
	public ReconciliationsListAction(String text) {
		super(Accounter.constants().ReconciliationsList());
		this.catagory = Accounter.constants().banking();
	}

	public ReconciliationsListAction(String text,
			ClientReconciliation reconcilation,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ReconciliationsHistoryView view = new ReconciliationsHistoryView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						ReconciliationsListAction.this);
			}

		});
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return Accounter.getFinanceMenuImages().newCheck();
	}

	@Override
	public String getHistoryToken() {
		return "recounciliationsList";
	}

	@Override
	public String getHelpToken() {
		return "recounciliations-list";
	}

}
