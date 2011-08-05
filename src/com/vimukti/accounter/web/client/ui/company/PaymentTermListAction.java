package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class PaymentTermListAction extends Action {
	public PaymentTermListAction(String text) {
		super(text);
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To  Load payment terms", t);

			}

			public void onCreated() {
				try {

					PaymentTermListDialog dialog = new PaymentTermListDialog();
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
		return Accounter.getFinanceMenuImages().paymentTermsList();
	}

	@Override
	public String getHistoryToken() {
		return "paymentTerms";
	}
}
