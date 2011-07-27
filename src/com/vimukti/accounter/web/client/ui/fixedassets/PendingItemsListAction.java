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
public class PendingItemsListAction extends Action {

	private PendingItemsListView view;

	public PendingItemsListAction(String text) {
		super(text);
		this.catagory = Accounter.getFixedAssetConstants()
				.fixedAssets();

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().pendingItemsList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new PendingItemsListView();
					MainFinanceWindow.getViewManager().showView(view, null,
							false, PendingItemsListAction.this);

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
		return "/images/pending_items_list.png";
	}

	@Override
	public String getHistoryToken() {
		return "pendingItems";
	}

}
