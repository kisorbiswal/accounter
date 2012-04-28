package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayRun;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(NewPayRunAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewPayRunView view = new NewPayRunView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewPayRunAction.this);
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
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
