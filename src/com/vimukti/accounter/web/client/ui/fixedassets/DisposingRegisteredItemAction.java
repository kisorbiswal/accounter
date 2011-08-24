package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class DisposingRegisteredItemAction extends Action {
	private DisposingRegisteredItemView view;;

	public DisposingRegisteredItemAction(String text) {
		super(text);
		this.catagory = Accounter.constants().fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new DisposingRegisteredItemView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, DisposingRegisteredItemAction.this);

			}

		});

	}

	@Override
	public String getHistoryToken() {
		return "disposingRegisteredItem";
	}

	@Override
	public String getHelpToken() {
		return "disposing-register-item";
	}

}
