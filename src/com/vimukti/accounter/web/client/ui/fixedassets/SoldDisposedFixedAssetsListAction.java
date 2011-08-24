/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @author Murali.A
 * 
 */
public class SoldDisposedFixedAssetsListAction extends Action {

	private SoldAndDisposedItemsListView view;

	/**
	 * @param text
	 */
	public SoldDisposedFixedAssetsListAction(String text) {
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
		return Accounter.getFinanceMenuImages().soldDisposedItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.Action#getView()
	 */

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				view = new SoldAndDisposedItemsListView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						SoldDisposedFixedAssetsListAction.this);

			}

		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sold_Disposed_Items_List.png";
	// }

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "sold-disposed-fixed-assets";
	}

}
