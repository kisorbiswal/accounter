package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class VendorsListAction extends Action {

	public VendorsListAction(String icon) {
		super("Vendors", icon);
		this.catagory = UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor());
	}

	public VendorsListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					// UIUtils.setCanvas(new VendorListView(),
					// getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(
							new VendorListView(), null, false,
							VendorsListAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor list", t);
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
		// TODO Auto-generated method stub
		return "/images/vendors.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return UIUtils.getVendorString("supplierList", "VendorList");
	}

}
