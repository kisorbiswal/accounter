package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewPayHeadAction extends Action<ClientPayHead> {

	public NewPayHeadAction() {
		super();
	}

	@Override
	public String getText() {
		return messages.newPayHead();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientPayHead data, final boolean isDependent) {
		GWT.runAsync(NewPayHeadAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewPayHeadView view = new NewPayHeadView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewPayHeadAction.this);
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
		return HistoryTokens.NEWPAYHEAD;
	}

	@Override
	public String getHelpToken() {
		return "new-payhead";
	}

}
