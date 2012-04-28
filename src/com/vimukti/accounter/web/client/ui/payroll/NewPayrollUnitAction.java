package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewPayrollUnitAction extends Action<ClientPayrollUnit> {

	@Override
	public String getText() {
		return messages.newPayrollUnit();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientPayrollUnit data, boolean isDependent) {
		GWT.runAsync(NewPayrollUnitAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewPayrollUnitDialog dialog = new NewPayrollUnitDialog(messages
						.newPayrollUnit());
				dialog.setData(data);
				dialog.show();
				dialog.center();
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
		return HistoryTokens.NEWPAYROLLUNIT;
	}

	@Override
	public String getHelpToken() {
		return "new-payroll-unit";
	}

}
