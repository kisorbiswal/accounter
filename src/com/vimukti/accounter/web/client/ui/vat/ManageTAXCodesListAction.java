/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * @author Murali.A
 * 
 */
public class ManageTAXCodesListAction extends Action {

	private ManageTAXCodesListView view;

	/**
	 * @param text
	 */
	public ManageTAXCodesListAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	/**
	 * @param text
	 * @param iconString
	 */
	public ManageTAXCodesListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getVATMessages().VAT();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().manageSalesTaxGroup();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new ManageTAXCodesListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ManageTAXCodesListAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Vendor View..", t);
			}
		});
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/Manage_Sales_Tax_Group.png";
	}
}
