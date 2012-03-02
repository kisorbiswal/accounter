package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class JobListAction extends Action<ClientJob> {

	@Override
	public String getText() {
		return "Jobs List";
	}

	@Override
	public void run() {
		runAysnc(data, isDependent);

	}

	private void runAysnc(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				JobListView jobListView = new JobListView();
				MainFinanceWindow.getViewManager().showView(jobListView, data,
						isDependent, JobListAction.this);

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
		return "jobList";
	}

	@Override
	public String getHelpToken() {
		return "jobs-list";
	}

}
