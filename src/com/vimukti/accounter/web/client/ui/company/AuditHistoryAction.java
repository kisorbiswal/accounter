package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AuditHistoryAction extends Action {

	private int objT;
	private long objID;

	public AuditHistoryAction(String text) {
		super(text);
	}

	public AuditHistoryAction(String text, int objectType, long objectID) {

		super(text);
		objT = objectType;
		objID = objectID;
		// run();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				HistoryView view = new HistoryView(objT, objID);

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, AuditHistoryAction.this);

			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {

		return messages.history();
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
