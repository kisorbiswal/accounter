package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author kumar kasimala
 * @modified by RaviKiran.G
 */

public class EnterBillsAction extends Action {

	protected VendorBillView view;

	public EnterBillsAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public EnterBillsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public void run(Object data, Boolean isEditable) {

		runAsync(data, isEditable);

	}

	private void runAsync(final Object data, final boolean isEditable) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load EnterBill...", t);

			}

			public void onCreated() {

				view = VendorBillView.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isEditable, EnterBillsAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

			}

		});

	}

	
//	@Override
//	public ParentCanvas getView() {
//		return this.view;
//	}

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
//	@Override
//	public String getImageUrl() {
//		return "/images/enter_bills.png";
//	}

	@Override
	public String getHistoryToken() {
		return "enterBill";
	}

}
