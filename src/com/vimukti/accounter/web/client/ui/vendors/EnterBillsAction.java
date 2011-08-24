package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 * @modified by RaviKiran.G
 */

public class EnterBillsAction extends Action {

	protected VendorBillView view;

	public EnterBillsAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isEditable) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = VendorBillView.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, EnterBillsAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newBill();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	// @Override
	// public String getImageUrl() {
	// return "/images/enter_bills.png";
	// }

	@Override
	public String getHistoryToken() {
		return "enterBill";
	}

	@Override
	public String getHelpToken() {
		return "add-bill";
	}

}
