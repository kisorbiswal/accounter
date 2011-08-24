package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.MakeDepositView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MakeDepositAction extends Action {
	protected MakeDepositView view;

	// private boolean isEdit;
	// private ClientMakeDeposit makeDeposit;

	public MakeDepositAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public MakeDepositAction(String text, ClientMakeDeposit makeDeposit,
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
				view = MakeDepositView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MakeDepositAction.this);

			}
			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().makeDeposit();
	}

	@Override
	public String getHistoryToken() {
		return "depositTransferFunds";
	}

	@Override
	public String getHelpToken() {
		return "makedeposit";
	}
}
