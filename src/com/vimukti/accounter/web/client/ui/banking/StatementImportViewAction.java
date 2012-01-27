package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StatementImportOptionView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class StatementImportViewAction extends Action<List<String[]>> {
	private StatementImportOptionView importOptionView;
	private List<String[]> importedData;
	private long accountId;

	@Override
	public String getText() {
		return "Statement Import Option";
	}

	public StatementImportViewAction(List<String[]> data, long accountId) {
		super();
		this.importedData = data;
		this.accountId = accountId;

	}

	@Override
	public void run() {
		runAsync(data);

	}

	private void runAsync(final Object data) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				importOptionView = new StatementImportOptionView(accountId);
				importOptionView.setImportStatementData(importedData);
				MainFinanceWindow.getViewManager().showView(importOptionView,
						new ClientStatement(), false,
						StatementImportViewAction.this);
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
		return "statementImportOption";
	}

	@Override
	public String getHelpToken() {
		return "statementImportOption";
	}

}
