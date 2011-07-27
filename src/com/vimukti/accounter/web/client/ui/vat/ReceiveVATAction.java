package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar
 * 
 */

public class ReceiveVATAction extends Action {

	public ReceiveVATAction(String text) {
		super(text);
		this.catagory = Accounter.getVATMessages().VAT();
	}

	public ReceiveVATAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getVATMessages().VAT();
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

	@Override
	public ParentCanvas<?> getView() {
		// its not using any where return null;
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load PaySalesTax..", t);
				return;
			}

			public void onCreated() {

				try {
					MainFinanceWindow.getViewManager().showView(
							new RecieveVATView(), data, isDependent,
							ReceiveVATAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

		});
	}

	@Override
	public String getImageUrl() {
		// its not using any where return null;
		return "";
	}

	@Override
	public String getHistoryToken() {
		return "receiveVat";
	}

}