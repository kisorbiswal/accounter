package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class PriceLevelListAction extends Action {

	public PriceLevelListAction(String text) {
		super(text);
	}

	public PriceLevelListAction(String text, String iconString) {
		super(text, iconString);
	}

	// @Override
	// public ParentCanvas<?> getView() {
	//
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Price level", t);
			}

			public void onCreated() {
				try {

					PriceLevelListDialog dialog = new PriceLevelListDialog(
							Accounter.constants().managePriceLevelListGroup(),
							" ");
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
		return Accounter.getFinanceMenuImages().priceLevelList();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/Price_Level_list.png";
	// }

	@Override
	public String getHistoryToken() {

		return "priceLevels";
	}
}
