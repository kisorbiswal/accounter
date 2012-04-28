package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(NewEmployeeGroupAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewEmployeeGroupDialog dialog = new NewEmployeeGroupDialog(
						messages.newEmployeeGroup());
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
		return HistoryTokens.NEWEMPLOYEEGROUP;
	}

	@Override
	public String getHelpToken() {
		return "new-employee-group";
	}

}
