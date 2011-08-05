package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShippingTermListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class ShippingTermListAction extends Action {

	public ShippingTermListAction(String text) {
		super(text);
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Shipping Terms", t);

			}

			public void onCreated() {
				try {

					ShippingTermListDialog dialog = new ShippingTermListDialog(
							Accounter.constants().manageShippingTermList(),
							Accounter.constants().toAddShippingTerm());
					dialog.show();

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
		return Accounter.getFinanceMenuImages().shippingTermList();
	}

	@Override
	public String getHistoryToken() {
		return "shippingTermsList";
	}
}
