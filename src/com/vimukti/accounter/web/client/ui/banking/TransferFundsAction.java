package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class TransferFundsAction extends Action {

	public TransferFundsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	public TransferFundsAction(String text, String iconString,
			ClientTransferFund transferFund, AsyncCallback<Object> callback) {
		super(text, iconString, transferFund, callback);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
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

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().transerFunds();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/transfer_funds.png";
	}
}
