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
	private boolean isEditable;

	public NewVendorAction() {
		super();
	}

	public NewVendorAction(String quickAddText) {
		super();
		super.setToolTip(Global.get().Vendor());
		this.quickAddText = quickAddText;

	}

	public NewVendorAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super();
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
				if (isEditable()) {
					view.onEdit();
				}

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

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
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

	public void setVendorName(String text) {
		this.quickAddText = text;
	}

	public void setisVendorViewEditable(boolean isEditable) {
		this.setEditable(isEditable);
	}

	@Override
	public String getText() {
		return Global.get().messages().newPayee(Global.get().Vendor());
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
