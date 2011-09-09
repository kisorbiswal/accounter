package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class NewVendorAction extends Action<ClientVendor> {

	protected VendorView view;
	public final static int FROM_CREDIT_CARD_EXPENSE = 119;

	private int openedFrom;
	private String quickAddText;

	public NewVendorAction(String text) {
		super(Global.get().messages().newVendor(Global.get().Vendor()));
		this.catagory = Global.get().Vendor();
	}

	public NewVendorAction(String text, String quickAddText) {
		super(text);
		this.catagory = Global.get().customer();
		super.setToolTip(Global.get().customer());
		this.quickAddText = quickAddText;

	}

	public NewVendorAction(String text, ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				view = new VendorView();
				if (quickAddText != null) {
					view.prepareForQuickAdd(quickAddText);
				}
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewVendorAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendor();
	}

	public void setOpenedFrom(int openedFrom) {
		this.openedFrom = openedFrom;
	}

	/**
	 * @return the openedFrom
	 */
	public int getOpenedFrom() {
		return openedFrom;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_vendor.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newVendor";
	}

	@Override
	public String getHelpToken() {
		return "add-vendor";
	}
}
