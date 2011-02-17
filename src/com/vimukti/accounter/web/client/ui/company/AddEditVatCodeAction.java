//package com.vimukti.accounter.web.client.ui.company;
//
//import com.google.gwt.resources.client.ImageResource;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
//import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
//import com.vimukti.accounter.web.client.ui.core.Action;
//import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
//import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
//
//public class AddEditVatCodeAction extends Action {
//
//	protected AddEditVatCodeView view;
//
//	public AddEditVatCodeAction(String text) {
//		super(text);
//		this.catagory = FinanceApplication.getCompanyMessages().company();
//	}
//
//	public AddEditVatCodeAction(String text, String iconString) {
//		super(text, iconString);
//		this.catagory = FinanceApplication.getCompanyMessages().company();
//	}
//
//	@Override
////	public ParentCanvas<?> getView() {
//		return this.view;
//	}
//
//	@Override
//	public void run(Object data, Boolean isDependent) {
//		runAsync(data, isDependent);
//	}
//
//	private void runAsync(final Object data, final Boolean isDependent) {
//		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {
//
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed To Load Sales Tax Code Dialog",
//				// t);
//
//			}
//
//			public void onCreated() {
//				try {
//					view = new AddEditVatCodeView();
//					MainFinanceWindow.getViewManager().showView(view, data,
//							isDependent, AddEditVatCodeAction.this);
//				} catch (Throwable e) {
//					onCreateFailed(e);
//
//				}
//
//			}
//		});
//
//	}
//
//	public ImageResource getBigImage() {
//		return null;
//	}
//
//	public ImageResource getSmallImage() {
//		return null;
//	}
//
//}
