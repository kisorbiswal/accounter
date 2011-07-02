package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class WriteChecksAction extends Action {

	public WriteChecksAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	public WriteChecksAction(String text, String iconString,
			ClientWriteCheck writeCheck, AsyncCallback<Object> callback) {
		super(text, iconString, writeCheck, callback);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					WriteChequeView view = WriteChequeView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isEditable, WriteChecksAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load WriteChecks..", t);
			}
		});
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
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
		return FinanceApplication.getFinanceMenuImages().newCheck();
	}

	@Override
	public String getImageUrl() {

		return "/images/new_check.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "writeCheck";
	}
}
