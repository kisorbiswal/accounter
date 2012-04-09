package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewEmployeeGroupAction extends Action<ClientEmployeeGroup> {

	@Override
	public String getText() {
		return messages.employeeGroup();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(ClientEmployeeGroup data, boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewEmployeeGroupDialog dialog = new NewEmployeeGroupDialog(
						messages.newEmployeeGroup());
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
		return HistoryTokens.NEWEMPLOYEEGROUP;
	}

	@Override
	public String getHelpToken() {
		return "new-employee-group";
	}

}
