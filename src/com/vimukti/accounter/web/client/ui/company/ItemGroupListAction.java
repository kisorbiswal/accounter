package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
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
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public ItemGroupListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
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
				// //UIUtils.logError("Failed To Load Item groups", t);

			}

			public void onCreated() {
				try {

					ItemGroupListDialog dialog = new ItemGroupListDialog(
							FinanceApplication.getCompanyMessages()
									.manageItemGroup(), FinanceApplication
									.getCompanyMessages().toAddItemGroup());
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
		return FinanceApplication.getFinanceMenuImages().items();
	}
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/items.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "itemGroupList";
	}

}
