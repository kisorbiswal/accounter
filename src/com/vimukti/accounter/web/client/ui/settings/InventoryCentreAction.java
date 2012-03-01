package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryCentreAction extends Action {

	protected InventoryCentreView view;

	public InventoryCentreAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public String getText() {
		return messages.inventoryCentre();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new InventoryCentreView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, InventoryCentreAction.this);

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
		return "inventoryCentre";
	}

	@Override
	public String getHelpToken() {
		return "inventoryCentre";
	}

}
