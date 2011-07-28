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
 * @author Kumar kasimala
 * @modified by Ravi Kiran.G
 */

public class BillsAction extends Action {

	protected BillListView view;
	public String viewType;

	public BillsAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public BillsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load EnterBillsList", t);

			}

			public void onCreated() {
				if (viewType == null)
					view = BillListView.getInstance();
				else
					view = new BillListView(viewType);

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, null,
							false, BillsAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);
				}

			}

		});
	}

	public void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		run(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bills();
	}

	@Override
	public String getImageUrl() {
		return "images/bills.png";
	}

	@Override
	public String getHistoryToken() {
		return "billsAndExpenses";
	}

}
