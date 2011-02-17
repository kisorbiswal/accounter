package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

public class AccounterAsync {

	public static void createAsync(final CreateViewAsyncCallBack callback) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				callback.onCreated();
			}

			public void onFailure(Throwable e) {
				callback.onCreateFailed(e);
			}
		});
	}

}
