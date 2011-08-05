package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AccounterAsyncCallback<T> implements AsyncCallback<T> {

	private PopupPanel processDialog;

	public AccounterAsyncCallback() {
		processDialog = UIUtils.getLoadingMessageDialog(Accounter.constants()
				.processingRequest());

		processDialog.center();
	}

	@Override
	public void onFailure(Throwable exception) {
		processDialog.removeFromParent();
		
		if (exception instanceof AccounterException) {
			onException((AccounterException) exception);
			return;
		}
		exception.printStackTrace();
		Accounter
				.showMessage("Your session expired, Please login again to continue");
	}

	public abstract void onException(AccounterException exception);

	@Override
	public void onSuccess(T result) {
		processDialog.removeFromParent();
	}
}
