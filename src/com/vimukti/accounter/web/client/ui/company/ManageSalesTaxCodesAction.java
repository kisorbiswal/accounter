//package com.vimukti.accounter.web.client.ui.company;
//
//import com.google.gwt.resources.client.ImageResource;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
//import com.vimukti.accounter.web.client.ui.SalesTaxCodesView;
//import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
//import com.vimukti.accounter.web.client.ui.core.Action;
//import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
//import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
//
//public class ManageSalesTaxCodesAction extends Action {
//
//	protected SalesTaxCodesView view;
//
//	public ManageSalesTaxCodesAction(String text) {
//		super(text);
//		this.catagory = FinanceApplication.constants().company();
//	}
//
//	public ManageSalesTaxCodesAction(String text, String iconString) {
//		super(text, iconString);
//		this.catagory = FinanceApplication.constants().company();
//	}
//
//	@Override
//	public ParentCanvas<?> getView() {
//		return this.view;
//	}
//
//	@Override
//	public void run(Object data, Boolean isDependent) {
//
//		runAsync(data, isDependent);
//	}
//
//	private void runAsync(final Object data, final Boolean isDependent) {
//		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {
//
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed To Load Manage Sales Tax Codes View",
//				// t);
//
//			}
//
//			public void onCreated() {
//				try {
//					view = new SalesTaxCodesView(FinanceApplication
//							.constants().tax());
//					MainFinanceWindow.getViewManager().showView(view, data,
//							isDependent, ManageSalesTaxCodesAction.this);
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
//		return FinanceApplication.getFinanceMenuImages().salesTaxCode();
//	}
//	@Override
//	public String getImageUrl() {
//		return "/images/Sales Tax.png";
//	}
//
//}
