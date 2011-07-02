package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class PayVATAction extends Action {

	public PayVATAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	public PayVATAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load PaySalesTax..", t);
				return;
			}

			public void onCreated() {

				try {
					MainFinanceWindow.getViewManager().showView(
							new PayVATView(), data, isDependent, PayVATAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

		});
	}
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "payVat";
	}

}
