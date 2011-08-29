package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AccountRegisterAction extends Action {

	private ClientAccount account;

	public AccountRegisterAction(String text, ClientAccount account) {
		super(text);
		this.account = account;
		this.catagory = Accounter.constants().banking();
	}

	public AccountRegisterAction(String text) {
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
				account = (ClientAccount) data;
				MainFinanceWindow.getViewManager().showView(
						new AccountRegisterOthersView(account), data,
						isEditable, AccountRegisterAction.this);
			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	@Override
	// public ParentCanvas getView() {
	// return null;
	// }
	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return Accounter.constants().accountRegister();
	}

	@Override
	public String getHelpToken() {
		return Accounter.constants().accountRegister();
	}

}
