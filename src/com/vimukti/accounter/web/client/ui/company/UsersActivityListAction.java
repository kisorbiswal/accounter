package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class UsersActivityListAction extends Action<ClientActivity> {

	public UsersActivityListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		runAsynce(data, isDependent);
	}

	public void runAsynce(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				UsersActivityListView view = new UsersActivityListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, UsersActivityListAction.this);
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "userActivity";
	}

	@Override
	public String getHelpToken() {
		return "users-activity-log";
	}

}
