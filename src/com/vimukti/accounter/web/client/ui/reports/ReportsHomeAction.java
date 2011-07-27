package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Mandeep Singh
 */

public class ReportsHomeAction extends Action {

	public ReportsHomeAction(String text) {
		super(text);
		this.catagory = Accounter.getReportsMessages().report();
	}

	public ReportsHomeAction(String text, String iconString) {
		super(text, iconString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {

		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			MainFinanceWindow.getViewManager().showView(
					new ReportSectionView(), null, isDependent,
					ReportsHomeAction.this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getReportsMessages()
					.failedtToLoadReportsHome());
		}
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reportsHome();
	}

	@Override
	public String getImageUrl() {
		return "/images/report_home.png";
	}

	@Override
	public String getHistoryToken() {

		return "reportHome";
	}

}
