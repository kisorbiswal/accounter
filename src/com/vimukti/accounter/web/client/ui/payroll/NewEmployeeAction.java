package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewEmployeeAction extends Action<ClientEmployee> {

	public NewEmployeeAction() {
		super();
	}

	@Override
	public String getText() {
		return messages.newEmployee();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientEmployee data, final boolean isDependent) {
		GWT.runAsync(NewEmployeeAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewEmployeeView view = new NewEmployeeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewEmployeeAction.this);
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
		return HistoryTokens.NEWEMPLOYEE;
	}

	@Override
	public String getHelpToken() {
		return "new-employee";
	}

}
