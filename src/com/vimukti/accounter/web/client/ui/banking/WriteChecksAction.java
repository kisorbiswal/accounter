package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

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
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				WriteChequeView view = WriteChequeView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, WriteChecksAction.this);

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
