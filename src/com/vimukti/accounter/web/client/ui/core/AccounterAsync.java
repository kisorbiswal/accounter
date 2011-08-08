package com.vimukti.accounter.web.client.ui.core;


public class AccounterAsync {

	public static void createAsync(
			final CreateViewAsyncCallback callback) {

//		GWT.runAsync(new RunAsyncCallback() {
//
//			public void onSuccess() {
				callback.onCreated();
//			}
//
//			public void onFailure(Throwable e) {
//				callback.onCreateFailed(e);
//			}
//		});
	}

}
