package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;


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
	public static void runAsync(
			final RunAsyncCallback callback) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				callback.onSuccess();
			}

			public void onFailure(Throwable e) {
				callback.onFailure(e);
			}
		});
	}

}
