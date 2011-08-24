package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ManageFiscalYearAction extends Action {

	public ManageFiscalYearAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		ManageFiscalYearDialog dialog = new ManageFiscalYearDialog(Accounter
				.constants().manageFiscalYear(), "");
		dialog.show();
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().manageFiscalYear();
	}

	@Override
	public String getHistoryToken() {
		return "manageFiscalYear";
	}

	@Override
	public String getHelpToken() {
		return "fiscal-year";
	}
}
