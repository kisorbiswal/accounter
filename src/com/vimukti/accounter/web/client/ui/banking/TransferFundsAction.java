package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TransferFundsAction extends Action {

	public TransferFundsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public TransferFundsAction(String text, ClientTransferFund transferFund,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				new TransferFundsDialog(data).show();

			}

//			@Override
//			public void onCreateFailed(Throwable t) {
//				// TODO Auto-generated method stub
//				
//			}

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
		return Accounter.getFinanceMenuImages().transerFunds();
	}

	@Override
	public String getHistoryToken() {
		return "transferFunds";
	}

	@Override
	public String getHelpToken() {
		return "deposit_transfer-funds";
	}
}
