package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewJournalEntryAction extends Action {

	public NewJournalEntryAction(String text) {
		super(Accounter.constants().newJournalEntry());
		this.catagory = Accounter.constants().company();
	}

	public NewJournalEntryAction(String text, ClientJournalEntry journalEntry,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				JournalEntryView view = JournalEntryView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, NewJournalEntryAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newJournalEntry();
	}

	@Override
	public String getHistoryToken() {
		return "newJournalEntry";
	}

	@Override
	public String getHelpToken() {
		return "new_journal-entry";
	}
}
