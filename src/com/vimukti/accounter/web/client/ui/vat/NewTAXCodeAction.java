package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeAction extends Action {

	private NewTAXCodeView view;

	public NewTAXCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().VAT();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesTaxCode();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Manage Sales Tax Codes View",
				// t);

			}

			public void onCreated() {
				try {
					view = new NewTAXCodeView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewTAXCodeAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales_Tax.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newVatCode";
	}

}
