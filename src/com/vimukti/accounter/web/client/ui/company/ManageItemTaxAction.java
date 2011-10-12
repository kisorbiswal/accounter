package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ManageItemTaxAction extends Action {

	public ManageItemTaxAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				//
				// ManageItemTaxDialog dialog = new ManageItemTaxDialog(
				// "Item Tax Code",
				// "To add an item tax, click Add. To change an Item Tax, select the Item Tax, and click Edit or Remove."
				// );
				// dialog.addCallBack(getViewConfiguration().getCallback());
				// dialog.show();
			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "manageItemTaxAction";
	}

	@Override
	public String getHelpToken() {
		return "item-tax";
	}
}
