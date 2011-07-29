package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.MakeDepositView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class MakeDepositAction extends Action {
	protected MakeDepositView view;

	// private boolean isEdit;
	// private ClientMakeDeposit makeDeposit;

	public MakeDepositAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().banking();
	}

	public MakeDepositAction(String text, String iconString,
			ClientMakeDeposit makeDeposit, AsyncCallback<Object> callback) {
		super(text, iconString);
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
					view = MakeDepositView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, MakeDepositAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load MakeDeposit", t);
			}
		});
	}

//	@Override
//	public ParentCanvas<?> getView() {
//		return this.view;
//	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().makeDeposit();
	}

//	@Override
//	public String getImageUrl() {
//
//		return "/images/make_deposit.png";
//	}

	@Override
	public String getHistoryToken() {
		return "depositTransferFunds";
	}
}
