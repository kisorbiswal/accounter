package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ManageVATCodeAction extends Action {

	protected ManageVATView view;

	public ManageVATCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().tax();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new ManageVATView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageVATCodeAction.this);

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// // NOTHING TO DO.
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "manage-vat-code";
	}
}
