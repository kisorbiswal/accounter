package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewCashBasisJournalEntryAction extends Action {
	@SuppressWarnings("unused")
	private ClientJournalEntry journalEntry;
	@SuppressWarnings("unused")
	private boolean isEdit;

	public NewCashBasisJournalEntryAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	public NewCashBasisJournalEntryAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {

		try {
			MainFinanceWindow.getViewManager().showView(new JournalEntryView(),
					null, false, this);
		} catch (Exception e) {
			Accounter.showError(FinanceApplication.getCompanyMessages()
					.failedToLoadCashBasisJournalEntryFailed());
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

}
