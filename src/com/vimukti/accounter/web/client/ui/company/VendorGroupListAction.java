package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.vendors.VendorGroupListDialog;

public class VendorGroupListAction extends Action {

	public VendorGroupListAction(String text) {
		super(text);
	}

	public VendorGroupListAction(String text, String iconString) {
		super(text, iconString);
	}

	// its not using any where
	@Override
	public ParentCanvas<?> getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Vendor Group List", t);
			}

			public void onCreated() {
				try {

					VendorGroupListDialog dialog = new VendorGroupListDialog(
							UIUtils.getVendorString(
									Accounter.getCompanyMessages()
											.manageSupplierGroup(), Accounter
											.getCompanyMessages()
											.manageVendorGroup()), UIUtils
									.getVendorString(Accounter
											.getCompanyMessages()
											.toAddSupplierGroup(), Accounter
											.getCompanyMessages()
											.toAddVendorGroup()));
					ViewManager viewManager = ViewManager.getInstance();
					viewManager.setCurrentDialog(dialog);
					// dialog.addCallBack(getViewConfiguration().getCallback());

					dialog.show();

				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendors();
	}

	@Override
	public String getImageUrl() {
		return "/images/vendors.png";
	}

	@Override
	public String getHistoryToken() {
		return UIUtils.getVendorString("supplierGroupList", "vendorGroupList");
	}
}
