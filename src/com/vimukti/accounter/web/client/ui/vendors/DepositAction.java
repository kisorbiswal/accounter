package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.DepositView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class DepositAction extends Action {

	public DepositAction() {
		super();
		this.catagory = messages.banking();
	}

	public DepositAction(ClientMakeDeposit deposit,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = messages.banking();
	}

	@Override
	public String getText() {
		return messages.deposit();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				DepositView view = DepositView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, DepositAction.this);

			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "deposit";
	}

	@Override
	public String getHelpToken() {
		return "deposit";
	}

}
