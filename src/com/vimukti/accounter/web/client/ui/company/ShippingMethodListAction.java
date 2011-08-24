package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShippingMethodListDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				ShippingMethodListDialog dialog = new ShippingMethodListDialog(
						Accounter.constants().manageShippingMethodList(),
						Accounter.constants().toAddShippingMethod());
				dialog.show();

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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