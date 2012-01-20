package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class DraftsListAction extends Action {
	public DraftsListView view;

	public DraftsListAction() {
		super();
		this.catagory = messages.draft();
	}

	@Override
	public String getText() {
		return "drafts-List";
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {

				view = new DraftsListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, DraftsListAction.this);

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
		return "drafts-List";
	}

	@Override
	public String getHelpToken() {
		return messages.draft();
	}

}
