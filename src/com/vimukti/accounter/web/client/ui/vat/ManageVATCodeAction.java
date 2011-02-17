package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ManageVATCodeAction extends Action {

	protected ManageVATView view;

	public ManageVATCodeAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Manage Sales Tax Codes View",
				// t);

			}

			public void onCreated() {
				try {
					view = new ManageVATView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ManageVATCodeAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "";
	}
}
