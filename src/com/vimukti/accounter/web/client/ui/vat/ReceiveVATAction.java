package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar
 * 
 */

public class ReceiveVATAction extends Action {

	public ReceiveVATAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
	}

	@Override
	public ImageResource getBigImage() {
		// its not using any where return null;
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// its not using any where return null;
		return null;
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // its not using any where return null;
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				MainFinanceWindow.getViewManager().showView(
						new ReceiveVATView(), data, isDependent,
						ReceiveVATAction.this);

			}

		});
	}

	// @Override
	// public String getImageUrl() {
	// // its not using any where return null;
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		return "receiveVat";
	}

	@Override
	public String getHelpToken() {
		return "receive-vat";
	}

}