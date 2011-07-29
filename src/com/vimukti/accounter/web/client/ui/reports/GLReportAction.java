package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author Mandeep Singh
 */

public class GLReportAction extends Action {

	protected GeneralLedgerReport report;

	public GLReportAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public GLReportAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					report = new GeneralLedgerReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, GLReportAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
			}
		});

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	
//	@Override
//	public ParentCanvas getView() {
//		return this.report;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
//	@Override
//	public String getImageUrl() {
//		return "/images/reports.png";
//	}

	@Override
	public String getHistoryToken() {
		return "generalLedger";
	}

}
