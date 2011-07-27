package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class JournalEntriesAction extends Action {

	public JournalEntriesAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public JournalEntriesAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {
					JournalEntryListView view = new JournalEntryListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, JournalEntriesAction.this);

				} catch (Throwable t) {
					onCreateFailed(t);
				}
			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load the Journal Entries", t);
			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().journalEntriesList();
	}

	@Override
	public String getImageUrl() {
		return "/images/Jonoul_entries.png";
	}

	@Override
	public String getHistoryToken() {
		return "journalEntries";
	}

}
