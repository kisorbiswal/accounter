package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShippingMethodListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class ShippingMethodListAction extends Action {

	public ShippingMethodListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ShippingMethodListDialog dialog = new ShippingMethodListDialog(
						Accounter.constants().manageShippingMethodList(),
						Accounter.constants().toAddShippingMethod());
				dialog.show();

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().shippingMethodsList();
	}

	@Override
	public String getHistoryToken() {
		return "shippingMethodsList";
	}

	@Override
	public String getHelpToken() {
		return "shipping_method-list";
	}
}