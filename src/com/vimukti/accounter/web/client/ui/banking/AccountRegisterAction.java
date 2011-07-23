package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AccountRegisterAction extends Action {

	private ClientAccount account;

	public AccountRegisterAction(String text, String icon, ClientAccount account) {
		super(text, icon);
		this.account = account;
		this.catagory = Accounter.getFinanceUIConstants().banking();
	}

	public AccountRegisterAction(String text, String icon) {
		super(text, icon);
		this.catagory = Accounter.getFinanceUIConstants().banking();
	}

	public void run() {

		runAsync(null, false);

	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					account = (ClientAccount) data;
					// if (account.getType() == ClientAccount.TYPE_CREDIT_CARD
					// || account.getType() == ClientAccount.TYPE_BANK) {
					// MainFinanceWindow.getViewManager().showView(
					// new AccountRegisterView(account), data,
					// isEditable, AccountRegisterAction.this);
					// } else {
					MainFinanceWindow.getViewManager().showView(
							new AccountRegisterOthersView(account), data,
							isEditable, AccountRegisterAction.this);
					// }

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Account Register....", t);
			}
		});
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
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
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "accountRegister";
	}

}
