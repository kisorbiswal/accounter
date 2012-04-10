package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewPayRunAction extends Action<ClientPayRun> {

	@Override
	public String getText() {
		return messages.newPayee(messages.payrun());
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientPayRun data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewPayRunView view = new NewPayRunView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewPayRunAction.this);

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
		return HistoryTokens.NEW_PAYRUN;
	}

	@Override
	public String getHelpToken() {
		return "new_payrun";
	}

}
