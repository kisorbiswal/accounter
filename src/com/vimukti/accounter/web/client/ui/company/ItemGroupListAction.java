package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemGroupListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class ItemGroupListAction extends Action {

	public ItemGroupListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	public ItemGroupListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
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

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Item groups", t);

			}

			public void onCreated() {
				try {

					ItemGroupListDialog dialog = new ItemGroupListDialog(
							Accounter.constants().manageItemGroup(),
							Accounter.constants().toAddItemGroup());
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
		// NOTHING TO DO.
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
		return "itemGroupList";
	}

}
