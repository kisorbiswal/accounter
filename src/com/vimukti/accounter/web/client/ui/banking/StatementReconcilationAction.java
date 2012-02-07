package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StatementTransactionsReconcileView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class StatementReconcilationAction extends Action<ClientReconciliation> {
	private StatementTransactionsReconcileView statementTransactionsView;
	private long accountid;
	private ClientStatement statementId;

	public StatementReconcilationAction(long accountid,
			ClientStatement statementId) {
		super();
		this.accountid = accountid;
		this.statementId = statementId;
	}

	@Override
	public String getText() {
		return messages.Reconciliation();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				statementTransactionsView = new StatementTransactionsReconcileView(
						accountid, statementId);
				MainFinanceWindow.getViewManager().showView(
						statementTransactionsView, data, isDependent,
						StatementReconcilationAction.this);
			}

			public void onCreateFailed(Throwable t) {
				/* UIUtils.logError */System.err
						.println("Failed to Load Report.." + t);
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
		return "Reconcilation";
	}

	@Override
	public String getHelpToken() {
		return "Bank-Statement";
	}

}
