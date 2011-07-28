/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * @author Murali.A
 * 
 */
public class RegisteredItemsListAction extends Action {

	private RegisteredItemsListView view;

	/**
	 * @param text
	 */
	public RegisteredItemsListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().fixedAssets();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().registeredItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getView()
	 */
	
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
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new RegisteredItemsListView();
					MainFinanceWindow.getViewManager().showView(view, null,
							false, RegisteredItemsListAction.this);

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
		return "/images/Registered_Items_List.png";
	}

	@Override
	public String getHistoryToken() {

		return "registeredItems";
	}

}
