package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class NewCheckAction extends Action {

	// private boolean isEdit;

	public NewCheckAction() {
		super();
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				WriteChequeView view = new WriteChequeView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, NewCheckAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//				
//
//			}
//
//			@SuppressWarnings("unused")
//			public void onCreateFailed(Throwable t) {
//				System.err.println((Exception) t);
//				// //UIUtils.logError("Failed to Load WriteChecks..", t);
//			}
//		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCheck();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_check.png";
	// }

	@Override
	public String getHistoryToken() {
		return "check";
	}

	@Override
	public String getHelpToken() {
		return "write-check";
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getText() {
		return messages.newCheck();
	}
}
