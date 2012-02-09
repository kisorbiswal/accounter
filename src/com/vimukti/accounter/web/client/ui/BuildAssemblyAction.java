package com.vimukti.accounter.web.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class BuildAssemblyAction extends Action<ClientInventoryAssembly> {

	private BuildAssemblyView view;

	public BuildAssemblyAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public String getText() {
		return messages.buildAssembly();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientInventoryAssembly data,
			final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new BuildAssemblyView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, BuildAssemblyAction.this);
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
		return "buildassembly";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

}
