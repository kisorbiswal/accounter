package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
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
		// TODO Auto-generated constructor stub
	}

	public VendorGroupListAction(String text, String iconString) {
		super(text, iconString);
		// TODO Auto-generated constructor stub
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

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Vendor Group List", t);
			}

			public void onCreated() {
				try {

					VendorGroupListDialog dialog = new VendorGroupListDialog(
							UIUtils.getVendorString(
									FinanceApplication.getCompanyMessages()
											.manageSupplierGroup(),
									FinanceApplication.getCompanyMessages()
											.manageVendorGroup()), UIUtils
									.getVendorString(FinanceApplication
											.getCompanyMessages()
											.toAddSupplierGroup(),
											FinanceApplication
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
		return FinanceApplication.getFinanceMenuImages().vendors();
	}
@Override
public String getImageUrl() {
	// TODO Auto-generated method stub
	return "/images/vendors.png";
}
}
