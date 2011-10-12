package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class PriceLevelListAction extends Action {

	public PriceLevelListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				PriceLevelListDialog dialog = new PriceLevelListDialog(
						Accounter.constants().managePriceLevelListGroup(), " ");
				dialog.show();

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().priceLevelList();
	}

	@Override
	public String getHistoryToken() {

		return "priceLevels";
	}

	@Override
	public String getHelpToken() {
		return "price_level-list";
	}
}
