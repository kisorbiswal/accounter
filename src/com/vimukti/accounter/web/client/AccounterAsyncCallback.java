package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AccounterAsyncCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable exception) {
		if (exception instanceof AccounterException) {
			onException((AccounterException) exception);
			return;
		}
		exception.printStackTrace();
		Accounter
				.showMessage("Your session expired, Please login again to continue");
	}

	public abstract void onException(AccounterException exception);
}
