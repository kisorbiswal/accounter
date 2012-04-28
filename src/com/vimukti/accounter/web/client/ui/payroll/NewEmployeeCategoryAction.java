package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEmployeeCategory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewEmployeeCategoryAction extends Action<ClientEmployeeCategory> {

	@Override
	public String getText() {
		return messages.newEmployeeCategory();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, boolean isDependent) {
		GWT.runAsync(NewEmployeeCategoryAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewEmployeeCategoryDialog dialog = new NewEmployeeCategoryDialog(
						messages.newEmployeeCategory());
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
		return HistoryTokens.NEWEMPLOYEECATEGORY;
	}

	@Override
	public String getHelpToken() {
		return "new-employee-category";
	}

}
