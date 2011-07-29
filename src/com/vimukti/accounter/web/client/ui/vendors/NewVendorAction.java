package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class NewVendorAction extends Action {

	private boolean isEdit;

	private ClientVendor vendor;
	protected VendorView view;
	public final static int FROM_CREDIT_CARD_EXPENSE = 119;

	private int openedFrom;

	public NewVendorAction(String text) {
		super(UIUtils.getVendorString(Accounter.constants().newSupplier(),
				Accounter.constants().newVendor()));
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public NewVendorAction(String text, ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				try {

					view = new VendorView();
					// UIUtils.setCanvas(view, getViewConfiguration());

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewVendorAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
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
		return UIUtils.getVendorString("newSupplier", "newVendor");
	}
}
