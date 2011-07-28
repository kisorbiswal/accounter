package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class TransferFundsAction extends Action {

	public TransferFundsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().banking();
	}

	public TransferFundsAction(String text, String iconString,
			ClientTransferFund transferFund, AsyncCallback<Object> callback) {
		super(text, iconString, transferFund, callback);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					new TransferFundsDialog(data).show();

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Transfer Fund", t);
			}
		});
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().transerFunds();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/transfer_funds.png";
	// }

	@Override
	public String getHistoryToken() {

		return "transferFunds";
	}
}
