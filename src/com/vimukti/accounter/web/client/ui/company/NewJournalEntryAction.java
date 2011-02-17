package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewJournalEntryAction extends Action {

	public NewJournalEntryAction(String text, String iconString) {
		super(FinanceApplication.getCompanyMessages().newJournalEntry(),
				iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public NewJournalEntryAction(String text, String iconString,
			ClientJournalEntry journalEntry, AsyncCallback<Object> callback) {
		super(text, iconString, journalEntry, callback);
		this.catagory = FinanceApplication.getCompanyMessages().company();
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

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().newJournalEntry();
	}
@Override
public String getImageUrl() {
	// TODO Auto-generated method stub
	return "/images/new_journal_entry.png";
}
}
