package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class NewPayeeAction extends Action<ClientPayee> {

	private boolean isEdit;

	private AbstractBaseView baseView;

	public NewPayeeAction(String text, AbstractBaseView baseView) {
		super(text);
		this.baseView = baseView;

	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	public void run(FormItem actionSource, Object data, Boolean isDependent) {
		runAsync(actionSource, data, isDependent);

	}

	@Override
	public void run() {
		runAsync(null, data, isDependent);

	}

	private void runAsync(final FormItem actionSource, Object data,
			Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to opn Payee dialog", t);
			}

			public void onCreated() {

				try {
					new SelectPayeeDialog(baseView, actionSource).show();

				} catch (Throwable t) {

					onCreateFailed(t);
				}

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newPayee";
	}

}