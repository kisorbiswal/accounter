package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class EmployeeAction extends Action {

	private EmployeeView view;

	public EmployeeAction() {
		super();
		super.setToolTip(messages.employee());
	}

	@Override
	public String getText() {
		return messages.newPayee(messages.employee());
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new EmployeeView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, EmployeeAction.this);
			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newEmployee";
	}

	@Override
	public String getHelpToken() {
		return "add-employee";
	}

}
