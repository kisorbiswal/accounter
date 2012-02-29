package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class UsersActivityListAction extends Action<ClientActivity> {

	public UsersActivityListAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.USER_ACTIVITY)) {
			runAsynce(data, isDependent);
		} else {
			Accounter.showSubscriptionWarning();
		}
	}

	public void runAsynce(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				UsersActivityListView view = new UsersActivityListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, UsersActivityListAction.this);
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

	@Override
	public String getText() {
		return messages.usersActivityLogTitle();
	}

}
