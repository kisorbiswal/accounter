package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WriteChecksAction extends Action {

	public WriteChecksAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public WriteChecksAction(String text, ClientWriteCheck writeCheck,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				WriteChequeView view = WriteChequeView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, WriteChecksAction.this);

			}

			// @Override
			// public void onCreateFailed(Throwable t) {
			// // TODO Auto-generated method stub
			//				
			// }

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public void setToolTip(String toolTip) {
		super.setToolTip(toolTip);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCheck();
	}

	@Override
	public String getHistoryToken() {

		return "writeCheck";
	}

	@Override
	public String getHelpToken() {
		return "write-check";
	}
}
