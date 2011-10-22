package com.vimukti.accounter.web.client.ui.core;


public class AccounterAsync {

//	private static PopupPanel loadingMessageDialog;

	public static void createAsync(final CreateViewAsyncCallback callback) {

//		loadingMessageDialog = UIUtils.getLoadingMessageDialog(Accounter
//				.constants().processingRequest());
//		loadingMessageDialog.center();

//		GWT.runAsync(new RunAsyncCallback() {

//			public void onSuccess() {
//				loadingMessageDialog.removeFromParent();
				callback.onCreated();
//			}

//			public void onFailure(Throwable e) {
//				loadingMessageDialog.removeFromParent();
//				Accounter
//						.showError(Accounter.constants().unableToshowtheview());
//			}
//		});
	}

	/*
	 * public static void runAsync(final RunAsyncCallback callback) {
	 * 
	 * GWT.runAsync(new RunAsyncCallback() {
	 * 
	 * public void onSuccess() { callback.onSuccess(); }
	 * 
	 * public void onFailure(Throwable e) { callback.onFailure(e); } }); }
	 */
}
