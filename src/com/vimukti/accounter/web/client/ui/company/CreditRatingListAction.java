package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreditRatingListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class CreditRatingListAction extends Action {

	public CreditRatingListAction(String text) {
		super(text);
	}

	public CreditRatingListAction(String text, String iconString) {
		super(text, iconString);
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Credit rating", t);

			}

			public void onCreated() {
				try {

					CreditRatingListDialog dialog = new CreditRatingListDialog(
							Accounter.constants().creditRatingList(),
							Accounter.constants().toAddCreditRating());
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
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().creditRatingList();
	}

	@Override
	public String getImageUrl() {
		return "/images/Creadit_rating_List.png";
	}

	@Override
	public String getHistoryToken() {
		return "creditRatingList";
	}

}
