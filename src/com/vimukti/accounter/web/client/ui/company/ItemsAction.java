package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemListView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class ItemsAction extends Action {

	public ItemsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public ItemsAction(String text, String iconString, String catageory) {
		super(text, iconString);
		this.catagory = catageory;
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load Items List", t);
			}

			public void onCreated() {

				try {

					ItemListView view = new ItemListView();
					view.setCatageoryType(getCatagory());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ItemsAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {

					onCreateFailed(t);
				}

			}

		});
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().items();
	}

	@Override
	public String getImageUrl() {
		return "/images/items.png";
	}

	@Override
	public String getHistoryToken() {
		return "items";
	}

}
