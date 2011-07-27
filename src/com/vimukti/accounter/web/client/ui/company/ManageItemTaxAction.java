package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ManageItemTaxAction extends Action {

	public ManageItemTaxAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public ManageItemTaxAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Item Tax", t);

			}

			public void onCreated() {
				try {
					//
					// ManageItemTaxDialog dialog = new ManageItemTaxDialog(
					// "Item Tax Code",
					// "To add an item tax, click Add. To change an Item Tax, select the Item Tax, and click Edit or Remove."
					// );
					// dialog.addCallBack(getViewConfiguration().getCallback());
					// dialog.show();

				} catch (Throwable e) {
					onCreateFailed(e);

				}

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
}
