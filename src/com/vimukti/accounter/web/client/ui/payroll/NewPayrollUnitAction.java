package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewPayrollUnitAction extends Action<ClientPayrollUnit> {

	@Override
	public String getText() {
		return messages.newPayrollUnit();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(ClientPayrollUnit data, boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewPayrollUnitDialog dialog = new NewPayrollUnitDialog(messages
						.newPayrollUnit());
				dialog.show();
				dialog.center();
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
