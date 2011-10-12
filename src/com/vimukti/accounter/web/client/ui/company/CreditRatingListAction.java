package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditRatingListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class CreditRatingListAction extends Action {

	public CreditRatingListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				CreditRatingListDialog dialog = new CreditRatingListDialog(
						Accounter.constants().creditRatingList(), Accounter
								.constants().toAddCreditRating());
				dialog.show();

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().creditRatingList();
	}

	@Override
	public String getHistoryToken() {
		return "creditRatingList";
	}

	@Override
	public String getHelpToken() {
		return "credit-rating";
	}

}
