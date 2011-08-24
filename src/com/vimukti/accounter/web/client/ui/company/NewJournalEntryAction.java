package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				JournalEntryView view = JournalEntryView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, NewJournalEntryAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
