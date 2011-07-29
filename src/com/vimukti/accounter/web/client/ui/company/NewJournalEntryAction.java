package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class NewJournalEntryAction extends Action {

	public NewJournalEntryAction(String text) {
		super(Accounter.constants().newJournalEntry());
		this.catagory = Accounter.constants().company();
	}

	public NewJournalEntryAction(String text, ClientJournalEntry journalEntry,
			AsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run(Object data, Boolean isEditable) {
		runAsync(data, isEditable);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					JournalEntryView view = JournalEntryView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isEditable, NewJournalEntryAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load JournalEntry...", t);
			}
		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newJournalEntry();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_journal_entry.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newJournalEntry";
	}
}
